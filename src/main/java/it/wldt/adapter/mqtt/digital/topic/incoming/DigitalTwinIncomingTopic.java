package it.wldt.adapter.mqtt.digital.topic.incoming;

import it.wldt.adapter.mqtt.digital.topic.MqttTopic;
import it.wldt.adapter.digital.event.DigitalActionWldtEvent;

/**
 * Represents an incoming topic in the context of a digital twin system.
 * This class extends the {@link MqttTopic} class and is designed to handle
 * the subscription of digital twin-related messages using a specified
 * {@link MqttSubscribeDigitalFunction}.
 *
 * The {@code DigitalTwinIncomingTopic} class encapsulates the logic for
 * applying the subscription function to the incoming message payload and
 * returning a {@link DigitalActionWldtEvent} representing the digital twin action.
 **
 * @see MqttTopic
 * @see MqttSubscribeDigitalFunction
 * @see DigitalActionWldtEvent
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class DigitalTwinIncomingTopic extends MqttTopic {

    private final MqttSubscribeDigitalFunction subscribeDigitalFunction;

    /**
     * Constructs a {@code DigitalTwinIncomingTopic} with the specified topic
     * and function for handling incoming digital twin messages.
     *
     * @param topic                    The topic to subscribe to for incoming digital twin messages.
     * @param subscribeDigitalFunction The function to apply for handling incoming message payloads.
     */
    public DigitalTwinIncomingTopic(String topic, MqttSubscribeDigitalFunction subscribeDigitalFunction) {
        super(topic);
        this.subscribeDigitalFunction = subscribeDigitalFunction;
    }

    /**
     * Applies the subscription function to the provided message payload.
     * Converts the payload to a {@link DigitalActionWldtEvent} representing
     * the digital twin action.
     *
     * @param messagePayload The payload of the incoming message.
     * @return A {@link DigitalActionWldtEvent} representing the digital twin action.
     * @see MqttSubscribeDigitalFunction
     * @see DigitalActionWldtEvent
     */
    public DigitalActionWldtEvent<?> applySubscribeFunction(String messagePayload) {
        return this.subscribeDigitalFunction.apply(messagePayload);
    }
}
