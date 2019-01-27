package pl.lodz.p.sfbgenerator.processing;

import javafx.scene.text.Text;
import pl.lodz.p.sfbgenerator.controller.Controller;

import java.io.IOException;

public class GenerateObjectTask implements Runnable {
    private String command;
    private Process process;
    private Controller controller;

    public GenerateObjectTask(String command, Controller controller) {
        this.command = command;
    }

    @Override
    public void run() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd","/c", "start","/wait", "cmd.exe","/c", command);
            process = processBuilder.start();
            controller.getObjProgressText().setText("Generating...");
            process.waitFor();
            controller.getObjProgressText().setText("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
