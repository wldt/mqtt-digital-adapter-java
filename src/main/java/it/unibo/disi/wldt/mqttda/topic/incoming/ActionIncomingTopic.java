package it.unibo.disi.wldt.mqttda.topic.incoming;

import it.unimore.dipi.iot.wldt.adapter.physical.event.PhysicalAssetActionWldtEvent;
import it.unimore.dipi.iot.wldt.exception.EventBusException;

import java.util.function.Function;

public class ActionIncomingTopic<T> extends DigitalTwinIncomingTopic{
    public ActionIncomingTopic(String topic, String actionKey,  Function<String, T> messageToAction) {
        super(topic, messagePayload -> {
            try {
                return new PhysicalAssetActionWldtEvent<>(actionKey, messageToAction.apply(messagePayload));
            } catch (EventBusException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
