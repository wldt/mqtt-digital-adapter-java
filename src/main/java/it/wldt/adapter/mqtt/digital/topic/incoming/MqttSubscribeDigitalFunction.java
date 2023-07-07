package it.wldt.adapter.mqtt.digital.topic.incoming;

import it.wldt.adapter.digital.event.DigitalActionWldtEvent;
import java.util.function.Function;

public interface MqttSubscribeDigitalFunction extends Function<String, DigitalActionWldtEvent<?>> {
}
