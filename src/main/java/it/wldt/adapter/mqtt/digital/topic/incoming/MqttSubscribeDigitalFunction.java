package it.wldt.adapter.mqtt.digital.topic.incoming;

import it.wldt.adapter.digital.event.DigitalActionWldtEvent;
import java.util.function.Function;

/**
 * Represents a function for subscribing to digital twin-related messages
 * via MQTT (Message Queuing Telemetry Transport). This interface extends
 * the {@link Function} interface and is specifically designed for converting
 * incoming message payloads to {@link DigitalActionWldtEvent} instances.
 *
 * Implementations of this interface should provide custom logic for
 * handling incoming message payloads and creating corresponding
 * {@link DigitalActionWldtEvent} objects.
 *
 * @see Function
 * @see DigitalActionWldtEvent
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
public interface MqttSubscribeDigitalFunction extends Function<String, DigitalActionWldtEvent<?>> {
}
