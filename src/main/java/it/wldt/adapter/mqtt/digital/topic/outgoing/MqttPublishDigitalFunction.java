package it.wldt.adapter.mqtt.digital.topic.outgoing;

import java.util.function.Function;

/**
 * Represents a function that converts an object of type {@code T} to a string
 * suitable for publishing in a Digital Twin system using the MQTT protocol.
 *
 * Implementations of this interface define the logic for converting a digital twin
 * state component to a string format that can be transmitted over an MQTT topic.
 * This is typically used when publishing digital twin state actions, events, or properties.
 *
 * This interface extends the {@link Function} interface, indicating its role as a functional
 * interface that can be used as a lambda expression or method reference.

 * @param <T> The type of the input to the function, representing the digital twin state component.
 * @see Function
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public interface MqttPublishDigitalFunction<T> extends Function<T, String> {
}
