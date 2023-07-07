package it.wldt.adapter.mqtt.digital;

import it.wldt.adapter.mqtt.digital.exception.MqttDigitalAdapterConfigurationException;
import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.adapter.mqtt.digital.utils.DefaultShadowingFunction;
import it.wldt.adapter.mqtt.digital.utils.DummyPhysicalAdapter;
import it.wldt.adapter.mqtt.digital.utils.DummyPhysicalAdapterConfiguration;
import it.wldt.core.engine.WldtEngine;
import it.wldt.exception.EventBusException;
import it.wldt.exception.ModelException;
import it.wldt.exception.WldtConfigurationException;
import it.wldt.exception.WldtRuntimeException;
import org.eclipse.paho.client.mqttv3.MqttException;

public class TestMain {
    public static void main(String[] args) throws WldtConfigurationException, ModelException, WldtRuntimeException, EventBusException, MqttDigitalAdapterConfigurationException, MqttException {

        WldtEngine engine = new WldtEngine(new DefaultShadowingFunction(), "mqtt-digital-twin");
        engine.addPhysicalAdapter(new DummyPhysicalAdapter("test-pa", new DummyPhysicalAdapterConfiguration(), true));

        MqttDigitalAdapterConfiguration configuration = MqttDigitalAdapterConfiguration.builder("127.0.0.1", 1883)
                .addPropertyTopic("energy", "dummy/properties/energy", MqttQosLevel.MQTT_QOS_0, value -> String.valueOf(((Double)value).intValue()))
                .addEventNotificationTopic("overheating", "dummy/events/overheating/notifications", MqttQosLevel.MQTT_QOS_0, Object::toString)
                .addPropertyTopic("switch", "dummy/properties/switch", MqttQosLevel.MQTT_QOS_0, Object::toString)
                .addActionTopic("switch_off", "app/actions/switch-off", msg -> "OFF")
                .addActionTopic("switch_on", "app/actions/switch-on", msg -> "ON")
                .build();
        engine.addDigitalAdapter(new MqttDigitalAdapter("test-da", configuration));

        engine.startLifeCycle();

    }
}
