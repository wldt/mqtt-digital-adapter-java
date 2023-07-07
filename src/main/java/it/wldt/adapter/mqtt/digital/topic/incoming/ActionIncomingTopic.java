package it.wldt.adapter.mqtt.digital.topic.incoming;

import it.wldt.adapter.digital.event.DigitalActionWldtEvent;
import it.wldt.exception.EventBusException;

import java.util.function.Function;

public class ActionIncomingTopic<T> extends DigitalTwinIncomingTopic{
    public ActionIncomingTopic(String topic, String actionKey,  Function<String, T> messageToAction) {
        super(topic, messagePayload -> {
            try {
                return new DigitalActionWldtEvent<>(actionKey, messageToAction.apply(messagePayload));
            } catch (EventBusException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
