package it.unibo.disi.wldt.mqttda.topic;

public enum MqttQosLevel {

    MQTT_QOS_0(0),
    MQTT_QOS_1(1),
    MQTT_QOS_2(2);

    private final int qosValue;

    MqttQosLevel(int qosValue) {
        this.qosValue = qosValue;
    }

    public int getQosValue() {
        return qosValue;
    }
}
