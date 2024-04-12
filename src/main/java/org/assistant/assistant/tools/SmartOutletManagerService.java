package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;

public class SmartOutletManagerService {

    @Tool("Toggles the smart strip, either on or off. To toggle {{it}}")
    public String toggleStrip(String toggle) {
        System.out.println(toggle);

        return "Query was vague. Ask the user which specific outlet to turn on or off";
    }

    @Tool("Toggles the first outlet in a smart strip, either on or off. To toggle {{it}}")
    public String toggleFirstOutlet(String toggle) {
        System.out.println(toggle);

        return "The first outlet has been switched " + toggle;
    }

    @Tool("Toggles the second outlet in a smart strip, either on or off. To toggle {{it}}")
    public String toggleSecondOutlet(String toggle) {
        System.out.println(toggle);

        return "The second outlet has been switched " + toggle;
    }

    @Tool("Toggles the third outlet in a smart strip, either on or off. To toggle {{it}}")
    public String toggleThirdOutlet(String toggle) {
        System.out.println(toggle);

        return "The third outlet has been switched " + toggle;
    }
}
