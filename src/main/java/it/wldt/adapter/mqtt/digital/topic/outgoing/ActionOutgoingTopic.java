package it.wldt.adapter.mqtt.digital.topic.outgoing;

import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.core.state.DigitalTwinStateAction;

public class ActionOutgoingTopic extends DigitalTwinOutgoingTopic<DigitalTwinStateAction> {
    public ActionOutgoingTopic(String topic, MqttQosLevel qosLevel, MqttPublishDigitalFunction<DigitalTwinStateAction> publishDigitalFunction) {
        super(topic, qosLevel, publishDigitalFunction);
    }
}
