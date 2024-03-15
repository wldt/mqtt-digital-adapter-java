package it.wldt.adapter.mqtt.digital.topic.incoming;

import it.wldt.adapter.digital.event.DigitalActionWldtEvent;
import it.wldt.exception.EventBusException;

import java.util.function.Function;

/**
 * Represents an incoming topic for handling actions in a digital twin system.
 * This class extends the {@link DigitalTwinIncomingTopic} class and specializes
 * in converting incoming messages to {@link DigitalActionWldtEvent} instances.
 *
 * The {@code ActionIncomingTopic} class is designed to facilitate the reception
 * and processing of messages related to specific actions in the digital twin system.
 * It uses a provided {@link Function} to convert message payloads to the desired
 * action type, encapsulated in a {@link DigitalActionWldtEvent}.
 *
 * @param <T> The type of the action associated with this incoming topic.
 * @see DigitalTwinIncomingTopic
 * @see DigitalActionWldtEvent
 * @see Function
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public class ActionIncomingTopic<T> extends DigitalTwinIncomingTopic{

    /**
     * Constructs an {@code ActionIncomingTopic} with the specified topic, action key,
     * and function for converting message payloads to the associated action type.
     *
     * @param topic           The topic to subscribe to for incoming action messages.
     * @param actionKey       The key identifying the type of action associated with this topic.
     * @param messageToAction A function to convert message payloads to the associated action type.
     */
    public ActionIncomingTopic(String topic, String actionKey,  Function<String, T> messageToAction) {
        super(topic, messagePayload -> {
            try {
                return new DigitalActionWldtEvent<>(actionKey, messageToAction.apply(messagePayload));
            } catch (EventBusException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
