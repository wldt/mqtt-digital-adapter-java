# MQTT Digital Adapter Java

This README provides an overview of the main classes in the Digital Twin library, focusing on the `MqttDigitalAdapter`,  
`MqttDigitalAdapterConfiguration`, and `MqttDigitalAdapterConfigurationBuilder` classes and guides you through using these classes to set up an MQTT Digital Adapter within WLDT.

Requires an external MQTT broker to send messages.

Main functionalities are: 

- Manages the interaction between the Digital Twin and external systems.
- Handles state updates, events, and property changes.
- Dynamic configuration of the MqttDigitalAdapter with broker details, topics, and other settings.
- Allows customization of data and payload management associated to MQTT topics for properties, events, and actions.

<div align="center">
  <img class="center" src="images/adapter_schema.jpg" width="100%">
</div>

Prerequisites:

- **External MQTT Broker:** The MqttDigitalAdapter library requires an external MQTT broker for optimal functionality and communication.
- Users must have access to a reliable MQTT broker to which the adapter can subscribe and publish.
- This external broker serves as the central communication hub, facilitating the exchange of messages between the adapter and digital applications

A complete example is provided in the `test` folder with a complete DT Creation in the `TestMain` class together with MQTT IoT demo device and
a test MQTT consumer.

## WLDT-Core Version Compatibility

The correct mapping and compatibility between versions is reported in the following table

| **mqtt-digital-adapter** |   wldt-core 0.2.1  |   wldt-core 0.3.0  |   wldt-core 0.4.0  |
|:------------------------:|:------------------:|:------------------:|:------------------:|
|          0.1.0           | :white_check_mark: |         :x:        |         :x:        |
|          0.1.1           |         :x:        | :white_check_mark: | :white_check_mark: |
|          0.1.2           |         :x:        | :white_check_mark: | :white_check_mark: |

## Installation

To use MqttDigitalAdapter in your Java project, you can include it as a dependency using Maven or Gradle.

### Maven

```xml
<dependency>
    <groupId>io.github.wldt</groupId>
    <artifactId>mqtt-digital-adapter</artifactId>
    <version>0.1.2</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.wldt:mqtt-digital-adapter:0.1.2'
```

## Class Structure & Functionalities

### MqttDigitalAdapterConfiguration

`MqttDigitalAdapterConfiguration` is a crucial class in the Digital Twin library, allowing developers to configure the behavior of the MQTT Digital Adapter. 
It provides a flexible and customizable way to set up MQTT communication parameters, topics for properties, events, and actions.

Key functionalities and exposed capabilities: 

- **Broker Configuration**
  - `brokerAddress` and `brokerPort`: Set the MQTT broker's address and port.
  - `username` and `password`: Set optional credentials for connecting to the broker.
- **Client Configuration**
  - `clientId`: Unique identifier for the MQTT client.
  - `cleanSessionFlag`: Flag indicating whether the client starts with a clean session.
  - `connectionTimeout`: Maximum time to wait for the connection to the MQTT broker.
- **MQTT Client Persistence**
  - `persistence`: Configurable persistence for the MQTT client's data.
- **Reconnect Configuration**:
  - `automaticReconnectFlag`: Flag enabling or disabling automatic reconnection.
- **Topic Configuration**:
  - `propertyUpdateTopics`: Map of property update topics.
  - `eventNotificationTopics`: Map of event notification topics.
  - `actionIncomingTopics`: Map of incoming action topics.
- **Builder Methods**:
  - `builder`: Static method to start building a new configuration. 
  - `addPropertyTopic`: Add a property topic with specified parameters. 
  - `addEventNotificationTopic`: Add an event notification topic. 
  - `addActionTopic`: Add an action topic. 
  - `setConnectionTimeout`: Set the connection timeout. 
  - `setCleanSessionFlag`: Set the clean session flag. 
  - `setAutomaticReconnectFlag`: Set the automatic reconnect flag. 
  - `setMqttClientPersistence`: Set the MQTT client persistence. 
  - `build`: Finalize the configuration and build the instance.

### MqttDigitalAdapterConfigurationBuilder

The `MqttDigitalAdapterConfigurationBuilder` is a powerful tool designed to simplify the process of constructing configurations 
for the MQTT Digital Adapter in the Digital Twin library. It offers a fluent and intuitive interface, allowing developers to define various aspects of MQTT communication seamlessly.

#### Builder Instantiation

The builder is instantiated by providing essential parameters like brokerAddress and brokerPort or including an optional clientId

```java
MqttDigitalAdapterConfigurationBuilder builder = MqttDigitalAdapterConfiguration.builder("127.0.0.1", 1883);
```

#### Adding Property Topics

Developers can add property topics with specific configurations, such as the key, topic, QoS level, and a function to convert property values to payload.

```java
builder.addPropertyTopic("energy", "dummy/properties/energy", MqttQosLevel.MQTT_QOS_0, value -> String.valueOf(((Double)value).intValue()));
```

#### Adding Event Notification Topics

Event notification topics are easily added, including event keys, topics, QoS levels, and payload conversion functions.

```java
builder.addEventNotificationTopic("overheating", "dummy/events/overheating/notifications", MqttQosLevel.MQTT_QOS_0, Object::toString);
```

#### Adding Action Topics

Developers can include action topics with key, topic, and a function to convert the payload to the desired action.

```java
builder.addActionTopic("switch_off", "app/actions/switch-off", msg -> "OFF");
```

#### Connection Options

Developers can set the connection timeout for the MQTT client.

```java
builder.setConnectionTimeout(30);
```

The clean session flag can be configured based on the desired behavior.

```java
builder.setCleanSessionFlag(true);
```

Developers can specify whether the MQTT client should automatically reconnect in case of a connection failure.

```java
builder.setAutomaticReconnectFlag(true);
```

The builder allows setting a custom MQTT client persistence, such as an in-memory persistence or a file-based one.

```java
builder.setMqttClientPersistence(new MemoryPersistence());
```

#### Building Configuration

The final configuration is built using the build method.

```java
MqttDigitalAdapterConfiguration configuration = builder.build();
```

### MqttDigitalAdapter

`MqttDigitalAdapter` extends DigitalAdapter and specializes in MQTT communication for Digital Twin instances. 
It handles the publication of state updates, events, and property changes over MQTT. 
This class facilitates seamless integration with MQTT-enabled systems.

It uses the information defined and provided in the `` to handle the communication both with the DT Core and external 
application interested to interact with the DT through the MQTT protocol.

Here's a basic example illustrating how to use MqttDigitalAdapter:

```java
// Create a Digital Twin instance
DigitalTwin digitalTwin = new DigitalTwin("my-digital-twin", new DefaultShadowingFunction());

// Add a Physical Adapter to the DT 
[...]

// Build the MQTT Digital Adapter Configuration
MqttDigitalAdapterConfiguration configuration = MqttDigitalAdapterConfiguration.builder("127.0.0.1", 1883)
    .addPropertyTopic("energy", "dummy/properties/energy", MqttQosLevel.MQTT_QOS_0, value -> String.valueOf(((Double)value).intValue()))
    .addActionTopic("switch_off", "app/actions/switch-off", msg -> "OFF")
    .build();

// Add the MQTT Digital Adapter to the Digital Twin
digitalTwin.addDigitalAdapter(new MqttDigitalAdapter("mqtt-da", configuration));

// Create the Digital Twin Engine and start the simulation
DigitalTwinEngine digitalTwinEngine = new DigitalTwinEngine();

digitalTwinEngine.addDigitalTwin(digitalTwin);
digitalTwinEngine.startAll();
```