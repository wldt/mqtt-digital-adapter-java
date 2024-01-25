package it.wldt.adapter.mqtt.digital;

import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.adapter.mqtt.digital.utils.DefaultShadowingFunction;
import it.wldt.adapter.mqtt.digital.utils.DummyPhysicalAdapter;
import it.wldt.adapter.mqtt.digital.utils.DummyPhysicalAdapterConfiguration;
import it.wldt.core.engine.DigitalTwin;
import it.wldt.core.engine.DigitalTwinEngine;

public class TestMain {
    public static void main(String[] args) {

        try {

            DigitalTwin digitalTwin = new DigitalTwin("mqtt-digital-twin", new DefaultShadowingFunction());

            // Create and assign a Demo Physical Adapter generating random physical variation to test the MQTT Digital Adapter
            digitalTwin.addPhysicalAdapter(
                    new DummyPhysicalAdapter(
                            "test-pa",
                            new DummyPhysicalAdapterConfiguration(),
                            true)
            );

            // Build the MQTT Digital Adapter Configuration
            MqttDigitalAdapterConfiguration configuration = MqttDigitalAdapterConfiguration.builder("127.0.0.1", 1883)
                    .addPropertyTopic("energy", "dummy/properties/energy", MqttQosLevel.MQTT_QOS_0, value -> String.valueOf(((Double)value).intValue()))
                    .addEventNotificationTopic("overheating", "dummy/events/overheating/notifications", MqttQosLevel.MQTT_QOS_0, Object::toString)
                    .addPropertyTopic("switch", "dummy/properties/switch", MqttQosLevel.MQTT_QOS_0, Object::toString)
                    .addActionTopic("switch_off", "app/actions/switch-off", msg -> "OFF")
                    .addActionTopic("switch_on", "app/actions/switch-on", msg -> "ON")
                    .build();

            // Add the MQTT Digital Adapter to the target Digital Twin
            digitalTwin.addDigitalAdapter(new MqttDigitalAdapter("test-da", configuration));

            // Create the Digital Twin Engine to execute the created DT instance
            DigitalTwinEngine digitalTwinEngine = new DigitalTwinEngine();

            // Add the Digital Twin to the Engine
            digitalTwinEngine.addDigitalTwin(digitalTwin);

            // Start all the DTs registered on the engine
            digitalTwinEngine.startAll();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
