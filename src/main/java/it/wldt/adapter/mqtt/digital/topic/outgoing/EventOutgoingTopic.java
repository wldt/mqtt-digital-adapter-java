package it.wldt.adapter.mqtt.digital.topic.outgoing;

import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.core.state.DigitalTwinStateEvent;

/**
 * Represents an outgoing topic for publishing events in a digital twin system.
 * This class extends the {@link DigitalTwinOutgoingTopic} class and is specifically
 * designed for handling the publishing of {@link DigitalTwinStateEvent} instances.
 * The {@code EventOutgoingTopic} class simplifies the process of publishing
 * digital twin state events to a specified MQTT topic using the provided
 * {@link MqttQosLevel} and {@link MqttPublishDigitalFunction}.
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class EventOutgoingTopic extends DigitalTwinOutgoingTopic<DigitalTwinStateEvent>{

    /**
     * Constructs an {@code EventOutgoingTopic} with the specified topic, QoS level,
     * and function for publishing digital twin state events.
     *
     * @param topic                  The topic to publish outgoing events to.
     * @param qosLevel               The quality of service level for message delivery.
     * @param publishDigitalFunction The function to apply for publishing digital twin state events.
     */
    public EventOutgoingTopic(String topic, MqttQosLevel qosLevel, MqttPublishDigitalFunction<DigitalTwinStateEvent> publishDigitalFunction) {
        super(topic, qosLevel, publishDigitalFunction);
    }
}
