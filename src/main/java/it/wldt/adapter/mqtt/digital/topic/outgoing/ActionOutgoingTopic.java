package it.wldt.adapter.mqtt.digital.topic.outgoing;

import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.core.state.DigitalTwinStateAction;

/**
 * Represents an outgoing topic for publishing actions in a digital twin system.
 * This class extends the {@link DigitalTwinOutgoingTopic} class and is specifically
 * designed for handling the publishing of {@link DigitalTwinStateAction} instances.
 *
 * The {@code ActionOutgoingTopic} class simplifies the process of publishing
 * digital twin state actions to a specified MQTT topic using the provided
 * {@link MqttPublishDigitalFunction}.
 *
 *
 * @see DigitalTwinOutgoingTopic
 * @see MqttQosLevel
 * @see MqttPublishDigitalFunction
 * @see DigitalTwinStateAction
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class ActionOutgoingTopic extends DigitalTwinOutgoingTopic<DigitalTwinStateAction> {

    /**
     * Constructs an {@code ActionOutgoingTopic} with the specified topic, QoS level,
     * and function for publishing digital twin state actions.
     *
     * @param topic                  The topic to publish outgoing actions to.
     * @param qosLevel               The quality of service level for message delivery.
     * @param publishDigitalFunction The function to apply for publishing digital twin state actions.
     */
    public ActionOutgoingTopic(String topic, MqttQosLevel qosLevel, MqttPublishDigitalFunction<DigitalTwinStateAction> publishDigitalFunction) {
        super(topic, qosLevel, publishDigitalFunction);
    }
}
