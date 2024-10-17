package it.wldt.adapter.mqtt.digital;

import it.wldt.adapter.mqtt.digital.exception.MqttDigitalAdapterConfigurationException;
import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.adapter.mqtt.digital.topic.incoming.ActionIncomingTopic;
import it.wldt.adapter.mqtt.digital.topic.outgoing.EventNotificationOutgoingTopic;
import it.wldt.adapter.mqtt.digital.topic.outgoing.PropertyOutgoingTopic;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;

import java.util.function.Function;

/**
 * The `MqttDigitalAdapterConfigurationBuilder` class provides a fluent builder pattern for creating instances of
 * `MqttDigitalAdapterConfiguration`. It facilitates the configuration of an MQTT Digital Adapter by allowing the
 * addition of property, event notification, and action topics, as well as setting various configuration parameters.
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class MqttDigitalAdapterConfigurationBuilder {

    /**
     * The `configuration` holds the partially built configuration that is being constructed by the builder.
     */
    private final MqttDigitalAdapterConfiguration configuration;

    /**
     * Constructs a new instance of the `MqttDigitalAdapterConfigurationBuilder` with the specified broker address
     * and broker port. It initializes the configuration with default values.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort The port number on which the MQTT broker is listening.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the broker address is empty or null, or the broker
     *                                                    port is not a positive number.
     */
    public MqttDigitalAdapterConfigurationBuilder(String brokerAddress, Integer brokerPort) throws MqttDigitalAdapterConfigurationException {
        if(!isValid(brokerAddress) || isValid(brokerPort))
            throw new MqttDigitalAdapterConfigurationException("Broker Address cannot be empty string or null and Broker Port must be a positive number");
        configuration = new MqttDigitalAdapterConfiguration(brokerAddress, brokerPort);
    }

    /**
     * Constructs a new instance of the `MqttDigitalAdapterConfigurationBuilder` with the specified broker address,
     * broker port, and client ID. It initializes the configuration with default values.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort The port number on which the MQTT broker is listening.
     * @param clientId The client ID for the MQTT connection.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the broker address or client ID is empty or null,
     *                                                    or the broker port is not a positive number.
     */
    public MqttDigitalAdapterConfigurationBuilder(String brokerAddress, Integer brokerPort, String clientId) throws MqttDigitalAdapterConfigurationException {
        if(!isValid(brokerAddress) || isValid(brokerPort) || !isValid(clientId))
            throw new MqttDigitalAdapterConfigurationException("Broker Address and Client Id cannot be empty string or null and Broker Port must be a positive number");
        configuration = new MqttDigitalAdapterConfiguration(brokerAddress, brokerPort);
    }

    /**
     * Adds a property update topic to the MQTT Digital Adapter configuration. The property is associated with a specified
     * key, MQTT topic, Quality of Service (QoS) level, and a function to convert the property value to its MQTT payload.
     *
     * @param <T> The type of the property value.
     * @param propertyKey The key associated with the property.
     * @param topic The MQTT topic for property updates.
     * @param qosLevel The Quality of Service (QoS) level for the MQTT topic.
     * @param propertyToPayloadFunction The function to convert the property value to its MQTT payload.
     * @return The updated `MqttDigitalAdapterConfigurationBuilder`.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the key, topic, or payload function is invalid.
     */
    public <T> MqttDigitalAdapterConfigurationBuilder addPropertyTopic(String propertyKey,
                                                                       String topic,
                                                                       MqttQosLevel qosLevel,
                                                                       Function<T, String> propertyToPayloadFunction) throws MqttDigitalAdapterConfigurationException {
        checkTopic(propertyKey, topic, propertyToPayloadFunction);
        this.configuration.getPropertyUpdateTopics().put(propertyKey, new PropertyOutgoingTopic<>(topic, qosLevel, propertyToPayloadFunction));
        return this;
    }

    /**
     * Adds a property update topic to the MQTT Digital Adapter configuration. The property is associated with a specified
     * key, MQTT topic, Quality of Service (QoS) level, and a function to convert the property value to its MQTT payload.
     *
     * @param <T> The type of the property value.
     * @param propertyKey The key associated with the property.
     * @param topic The MQTT topic for property updates.
     * @param qosLevel The Quality of Service (QoS) level for the MQTT topic.
     * @param isRetained The retained flag.
     * @param propertyToPayloadFunction The function to convert the property value to its MQTT payload.
     * @return The updated `MqttDigitalAdapterConfigurationBuilder`.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the key, topic, or payload function is invalid.
     */
    public <T> MqttDigitalAdapterConfigurationBuilder addPropertyTopic(String propertyKey,
                                                                       String topic,
                                                                       MqttQosLevel qosLevel,
                                                                       boolean isRetained,
                                                                       Function<T, String> propertyToPayloadFunction) throws MqttDigitalAdapterConfigurationException {
        checkTopic(propertyKey, topic, propertyToPayloadFunction);
        this.configuration.getPropertyUpdateTopics().put(propertyKey, new PropertyOutgoingTopic<>(topic, qosLevel, isRetained, propertyToPayloadFunction));
        return this;
    }

    /**
     * Adds an event notification topic to the MQTT Digital Adapter configuration. The event is associated with a specified
     * key, MQTT topic, Quality of Service (QoS) level, and a function to convert the event to its MQTT payload.
     *
     * @param <T> The type of the event value.
     * @param eventKey The key associated with the event.
     * @param topic The MQTT topic for event notifications.
     * @param qosLevel The Quality of Service (QoS) level for the MQTT topic.
     * @param eventToPayloadFunction The function to convert the event value to its MQTT payload.
     * @return The updated `MqttDigitalAdapterConfigurationBuilder`.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the key, topic, or payload function is invalid.
     */
    public <T> MqttDigitalAdapterConfigurationBuilder addEventNotificationTopic(String eventKey,
                                                                                String topic,
                                                                                MqttQosLevel qosLevel,
                                                                                Function<T, String> eventToPayloadFunction) throws  MqttDigitalAdapterConfigurationException{
        checkTopic(eventKey, topic, eventToPayloadFunction);
        this.configuration.getEventNotificationTopics().put(eventKey, new EventNotificationOutgoingTopic<>(topic, qosLevel, eventToPayloadFunction));
        return this;
    }

    /**
     * Adds an event notification topic to the MQTT Digital Adapter configuration. The event is associated with a specified
     * key, MQTT topic, Quality of Service (QoS) level, and a function to convert the event to its MQTT payload.
     *
     * @param <T> The type of the event value.
     * @param eventKey The key associated with the event.
     * @param topic The MQTT topic for event notifications.
     * @param qosLevel The Quality of Service (QoS) level for the MQTT topic.
     * @param isRetained The retained flag.
     * @param eventToPayloadFunction The function to convert the event value to its MQTT payload.
     * @return The updated `MqttDigitalAdapterConfigurationBuilder`.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the key, topic, or payload function is invalid.
     */
    public <T> MqttDigitalAdapterConfigurationBuilder addEventNotificationTopic(String eventKey,
                                                                                String topic,
                                                                                MqttQosLevel qosLevel,
                                                                                boolean isRetained,
                                                                                Function<T, String> eventToPayloadFunction) throws  MqttDigitalAdapterConfigurationException{
        checkTopic(eventKey, topic, eventToPayloadFunction);
        this.configuration.getEventNotificationTopics().put(eventKey, new EventNotificationOutgoingTopic<>(topic, qosLevel, isRetained, eventToPayloadFunction));
        return this;
    }

    /**
     * Adds an action topic to the MQTT Digital Adapter configuration. The action is associated with a specified key, MQTT
     * topic, and a function to convert the MQTT payload to an action value.
     *
     * @param <T> The type of the action value.
     * @param actionKey The key associated with the action.
     * @param topic The MQTT topic for incoming actions.
     * @param payloadToActionFunction The function to convert the MQTT payload to an action value.
     * @return The updated `MqttDigitalAdapterConfigurationBuilder`.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the key, topic, or payload function is invalid.
     */
    public <T> MqttDigitalAdapterConfigurationBuilder addActionTopic(String actionKey,
                                                                     String topic,
                                                                     Function<String, T> payloadToActionFunction) throws MqttDigitalAdapterConfigurationException {
        checkTopic(actionKey, topic, payloadToActionFunction);
        this.configuration.getActionIncomingTopics().put(actionKey, new ActionIncomingTopic<>(topic, actionKey, payloadToActionFunction));
        return this;
    }

    /**
     * Sets the connection timeout in seconds for the MQTT client in the MQTT Digital Adapter configuration.
     *
     * @param connectionTimeout The connection timeout in seconds. Must be a positive number.
     * @return The updated `MqttDigitalAdapterConfigurationBuilder`.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the connection timeout is not a positive number.
     */
    public MqttDigitalAdapterConfigurationBuilder setConnectionTimeout(Integer connectionTimeout) throws MqttDigitalAdapterConfigurationException {
        if(isValid(connectionTimeout)) throw new MqttDigitalAdapterConfigurationException("Connection Timeout must be a positive number");
        this.configuration.setConnectionTimeout(connectionTimeout);
        return this;
    }

    /**
     * Sets the clean session flag in the MQTT Digital Adapter configuration. If set to `true`, the MQTT client starts
     * with a clean session, meaning any previous session state is discarded.
     *
     * @param cleanSession The clean session flag.
     * @return The updated `MqttDigitalAdapterConfigurationBuilder`.
     */
    public MqttDigitalAdapterConfigurationBuilder setCleanSessionFlag(boolean cleanSession) {
        this.configuration.setCleanSessionFlag(cleanSession);
        return this;
    }

    /**
     * Sets the automatic reconnect flag in the MQTT Digital Adapter configuration. If set to `true`, the MQTT client
     * automatically attempts to reconnect to the broker in case of a connection loss.
     *
     * @param automaticReconnect The automatic reconnect flag.
     * @return The updated `MqttDigitalAdapterConfigurationBuilder`.
     */
    public MqttDigitalAdapterConfigurationBuilder setAutomaticReconnectFlag(boolean automaticReconnect){
        this.configuration.setAutomaticReconnectFlag(automaticReconnect);
        return this;
    }

    /**
     * Sets the MQTT client persistence in the MQTT Digital Adapter configuration.
     *
     * @param persistence The MQTT client persistence implementation.
     * @return The updated `MqttDigitalAdapterConfigurationBuilder`.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the persistence is null.
     */
    public MqttDigitalAdapterConfigurationBuilder setMqttClientPersistence(MqttClientPersistence persistence) throws MqttDigitalAdapterConfigurationException {
        if(persistence == null) throw new MqttDigitalAdapterConfigurationException("MqttClientPersistence cannot be null");
        this.configuration.setMqttClientPersistence(persistence);
        return this;
    }

    /**
     * Builds and returns an instance of `MqttDigitalAdapterConfiguration`. Before building, it checks whether the
     * configuration contains any MqttTopics (action topics, event notification topics, or property update topics). If no
     * topics are present, it throws an exception.
     *
     * @return An instance of `MqttDigitalAdapterConfiguration`.
     * @throws MqttDigitalAdapterConfigurationException Thrown when no MqttTopics are present in the configuration.
     */
    public MqttDigitalAdapterConfiguration build() throws MqttDigitalAdapterConfigurationException {
        if(this.configuration.getActionIncomingTopics().isEmpty()
                && this.configuration.getEventNotificationTopics().isEmpty()
                && this.configuration.getPropertyUpdateTopics().isEmpty())
            throw new MqttDigitalAdapterConfigurationException("Cannot build a MqttDigitalAdapterConfiguration without MqttTopics");

        return this.configuration;
    }


    /**
     * Checks if a key, topic, and function combination is valid. Throws an exception if the key or topic is empty or null,
     * or if the function is null.
     *
     * @param key The key to check.
     * @param topic The topic to check.
     * @param function The function to check.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the key or topic is empty or null, or when the function is null.
     */
    private <I, O> void checkTopic(String key, String topic, Function<I, O> function) throws MqttDigitalAdapterConfigurationException {
        if(!isValid(key) || !isValid(topic) || function == null)
            throw new MqttDigitalAdapterConfigurationException("Key and Topic cannot be empty or null and function cannot be null");
    }

    /**
     * Checks if a parameter is a valid non-empty string.
     *
     * @param param The parameter to check.
     * @return `true` if the parameter is a valid non-empty string, otherwise `false`.
     */
    private boolean isValid(String param){
        return param != null && !param.isEmpty();
    }

    /**
     * Checks if an integer parameter is a valid positive number.
     *
     * @param param The integer parameter to check.
     * @return `true` if the parameter is a valid positive number, otherwise `false`.
     */
    private boolean isValid(int param){
        return param <= 0;
    }
}
