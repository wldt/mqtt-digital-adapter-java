package it.wldt.adapter.mqtt.digital.topic.outgoing;

import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.adapter.mqtt.digital.topic.MqttTopic;
import it.wldt.core.state.DigitalTwinStateEventNotification;
import it.wldt.core.state.DigitalTwinStateProperty;

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
