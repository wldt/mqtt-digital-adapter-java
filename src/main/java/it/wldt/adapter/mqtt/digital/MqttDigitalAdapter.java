package it.wldt.adapter.mqtt.digital;

import it.wldt.adapter.mqtt.digital.topic.incoming.DigitalTwinIncomingTopic;
import it.wldt.adapter.mqtt.digital.topic.outgoing.DigitalTwinOutgoingTopic;
import it.wldt.adapter.mqtt.digital.topic.outgoing.EventNotificationOutgoingTopic;
import it.wldt.adapter.mqtt.digital.topic.outgoing.PropertyOutgoingTopic;
import it.wldt.adapter.digital.DigitalAdapter;
import it.wldt.core.state.*;
import it.wldt.exception.EventBusException;
import it.wldt.exception.WldtDigitalTwinStateEventException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The `MqttDigitalAdapter` class is a Digital Adapter implementation for MQTT communication. It extends the
 * `DigitalAdapter` class and provides functionality to interact with a Digital Twin over MQTT. The adapter
 * subscribes to incoming topics, handles state updates, and publishes events and property changes over MQTT.
 *
 * The class utilizes the Eclipse Paho MQTT client library for MQTT communication.
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class MqttDigitalAdapter extends DigitalAdapter<MqttDigitalAdapterConfiguration> {
    
    private final static Logger logger = LoggerFactory.getLogger(MqttDigitalAdapter.class);
    
    private final MqttClient mqttClient;

    /**
     * Constructs an instance of the `MqttDigitalAdapter` class with the specified identifier and configuration.
     * It initializes the MQTT client with the provided broker connection details.
     *
     * @param id The unique identifier for the adapter.
     * @param configuration The configuration for the MQTT Digital Adapter.
     * @throws MqttException If there is an issue with the MQTT client initialization.
     */
    public MqttDigitalAdapter(String id, MqttDigitalAdapterConfiguration configuration) throws MqttException {
        super(id, configuration);
        mqttClient = new MqttClient(getConfiguration().getBrokerConnectionString(),
                getConfiguration().getClientId(),
                getConfiguration().getPersistence());
    }

    /**
     * Callback method allowing the Digital Adapter to receive the updated Digital Twin State together with
     * the previous state and the list of applied changes
     *
     * @param newDigitalTwinState The new Digital Twin State computed by the Shadowing Function
     * @param previousDigitalTwinState The previous Digital Twin State
     * @param digitalTwinStateChangeList The list of applied changes to compute the new Digital Twin State
     */
    @Override
    protected void onStateUpdate(DigitalTwinState newDigitalTwinState, DigitalTwinState previousDigitalTwinState, ArrayList<DigitalTwinStateChange> digitalTwinStateChangeList) {

        if (digitalTwinStateChangeList != null && !digitalTwinStateChangeList.isEmpty()) {

            // Iterate through each state change in the list
            for (DigitalTwinStateChange stateChange : digitalTwinStateChangeList) {

                // Get information from the state change
                DigitalTwinStateChange.Operation operation = stateChange.getOperation();
                DigitalTwinStateChange.ResourceType resourceType = stateChange.getResourceType();
                DigitalTwinStateResource resource = stateChange.getResource();

                // Search for property variation or a property value variation
                if((((resourceType.equals(DigitalTwinStateChange.ResourceType.PROPERTY) && operation.equals(DigitalTwinStateChange.Operation.OPERATION_UPDATE)) || ((resourceType.equals(DigitalTwinStateChange.ResourceType.PROPERTY_VALUE)) && operation.equals(DigitalTwinStateChange.Operation.OPERATION_UPDATE_VALUE)))
                        && (resource instanceof DigitalTwinStateProperty))){

                    DigitalTwinStateProperty<?> digitalTwinStateProperty = (DigitalTwinStateProperty<?>) resource;

                    if(getConfiguration().getPropertyUpdateTopics().containsKey(digitalTwinStateProperty.getKey())){
                        PropertyOutgoingTopic<?> outgoingTopic = getConfiguration().getPropertyUpdateTopics().get(digitalTwinStateProperty.getKey());
                        publishOnDigitalTwinOutgoingTopic(outgoingTopic, outgoingTopic.applyPublishFunction(digitalTwinStateProperty));
                    }
                }
            }
        } else {
            // No state changes
            logger.info("No relevant DT's state changes detected !");
        }
    }

    /**
     * Callback method to receive a new computed Event Notification (associated to event declared in the DT State)
     * In the case of the MQTT Digital Adapter, it sends the received event over MQTT on the configured topic.
     *
     * @param digitalTwinStateEventNotification The generated Notification associated to a DT Event
     */
    @Override
    protected void onEventNotificationReceived(DigitalTwinStateEventNotification<?> digitalTwinStateEventNotification) {
        logger.info("MQTT Digital Adapter({}) - received event: {}", this.getId(), digitalTwinStateEventNotification.getDigitalEventKey());
        if(this.getConfiguration().getEventNotificationTopics().containsKey(digitalTwinStateEventNotification.getDigitalEventKey())){
            EventNotificationOutgoingTopic<?> outgoingTopic = getConfiguration().getEventNotificationTopics().get(digitalTwinStateEventNotification.getDigitalEventKey());
            publishOnDigitalTwinOutgoingTopic(outgoingTopic, outgoingTopic.applyPublishFunction(digitalTwinStateEventNotification));
        }
    }

    /**
     * Callback to notify the adapter on its correct startup
     */
    @Override
    public void onAdapterStart() {
        connectToMqttBroker();
        getConfiguration().getActionIncomingTopics().values().forEach(this::subscribeClientToDigitalTwinIncomingTopic);
        notifyDigitalAdapterBound();
    }

    /**
     * Callback to notify the adapter that has been stopped
     */
    @Override
    public void onAdapterStop() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * DT Life Cycle notification that the DT is correctly on Sync.
     * In this case the MQTT Digital Adapter observ the list of events for their variation with the aim to
     * publish the associated value over MQTT.
     *
     * @param currentDigitalTwinState
     */
    @Override
    public void onDigitalTwinSync(DigitalTwinState currentDigitalTwinState) {
        try {
//            currentDigitalTwinState.getActionList().ifPresent(actions ->
//                actions.stream()
//                        .map(DigitalTwinStateAction::getKey)
//                        .filter(a -> getConfiguration().getActionIncomingTopics().containsKey(a))
//                        .map(a -> getConfiguration().getActionIncomingTopics().get(a))
//                        .forEach(this::subscribeClientToDigitalTwinIncomingTopic));

            currentDigitalTwinState.getEventList()
                    .map(events -> events.stream()
                            .map(DigitalTwinStateEvent::getKey)
                            .filter(key -> getConfiguration().getEventNotificationTopics().containsKey(key))
                            .collect(Collectors.toList()))
                    .ifPresent(l -> {
                        try {
                            observeDigitalTwinEventsNotifications(l);
                        } catch (EventBusException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (WldtDigitalTwinStateEventException e) {
            e.printStackTrace();
        }
    }

    /**
     * DT Life Cycle notification that the DT is currently Not Sync
     * @param currentDigitalTwinState
     */
    @Override
    public void onDigitalTwinUnSync(DigitalTwinState currentDigitalTwinState) {

    }

    /**
     * DT Life Cycle notification that the DT has been created
     */
    @Override
    public void onDigitalTwinCreate() {

    }

    /**
     * DT Life Cycle Notification that the DT has correctly Started
     */
    @Override
    public void onDigitalTwinStart() {

    }

    /**
     * DT Life Cycle Notification that the DT has been stopped
     */
    @Override
    public void onDigitalTwinStop() {

    }

    /**
     * DT Life Cycle Notification that the DT has destroyed
     */
    @Override
    public void onDigitalTwinDestroy() {

    }

    /**
     * Publishes a message on the specified Digital Twin outgoing topic using MQTT.
     *
     * @param topic   The Digital Twin outgoing topic to publish the message on.
     * @param payload The message payload to be published.
     */
    private void publishOnDigitalTwinOutgoingTopic(DigitalTwinOutgoingTopic<?> topic, String payload){
        try {
            MqttMessage msg = new MqttMessage(payload.getBytes());
            msg.setQos(topic.getQos());
            msg.setRetained(true);
            mqttClient.publish(topic.getTopic(), msg);
            logger.info("MQTT Digital Adapter - MQTT client published message: {} on topic: {}", payload, topic.getTopic());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Subscribes the MQTT client to the specified Digital Twin incoming topic.
     * Upon receiving a message, it invokes the corresponding function to publish a Digital Action Wldt Event.
     *
     * @param topic The Digital Twin incoming topic to subscribe to.
     */
    private void subscribeClientToDigitalTwinIncomingTopic(DigitalTwinIncomingTopic topic) {
        try {
            mqttClient.subscribe(topic.getTopic(), topic.getQos(), (t, msg) ->{
                logger.info("MQTT Digital Adapter -receive message on topic: {}", t);
                //TODO: evaluate improvement
                new Thread(() -> {
                    try {
                        publishDigitalActionWldtEvent(topic.applySubscribeFunction(new String(msg.getPayload())));
                    } catch (EventBusException e) {
                        e.printStackTrace();
                    }
                }).start();
            });
            logger.info("MQTT Digital Adapter - MQTT client subscribed to topic: {}", topic.getTopic());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects the MQTT client to the configured broker using the provided connection options.
     * Logs information about the successful connection.
     */
    private void connectToMqttBroker(){
        try {
            mqttClient.connect(getConfiguration().getConnectOptions());
            logger.info("MQTT Digital Adapter - MQTT client connected to broker - clientId: {}", getConfiguration().getClientId());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
