package it.unibo.disi.wldt.mqttda.utils;

import it.unimore.dipi.iot.wldt.adapter.digital.event.DigitalActionWldtEvent;
import it.unimore.dipi.iot.wldt.adapter.physical.PhysicalAssetDescription;
import it.unimore.dipi.iot.wldt.adapter.physical.event.PhysicalAssetEventWldtEvent;
import it.unimore.dipi.iot.wldt.adapter.physical.event.PhysicalAssetPropertyWldtEvent;
import it.unimore.dipi.iot.wldt.core.model.ShadowingModelFunction;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateAction;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateEvent;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateEventNotification;
import it.unimore.dipi.iot.wldt.core.state.DigitalTwinStateProperty;
import it.unimore.dipi.iot.wldt.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

public class  DefaultShadowingFunction extends ShadowingModelFunction {

    private static final Logger logger = LoggerFactory.getLogger(DefaultShadowingFunction.class);

    public DefaultShadowingFunction() {
        super("default-shadowing-function");
    }

    @Override
    protected void onCreate() {
        logger.debug("Shadowing - OnCreate");
    }

    @Override
    protected void onStart() {
        logger.debug("Shadowing - OnStart");
    }

    @Override
    protected void onStop() {
        logger.debug("Shadowing - OnStop");
    }

    @Override
    protected void onDigitalTwinBound(Map<String, PhysicalAssetDescription> adaptersPhysicalAssetDescriptionMap) {
        logger.debug("Shadowing - onDtBound");
        startShadowing(adaptersPhysicalAssetDescriptionMap);
        try {
            this.observePhysicalAssetProperties(adaptersPhysicalAssetDescriptionMap.values()
                    .stream()
                    .flatMap(pad -> pad.getProperties().stream())
                    .collect(Collectors.toList()));
            //observes all the available events
            this.observePhysicalAssetEvents(adaptersPhysicalAssetDescriptionMap.values()
                    .stream()
                    .flatMap(pad -> pad.getEvents().stream())
                    .collect(Collectors.toList()));
            observeDigitalActionEvents();
        } catch (EventBusException | ModelException e) {
            e.printStackTrace();
        }
    }

    private void startShadowing(Map<String, PhysicalAssetDescription> adaptersPhysicalAssetDescriptionMap) {
        adaptersPhysicalAssetDescriptionMap.forEach((id, pad) -> {
            pad.getProperties()
                    .forEach(p -> {
                        try {
                            this.digitalTwinState.createProperty(new DigitalTwinStateProperty<>(p.getKey(), p.getInitialValue()));
                        } catch (WldtDigitalTwinStateException | WldtDigitalTwinStatePropertyBadRequestException
                                | WldtDigitalTwinStatePropertyConflictException | WldtDigitalTwinStatePropertyException e) {
                            e.printStackTrace();
                        }
                    });
            pad.getActions().forEach(a -> {
                try {
                    this.digitalTwinState.enableAction(new DigitalTwinStateAction(a.getKey(), a.getType(), a.getContentType()));
                } catch (WldtDigitalTwinStateActionException | WldtDigitalTwinStateActionConflictException
                        | WldtDigitalTwinStateException e) {
                    e.printStackTrace();
                }
            });
            pad.getEvents().forEach(e -> {
                try {
                    this.digitalTwinState.registerEvent(new DigitalTwinStateEvent(e.getKey(), e.getType()));
                } catch (WldtDigitalTwinStateEventException | WldtDigitalTwinStateEventConflictException ex) {
                    ex.printStackTrace();
                }
            });
            notifyShadowingSync();
        });
    }

    @Override
    protected void onDigitalTwinUnBound(Map<String, PhysicalAssetDescription> adaptersPhysicalAssetDescriptionMap, String errorMessage) {
        logger.debug("Shadowing - onDTUnBound - error: {} ", errorMessage);
    }

    @Override
    protected void onPhysicalAdapterBidingUpdate(String adapterId, PhysicalAssetDescription adapterPhysicalAssetDescription) {
        logger.info("Shadowing - onPABindingUpdate - updated Adapter: {}, new PAD: {}", adapterId, adapterPhysicalAssetDescription);
    }

    @Override
    protected void onPhysicalAssetPropertyVariation(PhysicalAssetPropertyWldtEvent<?> physicalPropertyEventMessage) {
        logger.info("Shadowing - onPAPropertyVariation - property event: {} ", physicalPropertyEventMessage);
        //Update Digital Twin Status
        try {
            this.digitalTwinState.updateProperty(
                    new DigitalTwinStateProperty<>(
                            physicalPropertyEventMessage.getPhysicalPropertyId(),
                            physicalPropertyEventMessage.getBody()));
        } catch (WldtDigitalTwinStatePropertyException | WldtDigitalTwinStatePropertyBadRequestException
                | WldtDigitalTwinStatePropertyNotFoundException | WldtDigitalTwinStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPhysicalAssetEventNotification(PhysicalAssetEventWldtEvent<?> physicalAssetEventWldtEvent) {
        logger.info("Shadowing - onPAEventNotification - received Event:{}", physicalAssetEventWldtEvent);
        try {
            this.digitalTwinState.notifyDigitalTwinStateEvent(new DigitalTwinStateEventNotification<>(
                    physicalAssetEventWldtEvent.getPhysicalEventKey(),
                    (String) physicalAssetEventWldtEvent.getBody(),
                    System.currentTimeMillis()));
        } catch (WldtDigitalTwinStateEventNotificationException | EventBusException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDigitalActionEvent(DigitalActionWldtEvent<?> digitalActionWldtEvent) {
        try {
            publishPhysicalAssetActionWldtEvent(digitalActionWldtEvent.getActionKey(), digitalActionWldtEvent.getBody());
        } catch (EventBusException e) {
            e.printStackTrace();
        }
    }
}
