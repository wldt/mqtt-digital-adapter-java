package it.wldt.adapter.mqtt.digital;

import it.wldt.adapter.mqtt.digital.exception.MqttDigitalAdapterConfigurationException;
import it.wldt.adapter.mqtt.digital.topic.incoming.ActionIncomingTopic;
import it.wldt.adapter.mqtt.digital.topic.outgoing.EventNotificationOutgoingTopic;
import it.wldt.adapter.mqtt.digital.topic.outgoing.PropertyOutgoingTopic;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;

import java.io.File;
import java.util.*;

/**
 * Configuration class for the MQTT Digital Adapter. It holds the necessary parameters for connecting to an MQTT broker
 * and provides options for customization. Instances of this class are created using the builder pattern.
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class MqttDigitalAdapterConfiguration {

    /**
     * The `brokerAddress` represents the address of the MQTT broker to which the MQTT Digital Adapter will connect.
     */
    private final String brokerAddress;

    /**
     * The `brokerPort` represents the port number on which the MQTT broker is listening for incoming connections.
     */
    private final Integer brokerPort;

    /**
     * The `username` is an optional field that holds the username for authenticating with the MQTT broker, if required.
     */
    private String username;

    /**
     * The `password` is an optional field that holds the password for authenticating with the MQTT broker, if required.
     */
    private String password;

    /**
     * The `token` is an optional field that holds the token for authenticating with the MQTT broker, if required.
     */
    private String accessToken;

    /**
     * The `clientId` is a unique identifier for the MQTT connection and is used to distinguish different clients.
     */
    private final String clientId;

    /**
     * The Base topic applied before every topic.
     */
    private String baseTopic;

    /**
     * The `cleanSessionFlag` is a boolean flag indicating whether a clean session should be used for the MQTT connection.
     * If set to true, the client and server discard any previous session state, starting with a clean slate.
     */
    private boolean cleanSessionFlag = false;

    /**
     * Flag indicating whether to use the mqtt 5.0 when connecting to MQTT broker.
     */
    private boolean mqttV5Flag = false;

    /**
     * The `connectionTimeout` represents the maximum time to wait for the MQTT connection to be established, in seconds.
     */
    private Integer connectionTimeout = 10;

    /**
     * The DT bound timeout (in seconds), connection lost time before going to un-bound state
     */
    private Integer boundTimeout = 10;

    /**
     * The `persistence` is an instance of `MqttClientPersistence` that determines how the client will persist state data
     * such as subscriptions and messages across client restarts.
     * Default is an in-memory persistence mechanism (`MemoryPersistence`).
     */
    private MqttClientPersistence persistence = new MemoryPersistence();

    /**
     * The persistence mechanism used by the MQTT 5.0 client.
     */
    private org.eclipse.paho.mqttv5.client.MqttClientPersistence persistenceV5 = new org.eclipse.paho.mqttv5.client.persist.MemoryPersistence();

    /**
     * The `automaticReconnectFlag` is a boolean flag indicating whether the MQTT client should attempt to
     * automatically reconnect to the broker in case of a connection failure.
     */
    private boolean automaticReconnectFlag = true;

    /**
     * The `propertyUpdateTopics` is a map that associates keys with instances of `PropertyOutgoingTopic`, representing
     * the topics where updates to Digital Twin properties should be published.
     */
    private final Map<String, PropertyOutgoingTopic<?>> propertyUpdateTopics = new HashMap<>();


    /**
     * The `eventNotificationTopics` is a map that associates keys with instances of `EventNotificationOutgoingTopic`,
     * representing the topics where event notifications from the Digital Twin should be published.
     */
    private final Map<String, EventNotificationOutgoingTopic<?>> eventNotificationTopics = new HashMap<>();

    /**
     * The `actionIncomingTopics` is a map that associates keys with instances of `ActionIncomingTopic`,
     * representing the topics where incoming actions for the Digital Twin should be subscribed to.
     */
    private final Map<String, ActionIncomingTopic<?>> actionIncomingTopics = new HashMap<>();


    /**
     * Constructs an instance of the MqttDigitalAdapterConfiguration with the specified broker details.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort    The port of the MQTT broker.
     * @param clientId      The client identifier for the MQTT connection.
     */
    protected MqttDigitalAdapterConfiguration(String brokerAddress, Integer brokerPort, String clientId) {
        this.brokerAddress = brokerAddress;
        this.brokerPort = brokerPort;
        this.clientId = clientId;
    }

    /**
     * Constructs an instance of the MqttDigitalAdapterConfiguration with the specified broker details.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort    The port of the MQTT broker.
     * @param clientId      The client identifier for the MQTT connection.
     * @param username      The client username for the MQTT connection.
     * @param password      The client password for the MQTT connection.
     */
    protected MqttDigitalAdapterConfiguration(String brokerAddress, Integer brokerPort, String clientId, String username, String password) {
        this.brokerAddress = brokerAddress;
        this.brokerPort = brokerPort;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
    }

    /**
     * Constructs an instance of the MqttDigitalAdapterConfiguration with the specified broker details.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort    The port of the MQTT broker.
     * @param clientId      The client identifier for the MQTT connection.
     * @param accessToken   The client access token for the MQTT connection.
     */
    public MqttDigitalAdapterConfiguration(String brokerAddress, Integer brokerPort, String clientId, String accessToken) {
        this.brokerAddress = brokerAddress;
        this.brokerPort = brokerPort;
        this.clientId = clientId;
        this.accessToken = accessToken;
    }

    /**
     * Constructs an instance of the MqttDigitalAdapterConfiguration with the specified broker details.
     * The client identifier is generated automatically.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort    The port of the MQTT broker.
     */
    protected MqttDigitalAdapterConfiguration(String brokerAddress, Integer brokerPort){
        this(brokerAddress, brokerPort, "wldt.mqtt.digital.adapter.client."+new Random(System.currentTimeMillis()).nextInt());
    }

    /**
     * Creates a builder for constructing instances of MqttDigitalAdapterConfiguration with specific parameters.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort    The port of the MQTT broker.
     * @param clientId      The client identifier for the MQTT connection.
     * @return A builder instance for MqttDigitalAdapterConfiguration.
     * @throws MqttDigitalAdapterConfigurationException If there is an issue with the configuration.
     */
    public static MqttDigitalAdapterConfigurationBuilder builder(String brokerAddress, Integer brokerPort, String clientId) throws MqttDigitalAdapterConfigurationException {
        return new MqttDigitalAdapterConfigurationBuilder(brokerAddress, brokerPort, clientId);
    }

    /**
     * Creates a builder for constructing instances of MqttDigitalAdapterConfiguration with specific parameters.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort    The port of the MQTT broker.
     * @param clientId      The client identifier for the MQTT connection.
     * @param username      The client username for the MQTT connection.
     * @param password      The client password for the MQTT connection.
     * @return A builder instance for MqttDigitalAdapterConfiguration.
     * @throws MqttDigitalAdapterConfigurationException If there is an issue with the configuration.
     */
    public static MqttDigitalAdapterConfigurationBuilder builder(String brokerAddress, Integer brokerPort, String clientId, String username, String password) throws MqttDigitalAdapterConfigurationException {
        return new MqttDigitalAdapterConfigurationBuilder(brokerAddress, brokerPort, clientId, username, password);
    }

    /**
     * Creates a builder for constructing instances of MqttDigitalAdapterConfiguration with specific parameters.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort    The port of the MQTT broker.
     * @param clientId      The client identifier for the MQTT connection.
     * @param accessToken   The client access token for the MQTT connection.
     * @return A builder instance for MqttDigitalAdapterConfiguration.
     * @throws MqttDigitalAdapterConfigurationException If there is an issue with the configuration.
     */
    public static MqttDigitalAdapterConfigurationBuilder builder(String brokerAddress, Integer brokerPort, String clientId, String accessToken) throws MqttDigitalAdapterConfigurationException {
        return new MqttDigitalAdapterConfigurationBuilder(brokerAddress, brokerPort, clientId, accessToken);
    }

    /**
     * Creates a builder for constructing instances of MqttDigitalAdapterConfiguration with specific parameters.
     * The client identifier is generated automatically.
     *
     * @param brokerAddress The address of the MQTT broker.
     * @param brokerPort    The port of the MQTT broker.
     * @return A builder instance for MqttDigitalAdapterConfiguration.
     * @throws MqttDigitalAdapterConfigurationException If there is an issue with the configuration.
     */
    public static MqttDigitalAdapterConfigurationBuilder builder(String brokerAddress, Integer brokerPort) throws MqttDigitalAdapterConfigurationException {
        return new MqttDigitalAdapterConfigurationBuilder(brokerAddress, brokerPort);
    }

    /**
     * Creates a new MqttDigitalAdapterConfigurationBuilder for building MqttDigitalAdapterConfiguration instances
     * with the json File from where read all the config.
     *
     * @param jsonFile The json File from where read all the config.
     * @return The MqttDigitalAdapterConfigurationBuilder instance.
     * @throws MqttDigitalAdapterConfigurationException If there is an issue creating the builder.
     * @see MqttDigitalAdapterConfigurationBuilder
     */
    public static MqttDigitalAdapterConfigurationBuilder builder(File jsonFile) throws MqttDigitalAdapterConfigurationException {
        return new MqttDigitalAdapterConfigurationBuilder(jsonFile);
    }

    /**
     * Gets the address of the MQTT broker.
     *
     * @return The broker address.
     */
    public String getBrokerAddress() {
        return brokerAddress;
    }

    /**
     * Gets the port of the MQTT broker.
     *
     * @return The broker port.
     */
    public Integer getBrokerPort() {
        return brokerPort;
    }

    /**
     * Gets the username for authenticating with the MQTT broker.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password for authenticating with the MQTT broker.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the access token for authenticating with the MQTT broker.
     *
     * @return The access token.
     */
    public String getAccessToken() { return accessToken; }

    /**
     * Gets the client identifier for the MQTT connection.
     *
     * @return The client identifier.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Gets the connection string for the MQTT broker.
     *
     * @return The broker connection string.
     */
    public String getBrokerConnectionString(){
        return String.format("tcp://%s:%d", brokerAddress, brokerPort);
    }

    /**
     * Gets the MQTT 5.0 Flag.
     *
     * @return The MQTT 5.0 Flag.
     */
    public boolean isMqttV5Flag() {
        return mqttV5Flag;
    }

    /**
     * Gets the MQTT client persistence.
     *
     * @return The MQTT client persistence.
     */
    public MqttClientPersistence getPersistence() {
        return persistence;
    }

    /**
     * Gets the persistence mechanism used by the MQTT 5.0 client.
     *
     * @return The MQTT 5.0 client persistence mechanism.
     */
    public org.eclipse.paho.mqttv5.client.MqttClientPersistence getPersistenceV5() {
        return persistenceV5;
    }

    /**
     * Gets the base topic.
     *
     * @return The Base topic.
     */
    public String getBaseTopic() {
        return baseTopic;
    }

    /**
     * Sets the DT bound timeout
     *
     * @return The time before DT state goes to un-bound
     */
    public Integer getBoundTimeout() {
        return boundTimeout;
    }

    /**
     * Gets the MQTT connection options.
     *
     * @return The MQTT connection options.
     */
    public MqttConnectOptions getConnectOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(automaticReconnectFlag);
        options.setCleanSession(cleanSessionFlag);
        options.setConnectionTimeout(connectionTimeout);
        if(username != null && !username.isEmpty() && password != null && !password.isEmpty()){
            options.setUserName(username);
            options.setPassword(password.toCharArray());
        } else if (accessToken != null && !accessToken.isEmpty()) {
            options.setUserName(accessToken);
        }
        return options;
    }

    /**
     * Gets the connection options for connecting to the MQTT broker.
     *
     * @return The MQTT connection options.
     */
    public MqttConnectionOptions getConnectOptionsV5(){
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setAutomaticReconnect(automaticReconnectFlag);
        options.setCleanStart(cleanSessionFlag);
        options.setConnectionTimeout(connectionTimeout);
        if(username != null && !username.isEmpty() && password != null && !password.isEmpty()){
            options.setUserName(username);
            options.setPassword(password.getBytes());
        } else if (accessToken != null && !accessToken.isEmpty()) {
            options.setUserName(accessToken);
        }
        return options;
    }

    /**
     * Sets the connection timeout for the MQTT connection.
     *
     * @param connectionTimeout The connection timeout value.
     */
    protected void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Sets the clean session flag for the MQTT connection.
     *
     * @param cleanSession The clean session flag.
     */
    protected void setCleanSessionFlag(boolean cleanSession) {
        this.cleanSessionFlag = cleanSession;
    }

    /**
     * Sets the mqtt version flag.
     *
     * @param mqttV5Flag The mqtt version flag.
     */
    protected void setMqttV5Flag(boolean mqttV5Flag) {
        this.mqttV5Flag = mqttV5Flag;
    }

    /**
     * Sets the automatic reconnect flag for the MQTT connection.
     *
     * @param automaticReconnect The automatic reconnect flag.
     */
    protected void setAutomaticReconnectFlag(boolean automaticReconnect){
        this.automaticReconnectFlag = automaticReconnect;
    }

    /**
     * Sets the MQTT client persistence for the MQTT connection.
     *
     * @param persistence The MQTT client persistence.
     */
    protected void setMqttClientPersistence(MqttClientPersistence persistence) {
        this.persistence = persistence;
    }

    /**
     * Gets the map of property update topics associated with their keys.
     *
     * @return The map of property update topics.
     */
    public Map<String, PropertyOutgoingTopic<?>> getPropertyUpdateTopics() {
        return propertyUpdateTopics;
    }

    /**
     * Gets the map of event notification topics associated with their keys.
     *
     * @return The map of event notification topics.
     */
    public Map<String, EventNotificationOutgoingTopic<?>> getEventNotificationTopics() {
        return eventNotificationTopics;
    }

    /**
     * Gets the map of action incoming topics associated with their keys.
     *
     * @return The map of action incoming topics.
     */
    public Map<String, ActionIncomingTopic<?>> getActionIncomingTopics() {
        return actionIncomingTopics;
    }

    /**
     * Sets the Base topic.
     *
     * @param baseTopic The MQTT client persistence mechanism.
     */
    protected void setBaseTopic(String baseTopic) {
        this.baseTopic = baseTopic;
    }

    /**
     * Sets the DT bound timeout
     *
     * @param boundTimeout The time before DT state goes to un-bound
     */
    public void setBoundTimeout(Integer boundTimeout) {
        this.boundTimeout = boundTimeout;
    }
}
