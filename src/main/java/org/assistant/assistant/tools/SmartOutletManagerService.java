package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;

import java.io.IOException;
import java.util.Map;

import static org.assistant.api.homeassistant.KasaSmartStripClient.*;
import static org.assistant.config.ConfigLoader.getProperty;

public class SmartOutletManagerService {

    private static final Map<String, String> STRIP_ENTITY_MAP = Map.of("FIRST", getProperty("assistant.api.homeassistant.strip.entity-id.first"),
            "SECOND", getProperty("assistant.api.homeassistant.strip.entity-id.second"),
            "THIRD", getProperty("assistant.api.homeassistant.strip.entity-id.third"));

    @Tool("Toggles the smart strip or outlets, either on or off. To toggle {{it}}")
    public String turnOnSpecificOutletOrStrip(String toggle) {
        System.out.println(toggle);

        return "Query was vague. Ask the user which specific outlet to turn on or off";
    }

    @Tool("Turns on a specific outlet number in a smart strip. The choices are only first, second and third.")
    public String turnOnSpecificOutlet(String outletNumber) {
        outletNumber = outletNumber.toUpperCase();
        if(!STRIP_ENTITY_MAP.containsKey(outletNumber)) {
            return "The specified outlet " + outletNumber  + " was not found. Please specify again.";
        }

        if(isOn(STRIP_ENTITY_MAP.get(outletNumber))) {
            return "The " + outletNumber + " outlet is already turned on.";
        }

        try {
            turnOn(STRIP_ENTITY_MAP.get(outletNumber));
            return "The " + outletNumber + " outlet was turned on successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "I'm having trouble turning on the outlet.";
        }
    }

    @Tool("Turns off a specific outlet number in a smart strip. The choices are only first, second and third.")
    public String turnOffSpecificOutlet(String outletNumber) {
        outletNumber = outletNumber.toUpperCase();
        if(!STRIP_ENTITY_MAP.containsKey(outletNumber)) {
            return "The specified outlet " + outletNumber  + " was not found. Please specify again.";
        }

        if(!isOn(STRIP_ENTITY_MAP.get(outletNumber))) {
            return "The " + outletNumber + " outlet is already turned off.";
        }

        try {
            turnOff(STRIP_ENTITY_MAP.get(outletNumber));
            return "The " + outletNumber + " outlet was turned off successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "I'm having trouble turning off the outlet.";
        }
    }
}
