package it.wldt.adapter.mqtt.digital.topic.outgoing;

import java.util.function.Function;

public interface MqttPublishDigitalFunction<T> extends Function<T, String> {
}
