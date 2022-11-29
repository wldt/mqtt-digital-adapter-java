package it.unibo.disi.wldt.mqttda;

import it.unibo.disi.wldt.mqttda.exception.MqttDigitalAdapterConfigurationException;
import it.unibo.disi.wldt.mqttda.topic.MqttQosLevel;
import it.unibo.disi.wldt.mqttda.utils.DefaultShadowingFunction;
import it.unibo.disi.wldt.mqttda.utils.DummyPhysicalAdapter;
import it.unibo.disi.wldt.mqttda.utils.DummyPhysicalAdapterConfiguration;
import it.unimore.dipi.iot.wldt.core.engine.WldtConfiguration;
import it.unimore.dipi.iot.wldt.core.engine.WldtEngine;
import it.unimore.dipi.iot.wldt.exception.EventBusException;
import it.unimore.dipi.iot.wldt.exception.ModelException;
import it.unimore.dipi.iot.wldt.exception.WldtConfigurationException;
import it.unimore.dipi.iot.wldt.exception.WldtRuntimeException;
import org.eclipse.paho.client.mqttv3.MqttException;

public class TestMain {
    public static void main(String[] args) throws WldtConfigurationException, ModelException, WldtRuntimeException, EventBusException, MqttDigitalAdapterConfigurationException, MqttException {

        WldtEngine engine = new WldtEngine(new DefaultShadowingFunction(), buildWldtConfiguration());
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

    private static WldtConfiguration buildWldtConfiguration() {

        //Manual creation of the WldtConfiguration
        WldtConfiguration wldtConfiguration = new WldtConfiguration();
        wldtConfiguration.setDeviceNameSpace("it.unimore.dipi.things");
        wldtConfiguration.setWldtBaseIdentifier("wldt");
        wldtConfiguration.setWldtStartupTimeSeconds(10);
        return wldtConfiguration;
    }
}
