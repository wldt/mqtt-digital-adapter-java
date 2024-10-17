package it.wldt.adapter.mqtt.digital.topic.outgoing;

import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.core.state.DigitalTwinStateProperty;

import java.util.function.Function;

/**
 * Represents an outgoing topic for publishing digital twin state properties in a digital twin system.
 * This class extends the {@link DigitalTwinOutgoingTopic} class and is specifically designed for handling
 * the publishing of {@link DigitalTwinStateProperty} instances.
 *
 * The {@code PropertyOutgoingTopic} class simplifies the process of publishing digital twin state properties
 * to a specified MQTT topic using the provided {@link MqttQosLevel} and {@link Function} for converting
 * the property value to a string.
 *
 * @param <T> The generic type representing the type of the property value.
 * @see DigitalTwinOutgoingTopic
 * @see MqttQosLevel
 * @see DigitalTwinStateProperty
 * @see Function
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class PropertyOutgoingTopic<T> extends DigitalTwinOutgoingTopic<DigitalTwinStateProperty<T>> {

    /**
     * Constructs a {@code PropertyOutgoingTopic} with the specified topic, QoS level,
     * and function for converting the property value to a string.
     *
     * @param topic                  The topic to publish outgoing properties to.
     * @param qosLevel               The quality of service level for message delivery.
     * @param propertyValueToString The function to apply for converting the property value to a string.
     */
    public PropertyOutgoingTopic(String topic, MqttQosLevel qosLevel, Function<T, String> propertyValueToString) {
        super(topic, qosLevel, dtStateProperty -> propertyValueToString.apply(dtStateProperty.getValue()));
    }

    /**
     * Constructs a {@code PropertyOutgoingTopic} with the specified topic, QoS level,
     * and function for converting the property value to a string.
     *
     * @param topic                  The topic to publish outgoing properties to.
     * @param qosLevel               The quality of service level for message delivery.
     * @param isRetained             The retained flag.
     * @param propertyValueToString The function to apply for converting the property value to a string.
     */
    public PropertyOutgoingTopic(String topic, MqttQosLevel qosLevel, boolean isRetained, Function<T, String> propertyValueToString) {
        super(topic, qosLevel, isRetained, dtStateProperty -> propertyValueToString.apply(dtStateProperty.getValue()));
    }
}
