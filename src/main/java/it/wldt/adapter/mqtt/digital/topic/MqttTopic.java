package it.wldt.adapter.mqtt.digital.topic;

/**
 * Represents an MQTT topic in a digital twin system, which can be used for publishing or subscribing to messages.
 *
 * <p>
 * The {@code MqttTopic} class encapsulates the topic string and Quality of Service (QoS) level.
 * It provides methods for retrieving the topic and QoS level, as well as setting the QoS level.
 * </p>
 *
 * <p>
 * Usage example:
 * </p>
 *
 * @see MqttQosLevel
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class MqttTopic {

    /**
     * The topic value
     */
    private final String topic;

    /**
     * The taget QoS Level
     */
    private MqttQosLevel qosLevel = MqttQosLevel.MQTT_QOS_0;

    /**
     * Constructs an {@code MqttTopic} with the specified topic string.
     *
     * @param topic The string representing the MQTT topic.
     */
    public MqttTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Constructs an {@code MqttTopic} with the specified topic string and QoS level.
     *
     * @param topic    The string representing the MQTT topic.
     * @param qosLevel The Quality of Service (QoS) level for message delivery.
     */
    public MqttTopic(String topic, MqttQosLevel qosLevel) {
        this.topic = topic;
        this.qosLevel = qosLevel;
    }

    /**
     * Gets the string representing the MQTT topic.
     *
     * @return The MQTT topic string.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Gets the Quality of Service (QoS) level for message delivery.
     *
     * @return The QoS level as an integer value.
     */
    public Integer getQos() {
        return qosLevel.getQosValue();
    }

    /**
     * Sets the Quality of Service (QoS) level for message delivery.
     *
     * @param qosLevel The QoS level to set.
     */
    public void setQosLevel(MqttQosLevel qosLevel) {
        this.qosLevel = qosLevel;
    }
}
