package it.wldt.adapter.mqtt.digital;

import it.wldt.adapter.mqtt.digital.topic.MqttQosLevel;
import it.wldt.adapter.mqtt.digital.utils.DefaultShadowingFunction;
import it.wldt.adapter.mqtt.digital.utils.DummyPhysicalAdapter;
import it.wldt.adapter.mqtt.digital.utils.DummyPhysicalAdapterConfiguration;
import it.wldt.core.engine.DigitalTwin;
import it.wldt.core.engine.DigitalTwinEngine;

/**
 * The `TestMain` class serves as the entry point for a demonstration of Digital Twin components. It creates a
 * Digital Twin instance, assigns a Dummy Physical Adapter to simulate physical variations, configures an MQTT Digital
 * Adapter, and adds them to the Digital Twin. Finally, it initiates the Digital Twin Engine and starts the simulation.
 * This serves as a test scenario for the integration of Digital Twin components.
 */
public class TestMain {

    /**
     * The main method, the starting point of the demonstration.
     *
     * @param args Command line arguments (unused).
     */
    public static void main(String[] args) {

        try {

            // Create a Digital Twin instance named "mqtt-digital-twin" with a DefaultShadowingFunction
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
