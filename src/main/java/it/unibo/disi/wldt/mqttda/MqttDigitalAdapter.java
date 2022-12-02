package it.unibo.disi.wldt.mqttda;

import it.unibo.disi.wldt.mqttda.topic.incoming.DigitalTwinIncomingTopic;
import it.unibo.disi.wldt.mqttda.topic.outgoing.DigitalTwinOutgoingTopic;
import it.unibo.disi.wldt.mqttda.topic.outgoing.EventNotificationOutgoingTopic;
import it.unibo.disi.wldt.mqttda.topic.outgoing.PropertyOutgoingTopic;
import it.unimore.dipi.iot.wldt.adapter.digital.DigitalAdapter;
import it.unimore.dipi.iot.wldt.core.event.WldtEvent;
import it.unimore.dipi.iot.wldt.core.event.WldtEventBus;
import it.unimore.dipi.iot.wldt.core.state.*;
import it.unimore.dipi.iot.wldt.exception.EventBusException;
import it.unimore.dipi.iot.wldt.exception.WldtDigitalTwinStateEventException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class MqttDigitalAdapter extends DigitalAdapter<MqttDigitalAdapterConfiguration> {
    
    private final static Logger logger = LoggerFactory.getLogger(MqttDigitalAdapter.class);
    
    private final MqttClient mqttClient;

    public MqttDigitalAdapter(String id, MqttDigitalAdapterConfiguration configuration) throws MqttException {
        super(id, configuration);
        mqttClient = new MqttClient(getConfiguration().getBrokerConnectionString(),
                getConfiguration().getClientId(),
                getConfiguration().getPersistence());
    }

    @Override
    protected void onStateChangePropertyCreated(DigitalTwinStateProperty<?> digitalTwinStateProperty) {

    }

    @Override
    protected void onStateChangePropertyUpdated(DigitalTwinStateProperty<?> digitalTwinStateProperty) {
        if(getConfiguration().getPropertyUpdateTopics().containsKey(digitalTwinStateProperty.getKey())){
            PropertyOutgoingTopic<?> outgoingTopic = getConfiguration().getPropertyUpdateTopics().get(digitalTwinStateProperty.getKey());
            publishOnDigitalTwinOutgoingTopic(outgoingTopic, outgoingTopic.applyPublishFunction(digitalTwinStateProperty));
        }
    }

    @Override
    protected void onStateChangePropertyDeleted(DigitalTwinStateProperty<?> digitalTwinStateProperty) {

    }

    @Override
    protected void onStatePropertyUpdated(DigitalTwinStateProperty<?> digitalTwinStateProperty) {
        logger.info("MQTT Digital Adapter({}) received property update", this.getId());
    }

    @Override
    protected void onStatePropertyDeleted(DigitalTwinStateProperty<?> digitalTwinStateProperty) {

    }

    @Override
    protected void onStateChangeActionEnabled(DigitalTwinStateAction digitalTwinStateAction) {

    }

    @Override
    protected void onStateChangeActionUpdated(DigitalTwinStateAction digitalTwinStateAction) {

    }

    @Override
    protected void onStateChangeActionDisabled(DigitalTwinStateAction digitalTwinStateAction) {

    }

    @Override
    protected void onStateChangeEventRegistered(DigitalTwinStateEvent digitalTwinStateEvent) {

    }

    @Override
    protected void onStateChangeEventRegistrationUpdated(DigitalTwinStateEvent digitalTwinStateEvent) {

    }

    @Override
    protected void onStateChangeEventUnregistered(DigitalTwinStateEvent digitalTwinStateEvent) {

    }

    @Override
    protected void onDigitalTwinStateEventNotificationReceived(DigitalTwinStateEventNotification<?> digitalTwinStateEventNotification) {
        logger.info("MQTT Digital Adapter({}) - received event: {}", this.getId(), digitalTwinStateEventNotification.getDigitalEventKey());
        if(this.getConfiguration().getEventNotificationTopics().containsKey(digitalTwinStateEventNotification.getDigitalEventKey())){
            EventNotificationOutgoingTopic<?> outgoingTopic = getConfiguration().getEventNotificationTopics().get(digitalTwinStateEventNotification.getDigitalEventKey());
            publishOnDigitalTwinOutgoingTopic(outgoingTopic, outgoingTopic.applyPublishFunction(digitalTwinStateEventNotification));
        }
    }

    @Override
    public void onAdapterStart() {
        connectToMqttBroker();
        getConfiguration().getActionIncomingTopics().values().forEach(this::subscribeClientToDigitalTwinIncomingTopic);
        notifyDigitalAdapterBound();
    }

    @Override
    public void onAdapterStop() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDigitalTwinSync(IDigitalTwinState digitalTwinState) {
        try {
//            digitalTwinState.getActionList().ifPresent(actions ->
//                actions.stream()
//                        .map(DigitalTwinStateAction::getKey)
//                        .filter(a -> getConfiguration().getActionIncomingTopics().containsKey(a))
//                        .map(a -> getConfiguration().getActionIncomingTopics().get(a))
//                        .forEach(this::subscribeClientToDigitalTwinIncomingTopic));

            digitalTwinState.getEventList()
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

    @Override
    public void onDigitalTwinUnSync(IDigitalTwinState digitalTwinState) {

    }

    @Override
    public void onDigitalTwinCreate() {

    }

    @Override
    public void onDigitalTwinStart() {

    }

    @Override
    public void onDigitalTwinStop() {

    }

    @Override
    public void onDigitalTwinDestroy() {

    }


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

    private void subscribeClientToDigitalTwinIncomingTopic(DigitalTwinIncomingTopic topic) {
        try {
            mqttClient.subscribe(topic.getTopic(), topic.getQos(), (t, msg) ->{
                logger.info("MQTT Digital Adapter -receive message on topic: {}", t);
                publishEvent(this.getId(), topic.applySubscribeFunction(new String(msg.getPayload())));
            });
            logger.info("MQTT Digital Adapter - MQTT client subscribed to topic: {}", topic.getTopic());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publishEvent(String publisher, WldtEvent<?> event){
        new Thread(() -> {
            try {
                WldtEventBus
                        .getInstance()
                        .publishEvent(publisher, event);
            } catch (EventBusException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void connectToMqttBroker(){
        try {
            mqttClient.connect(getConfiguration().getConnectOptions());
            logger.info("MQTT Digital Adapter - MQTT client connected to broker - clientId: {}", getConfiguration().getClientId());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
