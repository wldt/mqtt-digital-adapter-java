package it.unibo.disi.wldt.mqttda.topic.outgoing;

import java.util.function.Function;

public interface MqttPublishDigitalFunction<T> extends Function<T, String> {
}
