package it.wldt.adapter.mqtt.digital.topic;

public class MqttTopic {

    private final String topic;
    private MqttQosLevel qosLevel = MqttQosLevel.MQTT_QOS_0;

    public MqttTopic(String topic) {
        this.topic = topic;
    }

    public MqttTopic(String topic, MqttQosLevel qosLevel) {
        this.topic = topic;
        this.qosLevel = qosLevel;
    }

    public String getTopic() {
        return topic;
    }

    public Integer getQos() {
        return qosLevel.getQosValue();
    }

    public void setQosLevel(MqttQosLevel qosLevel) {
        this.qosLevel = qosLevel;
    }
}
