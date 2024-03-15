package it.wldt.adapter.mqtt.digital.topic;

/**
 * Enumeration representing the Quality of Service (QoS) levels for MQTT message delivery.
 *
 * <p>
 * The MQTT protocol defines three QoS levels to control the message delivery guarantees:
 * </p>
 *
 * <ul>
 *   <li>{@link #MQTT_QOS_0}: At most once delivery (0)</li>
 *   <li>{@link #MQTT_QOS_1}: At least once delivery (1)</li>
 *   <li>{@link #MQTT_QOS_2}: Exactly once delivery (2)</li>
 * </ul>
 *
 * <p>
 * Each QoS level provides a different level of assurance regarding the delivery of a message.
 * Clients can choose the appropriate QoS level based on their specific requirements for message delivery.
 * </p>
 *
 * @see <a href="https://docs.oasis-open.org/mqtt/mqtt/v5.0/csd01/mqtt-v5.0-csd01.html#_Quality_of_Service">MQTT Specification: Quality of Service</a>
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public enum MqttQosLevel {

    /**
     * Represents the MQTT Quality of Service (QoS) level 0.
     * At most once delivery.
     */
    MQTT_QOS_0(0),

    /**
     * Represents the MQTT Quality of Service (QoS) level 1.
     * At least once delivery.
     */
    MQTT_QOS_1(1),

    /**
     * Represents the MQTT Quality of Service (QoS) level 2.
     * Exactly once delivery.
     */
    MQTT_QOS_2(2);

    /**
     * The current/target QoS Level Value
     */
    private final int qosValue;

    /**
     * Constructs an {@code MqttQosLevel} enum with the specified QoS value.
     *
     * @param qosValue The integer value representing the QoS level.
     */
    MqttQosLevel(int qosValue) {
        this.qosValue = qosValue;
    }

    /**
     * Gets the integer value representing the QoS level.
     *
     * @return The QoS level as an integer value.
     */
    public int getQosValue() {
        return qosValue;
    }
}
