package pl.lodz.p.sfbgenerator.processing;

import javafx.scene.text.Text;
import pl.lodz.p.sfbgenerator.controller.Controller;
import pl.lodz.p.sfbgenerator.file.FileManager;

import java.io.File;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Text;

public class GenerateAllTask implements Runnable {
    private String command;
    private Controller controller;
    private String imageDir;
    private String category;

    public GenerateAllTask(String command, Controller controller, String imageDir, String category) {
        this.command = command;
        this.controller = controller;
        this.imageDir = imageDir;
        this.category = category;
    }

    @Override
    public void run() {
        GenerateObjectTask generateObjectTask = new GenerateObjectTask(command, controller);
        generateObjectTask.run();

        if(FileManager.checkGeneratingObj()) {
            File obj = new File(FileManager.GENERATED_OBJ);
            GenerateSfbTask generateSfbTask = new GenerateSfbTask(obj.getAbsolutePath(), controller, imageDir, category);
            generateSfbTask.run();
        }
    }
}
