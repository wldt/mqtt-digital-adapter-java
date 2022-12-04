package it.unibo.disi.wldt.mqttda.topic.incoming;

import it.unibo.disi.wldt.mqttda.topic.MqttTopic;
import it.unimore.dipi.iot.wldt.adapter.digital.event.DigitalActionWldtEvent;
import it.unimore.dipi.iot.wldt.adapter.physical.event.PhysicalAssetActionWldtEvent;

public class DigitalTwinIncomingTopic extends MqttTopic {

    private final MqttSubscribeDigitalFunction subscribeDigitalFunction;

    public DigitalTwinIncomingTopic(String topic, MqttSubscribeDigitalFunction subscribeDigitalFunction) {
        super(topic);
        this.subscribeDigitalFunction = subscribeDigitalFunction;
    }

    public DigitalActionWldtEvent<?> applySubscribeFunction(String messagePayload) {
        return this.subscribeDigitalFunction.apply(messagePayload);
    }
}
