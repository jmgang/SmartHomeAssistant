package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;

public class SmartOutletManagerService {

    @Tool("Toggles the outlet either on or off. To toggle {{it}}")
    public String toggleOutlet(String query) {
        System.out.println(query);

        return "The outlet has switched " + query;
    }
}
