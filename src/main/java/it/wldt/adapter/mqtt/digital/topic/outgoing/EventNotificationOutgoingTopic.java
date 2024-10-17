package it.wldt.adapter.mqtt.digital.topic.outgoing;

import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.core.state.DigitalTwinStateEventNotification;

import java.util.function.Function;

/**
 * Represents an outgoing topic for publishing event notifications in a digital twin system.
 * This class extends the {@link DigitalTwinOutgoingTopic} class and is specifically
 * designed for handling the publishing of {@link DigitalTwinStateEventNotification} instances.
 *
 * The {@code EventNotificationOutgoingTopic} class simplifies the process of publishing
 * digital twin state event notifications to a specified MQTT topic using the provided
 * {@link MqttQosLevel} and {@link Function} for converting the notification body to a string.
 *
 * @param <T> The generic type representing the body of the event notification.
 * @see DigitalTwinOutgoingTopic
 * @see MqttQosLevel
 * @see DigitalTwinStateEventNotification
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class EventNotificationOutgoingTopic<T> extends DigitalTwinOutgoingTopic<DigitalTwinStateEventNotification<T>>{

    /**
     * Constructs an {@code EventNotificationOutgoingTopic} with the specified topic, QoS level,
     * and function for converting the notification body to a string.
     *
     * @param topic                The topic to publish outgoing event notifications to.
     * @param qosLevel             The quality of service level for message delivery.
     * @param notificationBodyToString The function to apply for converting the notification body to a string.
     */
    public EventNotificationOutgoingTopic(String topic, MqttQosLevel qosLevel, Function<T, String> notificationBodyToString) {
        super(topic, qosLevel, eventNotification -> notificationBodyToString.apply(eventNotification.getBody()));
    }

    /**
     * Constructs an {@code EventNotificationOutgoingTopic} with the specified topic, QoS level,
     * and function for converting the notification body to a string.
     *
     * @param topic                The topic to publish outgoing event notifications to.
     * @param qosLevel             The quality of service level for message delivery.
     * @param isRetained           The retained flag.
     * @param notificationBodyToString The function to apply for converting the notification body to a string.
     */
    public EventNotificationOutgoingTopic(String topic, MqttQosLevel qosLevel, boolean isRetained, Function<T, String> notificationBodyToString) {
        super(topic, qosLevel, isRetained, eventNotification -> notificationBodyToString.apply(eventNotification.getBody()));
    }
}
