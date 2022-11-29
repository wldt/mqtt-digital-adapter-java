package it.unibo.disi.wldt.mqttda.topic.outgoing;

import it.unibo.disi.wldt.mqttda.topic.MqttQosLevel;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateEvent;

public class EventOutgoingTopic extends DigitalTwinOutgoingTopic<DigitalTwinStateEvent>{
    public EventOutgoingTopic(String topic, MqttQosLevel qosLevel, MqttPublishDigitalFunction<DigitalTwinStateEvent> publishDigitalFunction) {
        super(topic, qosLevel, publishDigitalFunction);
    }
}
