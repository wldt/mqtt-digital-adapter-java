package it.unibo.disi.wldt.mqttda.topic.outgoing;

import it.unibo.disi.wldt.mqttda.topic.MqttQosLevel;
import it.unibo.disi.wldt.mqttda.topic.MqttTopic;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateEventNotification;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateProperty;

public class DigitalTwinOutgoingTopic<T> extends MqttTopic {
    private final MqttPublishDigitalFunction<T> publishDigitalFunction;

    public DigitalTwinOutgoingTopic(String topic, MqttQosLevel qosLevel, MqttPublishDigitalFunction<T> publishDigitalFunction) {
        super(topic, qosLevel);
        this.publishDigitalFunction = publishDigitalFunction;
    }

    public String applyPublishFunction(DigitalTwinStateProperty<?> digitalTwinStateComponent){
        return publishDigitalFunction.apply((T) digitalTwinStateComponent);
    }

    public String applyPublishFunction(DigitalTwinStateEventNotification<?> digitalTwinStateComponent){
        return publishDigitalFunction.apply((T) digitalTwinStateComponent);
    }
}
