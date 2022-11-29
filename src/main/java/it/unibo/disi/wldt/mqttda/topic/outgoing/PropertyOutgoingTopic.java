package it.unibo.disi.wldt.mqttda.topic.outgoing;

import it.unibo.disi.wldt.mqttda.topic.MqttQosLevel;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateProperty;

import java.util.function.Function;

public class PropertyOutgoingTopic<T> extends DigitalTwinOutgoingTopic<DigitalTwinStateProperty<T>> {
    public PropertyOutgoingTopic(String topic, MqttQosLevel qosLevel, Function<T, String> propertyValueToString) {
        super(topic, qosLevel, dtStateProperty -> propertyValueToString.apply(dtStateProperty.getValue()));
    }
}
