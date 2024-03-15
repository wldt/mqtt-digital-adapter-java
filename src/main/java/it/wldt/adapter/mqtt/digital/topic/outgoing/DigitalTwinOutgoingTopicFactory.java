package it.wldt.adapter.mqtt.digital.topic.outgoing;

import com.google.gson.Gson;
import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.core.state.DigitalTwinStateProperty;

/**
 * Factory class for creating default outgoing topics in a digital twin system.
 * This class provides methods for creating default outgoing topics for actions,
 * events, and properties with specific suffixes indicating the state or event type.
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com, Marta Spadoni University of Bologna
 */
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


    /**
     * Creates a default action outgoing topic for the specified topic and QoS level.
     *
     * @param topic    The topic to publish outgoing actions to.
     * @param qosLevel The quality of service level for message delivery.
     * @return A default action outgoing topic.
     * @see ActionOutgoingTopic
     */
    private static ActionOutgoingTopic createDefaultActionOutgoingTopic(String topic, MqttQosLevel qosLevel){
        return new ActionOutgoingTopic(topic,
                qosLevel,
                digitalTwinStateAction -> new Gson().toJson(digitalTwinStateAction));
    }

    /**
     * Creates a default event outgoing topic for the specified topic and QoS level.
     *
     * @param topic    The topic to publish outgoing events to.
     * @param qosLevel The quality of service level for message delivery.
     * @return A default event outgoing topic.
     * @see EventOutgoingTopic
     */
    private static EventOutgoingTopic createDefaultEventOutgoingTopic(String topic, MqttQosLevel qosLevel){
        return new EventOutgoingTopic(topic,
                qosLevel,
                digitalTwinStateEvent -> new Gson().toJson(digitalTwinStateEvent));
    }

    /**
     * Creates a default property outgoing topic for the specified topic and QoS level.
     *
     * @param topic    The topic to publish outgoing properties to.
     * @param qosLevel The quality of service level for message delivery.
     * @return A default property outgoing topic.
     * @see DigitalTwinOutgoingTopic
     * @see DigitalTwinStateProperty
     */
    private static DigitalTwinOutgoingTopic<DigitalTwinStateProperty<?>> createDefaultPropertyOutgoingTopic(String topic, MqttQosLevel qosLevel){
        return new DigitalTwinOutgoingTopic<>(topic,
        qosLevel,
        digitalTwinStateProperty -> new Gson().toJson(digitalTwinStateProperty));
    }

    /**
     * Creates a default action outgoing topic for the "enabled" state with the specified action key.
     *
     * @param actionKey The key identifying the type of action associated with the topic.
     * @param qosLevel  The quality of service level for message delivery.
     * @return A default action outgoing topic for the "enabled" state.
     * @see ActionOutgoingTopic
     */
    public static ActionOutgoingTopic createDefaultActionEnabledTopic(String actionKey, MqttQosLevel qosLevel){
        return createDefaultActionOutgoingTopic(STATE_ACTIONS_PREFIX+actionKey+ENABLED_SUFFIX, qosLevel);
    }

    /**
     * Creates a default action outgoing topic for the "disabled" state with the specified action key.
     *
     * @param actionKey The key identifying the type of action associated with the topic.
     * @param qosLevel  The quality of service level for message delivery.
     * @return A default action outgoing topic for the "disabled" state.
     * @see ActionOutgoingTopic
     */
    public static ActionOutgoingTopic createDefaultActionDisabledTopic(String actionKey, MqttQosLevel qosLevel){
        return createDefaultActionOutgoingTopic(STATE_ACTIONS_PREFIX+actionKey+DISABLED_SUFFIX, qosLevel);
    }

    /**
     * Creates a default action outgoing topic for the "updated" state with the specified action key.
     *
     * @param actionKey The key identifying the type of action associated with the topic.
     * @param qosLevel  The quality of service level for message delivery.
     * @return A default action outgoing topic for the "updated" state.
     * @see ActionOutgoingTopic
     */
    public static ActionOutgoingTopic createDefaultActionUpdatedTopic(String actionKey, MqttQosLevel qosLevel){
        return createDefaultActionOutgoingTopic(STATE_ACTIONS_PREFIX+actionKey+UPDATED_SUFFIX, qosLevel);
    }

    /**
     * Creates a default event outgoing topic for the "registered" state with the specified event key.
     *
     * @param eventKey The key identifying the type of event associated with the topic.
     * @param qosLevel The quality of service level for message delivery.
     * @return A default event outgoing topic for the "registered" state.
     * @see EventOutgoingTopic
     */
    public static EventOutgoingTopic createDefaultEventRegisteredTopic(String eventKey, MqttQosLevel qosLevel){
        return createDefaultEventOutgoingTopic(STATE_EVENTS_PREFIX+eventKey+REGISTERED_SUFFIX, qosLevel);
    }

    /**
     * Creates a default event outgoing topic for the "unregistered" state with the specified event key.
     *
     * @param eventKey The key identifying the type of event associated with the topic.
     * @param qosLevel The quality of service level for message delivery.
     * @return A default event outgoing topic for the "unregistered" state.
     * @see EventOutgoingTopic
     */
    public static EventOutgoingTopic createDefaultEventUnregisteredTopic(String eventKey, MqttQosLevel qosLevel){
        return createDefaultEventOutgoingTopic(STATE_EVENTS_PREFIX+eventKey+UNREGISTERED_SUFFIX, qosLevel);
    }

    /**
     * Creates a default event outgoing topic for the "updated" state with the specified event key.
     *
     * @param eventKey The key identifying the type of event associated with the topic.
     * @param qosLevel The quality of service level for message delivery.
     * @return A default event outgoing topic for the "updated" state.
     * @see EventOutgoingTopic
     */
    public static EventOutgoingTopic createDefaultEventUpdatedTopic(String eventKey, MqttQosLevel qosLevel){
        return createDefaultEventOutgoingTopic(STATE_EVENTS_PREFIX+eventKey+UPDATED_SUFFIX, qosLevel);
    }

    /**
     * Creates a default property outgoing topic for the "created" state with the specified property key.
     *
     * @param propertyKey The key identifying the type of property associated with the topic.
     * @param qosLevel    The quality of service level for message delivery.
     * @return A default property outgoing topic for the "created" state.
     * @see DigitalTwinOutgoingTopic
     * @see DigitalTwinStateProperty
     */
    public static DigitalTwinOutgoingTopic<DigitalTwinStateProperty<?>> createDefaultPropertyCreatedTopic(String propertyKey, MqttQosLevel qosLevel){
        return createDefaultPropertyOutgoingTopic(STATE_PROPERTIES_PREFIX+propertyKey+CREATED_SUFFIX, qosLevel);
    }

    /**
     * Creates a default property outgoing topic for the "deleted" state with the specified property key.
     *
     * @param propertyKey The key identifying the type of property associated with the topic.
     * @param qosLevel    The quality of service level for message delivery.
     * @return A default property outgoing topic for the "deleted" state.
     * @see DigitalTwinOutgoingTopic
     * @see DigitalTwinStateProperty
     */
    public static DigitalTwinOutgoingTopic<DigitalTwinStateProperty<?>> createDefaultPropertyDeletedTopic(String propertyKey, MqttQosLevel qosLevel){
        return createDefaultPropertyOutgoingTopic(STATE_PROPERTIES_PREFIX+propertyKey+DELETED_SUFFIX, qosLevel);
    }
}
