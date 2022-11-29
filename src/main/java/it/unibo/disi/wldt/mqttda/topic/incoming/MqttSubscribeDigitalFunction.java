package it.unibo.disi.wldt.mqttda.topic.incoming;

import it.unimore.dipi.iot.wldt.adapter.physical.event.PhysicalAssetActionWldtEvent;

import java.util.function.Function;

public interface MqttSubscribeDigitalFunction extends Function<String, PhysicalAssetActionWldtEvent<?>> {
}
