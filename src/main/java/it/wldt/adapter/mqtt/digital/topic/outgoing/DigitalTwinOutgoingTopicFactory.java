package it.wldt.adapter.mqtt.digital.topic.outgoing;

import com.google.gson.Gson;
import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.core.state.DigitalTwinStateProperty;

public class DigitalTwinOutgoingTopicFactory {
    private static final String STATE_ACTIONS_PREFIX = "state/actions/";
    private static final String STATE_EVENTS_PREFIX = "state/events/";
    private static final String STATE_PROPERTIES_PREFIX = "state/properties/";

    private static final String ENABLED_SUFFIX = "/enabled";
    private static final String DISABLED_SUFFIX = "/disabled";
    private static final String UPDATED_SUFFIX = "/updated";
    private static final String REGISTERED_SUFFIX = "/registered";
    private static final String UNREGISTERED_SUFFIX = "/unregistered";
    private static final String NOTIFICATION_SUFFIX = "/notification";
    private static final String CREATED_SUFFIX = "/created";
    private static final String DELETED_SUFFIX = "/deleted";

    private static ActionOutgoingTopic createDefaultActionOutgoingTopic(String topic, MqttQosLevel qosLevel){
        return new ActionOutgoingTopic(topic,
                qosLevel,
                digitalTwinStateAction -> new Gson().toJson(digitalTwinStateAction));
    }

    private static EventOutgoingTopic createDefaultEventOutgoingTopic(String topic, MqttQosLevel qosLevel){
        return new EventOutgoingTopic(topic,
                qosLevel,
                digitalTwinStateEvent -> new Gson().toJson(digitalTwinStateEvent));
    }

    private static DigitalTwinOutgoingTopic<DigitalTwinStateProperty<?>> createDefaultPropertyOutgoingTopic(String topic, MqttQosLevel qosLevel){
        return new DigitalTwinOutgoingTopic<>(topic,
        qosLevel,
        digitalTwinStateProperty -> new Gson().toJson(digitalTwinStateProperty));
    }

    public static ActionOutgoingTopic createDefaultActionEnabledTopic(String actionKey, MqttQosLevel qosLevel){
        return createDefaultActionOutgoingTopic(STATE_ACTIONS_PREFIX+actionKey+ENABLED_SUFFIX, qosLevel);
    }

    public static ActionOutgoingTopic createDefaultActionDisabledTopic(String actionKey, MqttQosLevel qosLevel){
        return createDefaultActionOutgoingTopic(STATE_ACTIONS_PREFIX+actionKey+DISABLED_SUFFIX, qosLevel);
    }

    public static ActionOutgoingTopic createDefaultActionUpdatedTopic(String actionKey, MqttQosLevel qosLevel){
        return createDefaultActionOutgoingTopic(STATE_ACTIONS_PREFIX+actionKey+UPDATED_SUFFIX, qosLevel);
    }

    public static EventOutgoingTopic createDefaultEventRegisteredTopic(String eventKey, MqttQosLevel qosLevel){
        return createDefaultEventOutgoingTopic(STATE_EVENTS_PREFIX+eventKey+REGISTERED_SUFFIX, qosLevel);
    }

    public static EventOutgoingTopic createDefaultEventUnregisteredTopic(String eventKey, MqttQosLevel qosLevel){
        return createDefaultEventOutgoingTopic(STATE_EVENTS_PREFIX+eventKey+UNREGISTERED_SUFFIX, qosLevel);
    }

    public static EventOutgoingTopic createDefaultEventUpdatedTopic(String eventKey, MqttQosLevel qosLevel){
        return createDefaultEventOutgoingTopic(STATE_EVENTS_PREFIX+eventKey+UPDATED_SUFFIX, qosLevel);
    }

    public static DigitalTwinOutgoingTopic<DigitalTwinStateProperty<?>> createDefaultPropertyCreatedTopic(String propertyKey, MqttQosLevel qosLevel){
        return createDefaultPropertyOutgoingTopic(STATE_PROPERTIES_PREFIX+propertyKey+CREATED_SUFFIX, qosLevel);
    }

    public static DigitalTwinOutgoingTopic<DigitalTwinStateProperty<?>> createDefaultPropertyDeletedTopic(String propertyKey, MqttQosLevel qosLevel){
        return createDefaultPropertyOutgoingTopic(STATE_PROPERTIES_PREFIX+propertyKey+DELETED_SUFFIX, qosLevel);
    }
}
