package it.wldt.adapter.mqtt.digital.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/** Serializable class that is used to cast info from a JSON configuration File */
public class MqttDigitalAdapterFileConfiguration implements Serializable {

    /** The broker address of the MQTT Broker */
    @JsonProperty("broker")
    private String mqttBroker;

    /** The broker port of the MQTT Broker */
    @JsonProperty("port")
    private Integer mqttPort;

    /** The username used for connecting to  the MQTT Broker */
    @JsonProperty("username")
    private String mqttUsername;

    /** The password used for connecting to  the MQTT Broker */
    @JsonProperty("password")
    private String mqttPassword;

    /** The access token used for connecting to  the MQTT Broker */
    @JsonProperty("access_token")
    private String accessToken;

    /** The client_id used for connecting to  the MQTT Broker */
    @JsonProperty("client_id")
    private String mqttClientId;

    /** The base topic used before every topic */
    @JsonProperty("base_topic")
    private String mqttBaseTopic;

    /** A list of Map that can contains Properties, Events and Actions to implement inside the PAD */
    @JsonProperty("topic_list")
    private List<HashMap<String, Object>> mqttTopicList;

    /** Empty class Constructor */
    public MqttDigitalAdapterFileConfiguration() {
    }

    /**
     * @return the MQTT broker address
     */
    public String getMqttBroker() {
        return mqttBroker;
    }

    /**
     * @return the MQTT broker port
     */
    public Integer getMqttPort() {
        return mqttPort;
    }

    /**
     * @return the username used to connect to MQTT broker
     */
    public String getMqttUsername() {
        return mqttUsername;
    }

    /**
     * @return the password used to connect to MQTT broker
     */
    public String getMqttPassword() {
        return mqttPassword;
    }

    /**
     * @return the access token used to connect to MQTT broker
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @return the client_id used to connect to MQTT broker
     */
    public String getMqttClientId() {
        return mqttClientId;
    }

    /**
     * @return the base topic used before each topic
     */
    public String getMqttBaseTopic() {
        return mqttBaseTopic;
    }

    /**
     * @return the List of Properties, Events and Actions
     */
    public List<HashMap<String, Object>> getMqttTopicList() {
        return mqttTopicList;
    }

    /**
     * Set the MQTT broker address
     * @param mqttBroker the MQTT broker Address
     */
    public void setMqttBroker(String mqttBroker) {
        this.mqttBroker = mqttBroker;
    }

    /**
     * Set the MQTT broker port
     * @param mqttPort the MQTT broker port
     */
    public void setMqttPort(Integer mqttPort) {
        this.mqttPort = mqttPort;
    }

    /**
     * Set the username
     * @param mqttUsername the username
     */
    public void setMqttUsername(String mqttUsername) {
        this.mqttUsername = mqttUsername;
    }

    /**
     * Set the password
     * @param mqttPassword the password
     */
    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }

    /**
     * Set the access token
     * @param accessToken the access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Set the client_id
     * @param mqttClientId the client_id
     */
    public void setMqttClientId(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }

    /**
     * Set the base topic
     * @param mqttBaseTopic the base topic
     */
    public void setMqttBaseTopic(String mqttBaseTopic) {
        this.mqttBaseTopic = mqttBaseTopic;
    }

    /**
     * Set the Topic List
     * @param mqttTopicList the Topic List
     */
    public void setMqttTopicList(List<HashMap<String, Object>> mqttTopicList) {
        this.mqttTopicList = mqttTopicList;
    }
}
