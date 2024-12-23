package it.wldt.adapter.mqtt.digital.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class MqttDigitalAdapterFileConfiguration implements Serializable {
    @JsonProperty("broker")
    private String mqttBroker;

    @JsonProperty("port")
    private Integer mqttPort;

    @JsonProperty("username")
    private String mqttUsername;

    @JsonProperty("password")
    private String mqttPassword;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("client_id")
    private String mqttClientId;

    @JsonProperty("base_topic")
    private String mqttBaseTopic;

    @JsonProperty("topic_list")
    private List<HashMap<String, Object>> mqttTopicList;

    public MqttDigitalAdapterFileConfiguration() {
    }

    public String getMqttBroker() {
        return mqttBroker;
    }
    public Integer getMqttPort() {
        return mqttPort;
    }
    public String getMqttUsername() {
        return mqttUsername;
    }
    public String getMqttPassword() {
        return mqttPassword;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public String getMqttClientId() {
        return mqttClientId;
    }
    public String getMqttBaseTopic() {
        return mqttBaseTopic;
    }
    public List<HashMap<String, Object>> getMqttTopicList() {
        return mqttTopicList;
    }

    public void setMqttBroker(String mqttBroker) {
        this.mqttBroker = mqttBroker;
    }
    public void setMqttPort(Integer mqttPort) {
        this.mqttPort = mqttPort;
    }
    public void setMqttUsername(String mqttUsername) {
        this.mqttUsername = mqttUsername;
    }
    public void setMqttPassword(String mqttPassword) {
        this.mqttPassword = mqttPassword;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public void setMqttClientId(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }
    public void setMqttBaseTopic(String mqttBaseTopic) {
        this.mqttBaseTopic = mqttBaseTopic;
    }
    public void setMqttTopicList(List<HashMap<String, Object>> mqttTopicList) {
        this.mqttTopicList = mqttTopicList;
    }
}
