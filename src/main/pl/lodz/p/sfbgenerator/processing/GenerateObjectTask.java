package pl.lodz.p.sfbgenerator.processing;

import javafx.scene.text.Text;

import java.io.IOException;

public class GenerateObjectTask implements Runnable {
    private Text progress;
    private String command;
    private Process process;

    public GenerateObjectTask(Text progress, String command) {
        this.progress = progress;
        this.command = command;
    }

    @Override
    public void run() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd","/c", "start","/wait", "cmd.exe","/c", command);
            process = processBuilder.start();
            progress.setText("Generating...");
            process.waitFor();
            progress.setText("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
