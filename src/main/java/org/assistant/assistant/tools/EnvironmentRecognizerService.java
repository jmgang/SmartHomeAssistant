package org.assistant.assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import org.assistant.api.llm.VertexAIGeminiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.assistant.config.ConfigLoader.getProperty;

public class EnvironmentRecognizerService {

    @Tool("Captures an image of the environment and describes the image in the perspective of an agent.")
    public String recognizeEnvironmentByImage() {
        try {
            captureImage();
            return VertexAIGeminiClient.analyzeImage(getProperty("assistant.tools.environment-recognizer.filepath"));
        } catch (Exception e) {
            e.printStackTrace();
            return "Having trouble processing an image.";
        }

    }

    private void captureImage() throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("fswebcam -r --no-banner --png 9 " +
                getProperty("assistant.tools.environment-recognizer.filepath"));

        var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        int exitVal = process.waitFor();

        if(exitVal == 0) {
            System.out.println("Success capturing image");
        }else{
            System.out.println("Error capturing image");
        }
    }

}
