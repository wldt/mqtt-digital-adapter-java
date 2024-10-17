package it.wldt.adapter.mqtt.digital.topic.outgoing;

import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.adapter.mqtt.digital.topic.MqttTopic;
import it.wldt.core.state.DigitalTwinStateEventNotification;
import it.wldt.core.state.DigitalTwinStateProperty;

/**
 * Represents an outgoing topic for publishing messages in the context of a digital twin system.
 * This class extends the {@link MqttTopic} class and is designed to simplify the process
 * of publishing messages of a generic type {@code T} using a specified {@link MqttPublishDigitalFunction}.
 *
 * The {@code DigitalTwinOutgoingTopic} class encapsulates the logic for applying the
 * publishing function to the provided digital twin state components, such as
 * {@link DigitalTwinStateProperty} and {@link DigitalTwinStateEventNotification}, and
 * converting them into message payloads for publishing.
 *
 * @param <T> The generic type representing the digital twin state components.
 * @see MqttTopic
 * @see MqttQosLevel
 * @see MqttPublishDigitalFunction
 * @see DigitalTwinStateProperty
 * @see DigitalTwinStateEventNotification
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class DigitalTwinOutgoingTopic<T> extends MqttTopic {
    private final MqttPublishDigitalFunction<T> publishDigitalFunction;

    /**
     * Constructs a {@code DigitalTwinOutgoingTopic} with the specified topic, QoS level,
     * and function for publishing digital twin state components.
     *
     * @param topic                  The topic to publish outgoing messages to.
     * @param qosLevel               The quality of service level for message delivery.
     * @param publishDigitalFunction The function to apply for publishing digital twin state components.
     */
    public DigitalTwinOutgoingTopic(String topic, MqttQosLevel qosLevel, MqttPublishDigitalFunction<T> publishDigitalFunction) {
        super(topic, qosLevel);
        this.publishDigitalFunction = publishDigitalFunction;
    }

    /**
     * Constructs a {@code DigitalTwinOutgoingTopic} with the specified topic, QoS level,
     * and function for publishing digital twin state components.
     *
     * @param topic                  The topic to publish outgoing messages to.
     * @param qosLevel               The quality of service level for message delivery.
     * @param isRetained             The retained flag.
     * @param publishDigitalFunction The function to apply for publishing digital twin state components.
     */
    public DigitalTwinOutgoingTopic(String topic, MqttQosLevel qosLevel, boolean isRetained, MqttPublishDigitalFunction<T> publishDigitalFunction) {
        super(topic, qosLevel, isRetained);
        this.publishDigitalFunction = publishDigitalFunction;
    }

    /**
     * Applies the publishing function to the provided digital twin state property component.
     * Converts the component into a message payload for publishing.
     *
     * @param digitalTwinStateComponent The digital twin state property component to publish.
     * @return The message payload for publishing.
     * @see MqttPublishDigitalFunction
     * @see DigitalTwinStateProperty
     */
    public String applyPublishFunction(DigitalTwinStateProperty<?> digitalTwinStateComponent){
        return publishDigitalFunction.apply((T) digitalTwinStateComponent);
    }

    /**
     * Applies the publishing function to the provided digital twin state event notification component.
     * Converts the component into a message payload for publishing.
     *
     * @param digitalTwinStateComponent The digital twin state event notification component to publish.
     * @return The message payload for publishing.
     * @see MqttPublishDigitalFunction
     * @see DigitalTwinStateEventNotification
     */
    public String applyPublishFunction(DigitalTwinStateEventNotification<?> digitalTwinStateComponent){
        return publishDigitalFunction.apply((T) digitalTwinStateComponent);
    }
}
