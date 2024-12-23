package it.wldt.adapter.mqtt.digital;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.wldt.adapter.mqtt.digital.exception.MqttDigitalAdapterConfigurationException;
import it.wldt.adapter.mqtt.digital.model.MqttDigitalAdapterFileConfiguration;
import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.adapter.mqtt.digital.topic.incoming.ActionIncomingTopic;
import it.wldt.adapter.mqtt.digital.topic.outgoing.EventNotificationOutgoingTopic;
import it.wldt.adapter.mqtt.digital.topic.outgoing.PropertyOutgoingTopic;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;

import java.io.File;
import java.util.HashMap;
import java.util.Random;
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
        configuration = new MqttDigitalAdapterConfiguration(brokerAddress, brokerPort, clientId);
    }

    /**
     * Constructs a new instance of the `MqttDigitalAdapterConfigurationBuilder` with the specified broker address,
     * broker port, client ID, Username and Password It initializes the configuration with default values.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort The port number on which the MQTT broker is listening.
     * @param clientId The client ID for the MQTT connection.
     * @param username The username for the MQTT connection.
     * @param password The password for the MQTT connection.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the broker address or client ID is empty or null,
     *                                                    or the broker port is not a positive number.
     */
    public MqttDigitalAdapterConfigurationBuilder(String brokerAddress, Integer brokerPort, String clientId, String username, String password) throws MqttDigitalAdapterConfigurationException {
        if(!isValid(brokerAddress) || isValid(brokerPort) || !isValid(clientId) || !isValid(username) || !isValid(password))
            throw new MqttDigitalAdapterConfigurationException("Broker Address, Client Id, Username or Password cannot be empty string or null and Broker Port must be a positive number");
        configuration = new MqttDigitalAdapterConfiguration(brokerAddress, brokerPort, clientId, username, password);
    }

    /**
     * Constructs a new instance of the `MqttDigitalAdapterConfigurationBuilder` with the specified broker address,
     * broker port, client ID and Access Token It initializes the configuration with default values.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort The port number on which the MQTT broker is listening.
     * @param clientId The client ID for the MQTT connection.
     * @param accessToken The access token for the MQTT connection.
     * @throws MqttDigitalAdapterConfigurationException Thrown when the broker address or client ID is empty or null,
     *                                                    or the broker port is not a positive number.
     */
    public MqttDigitalAdapterConfigurationBuilder(String brokerAddress, Integer brokerPort, String clientId, String accessToken) throws MqttDigitalAdapterConfigurationException {
        if(!isValid(brokerAddress) || isValid(brokerPort) || !isValid(clientId) || !isValid(accessToken))
            throw new MqttDigitalAdapterConfigurationException("Broker Address, Client Id or Access Token cannot be empty string or null and Broker Port must be a positive number");
        configuration = new MqttDigitalAdapterConfiguration(brokerAddress, brokerPort, clientId, accessToken);
    }

    /**
     * Constructs a builder with the required parameters for creating MqttDigitalAdapterConfiguration.
     *
     * @param jsonFile      The json file with all the configuration parameters.
     * @throws MqttDigitalAdapterConfigurationException If the provided parameters are invalid.
     */
    public MqttDigitalAdapterConfigurationBuilder(File jsonFile) throws MqttDigitalAdapterConfigurationException {
        if(!isValid(jsonFile))
            throw new MqttDigitalAdapterConfigurationException("Configuration file must exists, must be a file and it must be read.");
        MqttDigitalAdapterFileConfiguration fileConfig = getMqttFileConfiguration(jsonFile);

        if(!isValid(fileConfig.getMqttClientId())) { fileConfig.setMqttClientId("wldt.mqtt.client."+new Random(System.currentTimeMillis()).nextInt()); }
        if(fileConfig.getAccessToken() != null && !fileConfig.getAccessToken().isEmpty()) {
            configuration = new MqttDigitalAdapterConfiguration(fileConfig.getMqttBroker(), fileConfig.getMqttPort(), fileConfig.getMqttClientId(), fileConfig.getAccessToken());
        } else {
            configuration = new MqttDigitalAdapterConfiguration(fileConfig.getMqttBroker(), fileConfig.getMqttPort(), fileConfig.getMqttClientId(), fileConfig.getMqttUsername(), fileConfig.getMqttPassword());
        }
        configuration.setBaseTopic(fileConfig.getMqttBaseTopic());
        try {
            for (HashMap<String, Object> topicMap : fileConfig.getMqttTopicList()) {

                MqttQosLevel qosLevel = null;
                if((int) topicMap.get("qos") == 0) {
                    qosLevel = MqttQosLevel.MQTT_QOS_0;
                } else if ((int) topicMap.get("qos") == 1) {
                    qosLevel = MqttQosLevel.MQTT_QOS_1;
                } else if ((int) topicMap.get("qos") == 2) {
                    qosLevel = MqttQosLevel.MQTT_QOS_2;
                }

                if(topicMap.get("type").equals("property")) {
                    if(topicMap.get("function_type").equals("number")) {
                        if(topicMap.get("initial_value") instanceof Integer) {
                            addPropertyTopic((String) topicMap.get("property_key"), (String) topicMap.get("topic"), qosLevel, value -> Integer.toString((int) value));
                        } else if (topicMap.get("initial_value") instanceof Double) {
                            addPropertyTopic((String) topicMap.get("property_key"), (String) topicMap.get("topic"), qosLevel, value -> Double.toString((double) value));
                        }
                    } else if(topicMap.get("function_type").equals("string")) {
                        addPropertyTopic((String) topicMap.get("property_key"), (String) topicMap.get("topic"), qosLevel, String::valueOf);
                    } else if(topicMap.get("function_type").equals("boolean")) {
                        addPropertyTopic((String) topicMap.get("property_key"), (String) topicMap.get("topic"), qosLevel, value -> Boolean.toString((boolean) value));
                    } else if(topicMap.get("function_type").equals("bytes")) {
                        addPropertyTopic((String) topicMap.get("property_key"), (String) topicMap.get("topic"), qosLevel, MqttDigitalAdapterConfigurationBuilder::fromStringToBytes);
                    } else if(topicMap.get("function_type").equals("json")) {
                        addPropertyTopic((String) topicMap.get("property_key"), (String) topicMap.get("topic"), qosLevel, ObjectNode::toString);
                    } else {
                        throw new MqttDigitalAdapterConfigurationException("Wrong function type passed in file configuration. Property function can be number, string, boolean, bytes or json");
                    }
                } else if(topicMap.get("type").equals("event")) {
                    if(topicMap.get("function_type").equals("number")) {
                        if(topicMap.get("initial_value") instanceof Integer) {
                            addEventNotificationTopic((String) topicMap.get("event_key"), (String) topicMap.get("topic"), qosLevel, value -> Integer.toString((int) value));
                        } else if (topicMap.get("initial_value") instanceof Double) {
                            addEventNotificationTopic((String) topicMap.get("event_key"), (String) topicMap.get("topic"), qosLevel, value -> Double.toString((double) value));
                        }
                    } else if(topicMap.get("function_type").equals("string")) {
                        addEventNotificationTopic((String) topicMap.get("event_key"), (String) topicMap.get("topic"), qosLevel, String::valueOf);
                    } else if(topicMap.get("function_type").equals("boolean")) {
                        addEventNotificationTopic((String) topicMap.get("event_key"), (String) topicMap.get("topic"), qosLevel, value -> Boolean.toString((boolean) value));
                    } else if(topicMap.get("function_type").equals("bytes")) {
                        addEventNotificationTopic((String) topicMap.get("event_key"), (String) topicMap.get("topic"), qosLevel, MqttDigitalAdapterConfigurationBuilder::fromStringToBytes);
                    } else if(topicMap.get("function_type").equals("json")) {
                        addEventNotificationTopic((String) topicMap.get("event_key"), (String) topicMap.get("topic"), qosLevel, ObjectNode::toString);
                    } else {
                        throw new MqttDigitalAdapterConfigurationException("Wrong function type passed in file configuration. Event function can be number, string, boolean, bytes or json");
                    }
                } else if(topicMap.get("type").equals("action")) {
                    if(topicMap.get("function_type").equals("number")) {
                        if(topicMap.get("initial_value") instanceof Integer) {
                            addActionTopic((String) topicMap.get("action_key"), (String) topicMap.get("topic"), Integer::parseInt);
                        } else if (topicMap.get("initial_value") instanceof Double) {
                            addActionTopic((String) topicMap.get("action_key"), (String) topicMap.get("topic"), Double::parseDouble);
                        }
                    } else if(topicMap.get("function_type").equals("string")) {
                        addActionTopic((String) topicMap.get("action_key"), (String) topicMap.get("topic"), String::valueOf);
                    } else if(topicMap.get("function_type").equals("boolean")) {
                        addActionTopic((String) topicMap.get("action_key"), (String) topicMap.get("topic"), Boolean::parseBoolean);
                    } else if(topicMap.get("function_type").equals("bytes")) {
                        addActionTopic((String) topicMap.get("action_key"), (String) topicMap.get("topic"), MqttDigitalAdapterConfigurationBuilder::parseBytesFromString);
                    } else if(topicMap.get("function_type").equals("json")) {
                        ObjectMapper mapper = new ObjectMapper();
                        addActionTopic((String) topicMap.get("action_key"), (String) topicMap.get("topic"), value -> {
                            try {
                                return mapper.readTree(value);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {
                        throw new MqttDigitalAdapterConfigurationException("Wrong function type passed in file configuration. Action function can be number, string, boolean, bytes or json");
                    }
                }
            }
        } catch (Exception e) {
            throw new MqttDigitalAdapterConfigurationException("Error occurred during topic list read in configuration file.");

        }
    }

    private static byte[] parseBytesFromString(String intArrayString) {

        if (intArrayString.trim().equals("[]")) {
            return new byte[0]; // Return an empty byte array
        }

        try {
            // Remove square brackets and split the string
            String cleanedString = intArrayString.replaceAll("[\\[\\]]", ""); // Remove square brackets
            String[] intStrings = cleanedString.split(","); // Split by comma

            // Convert to byte array
            byte[] byteArray = new byte[intStrings.length];
            for (int i = 0; i < intStrings.length; i++) {
                byteArray[i] = (byte) Integer.parseInt(intStrings[i].trim());
            }

            return byteArray;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input string contains invalid integers.", e);
        }
    }

    private static String fromStringToBytes(byte[] byteArray) {
        if (byteArray.length == 0) {
            return "[]"; // Return an empty array representation
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < byteArray.length; i++) {
            sb.append(byteArray[i] & 0xFF); // Ensure the byte is treated as unsigned
            if (i < byteArray.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private MqttDigitalAdapterFileConfiguration getMqttFileConfiguration(File jsonFile) throws MqttDigitalAdapterConfigurationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonFile, MqttDigitalAdapterFileConfiguration.class);
        } catch (Exception e) {
            throw new MqttDigitalAdapterConfigurationException("Error occurred when reading mqtt physical adapter configuration file.");
        }
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

    /**
     * Checks if the given file parameter is valid (exists, is a file, can be read).
     *
     * @param param The file parameter to be checked.
     * @return true if the file exists, is a file and can be read.
     */
    private boolean isValid(File param){
        return param.exists() && param.isFile() && param.canRead();
    }
}
