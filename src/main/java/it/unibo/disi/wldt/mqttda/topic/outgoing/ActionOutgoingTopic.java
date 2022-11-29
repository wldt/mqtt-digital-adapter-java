package it.unibo.disi.wldt.mqttda.topic.outgoing;

import it.unibo.disi.wldt.mqttda.topic.MqttQosLevel;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateAction;

public class ActionOutgoingTopic extends DigitalTwinOutgoingTopic<DigitalTwinStateAction> {
    public ActionOutgoingTopic(String topic, MqttQosLevel qosLevel, MqttPublishDigitalFunction<DigitalTwinStateAction> publishDigitalFunction) {
        super(topic, qosLevel, publishDigitalFunction);
    }
}
