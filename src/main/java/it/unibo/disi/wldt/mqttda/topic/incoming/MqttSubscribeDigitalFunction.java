package it.unibo.disi.wldt.mqttda.topic.incoming;

import it.unimore.dipi.iot.wldt.adapter.digital.event.DigitalActionWldtEvent;
import java.util.function.Function;

public interface MqttSubscribeDigitalFunction extends Function<String, DigitalActionWldtEvent<?>> {
}
