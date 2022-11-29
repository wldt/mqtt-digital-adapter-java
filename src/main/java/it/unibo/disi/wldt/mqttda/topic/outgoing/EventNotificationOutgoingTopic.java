package it.unibo.disi.wldt.mqttda.topic.outgoing;

import it.unibo.disi.wldt.mqttda.topic.MqttQosLevel;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateEvent;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateEventNotification;

import java.util.function.Function;

public class EventNotificationOutgoingTopic<T> extends DigitalTwinOutgoingTopic<DigitalTwinStateEventNotification<T>>{
    public EventNotificationOutgoingTopic(String topic, MqttQosLevel qosLevel, Function<T, String> notificationBodyToString) {
        super(topic, qosLevel, eventNotification -> notificationBodyToString.apply(eventNotification.getBody()));
    }
}
