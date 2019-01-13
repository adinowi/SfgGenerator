package pl.lodz.p.sfbgenerator.processing;

import javafx.scene.text.Text;
import pl.lodz.p.sfbgenerator.controller.Controller;
import pl.lodz.p.sfbgenerator.file.FileManager;

import java.io.File;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Text;

public class GenerateAllTask implements Runnable {
    private Text objProgressText;
    private String command;
    private Text sfbProgressText;
    private Controller controller;
    private String imageDir;
    private String category;

    public GenerateAllTask(Text objProgressText, String command, Text sfbProgressText, Controller controller, String imageDir, String category) {
        this.objProgressText = objProgressText;
        this.command = command;
        this.sfbProgressText = sfbProgressText;
        this.controller = controller;
        this.imageDir = imageDir;
        this.category = category;
    }

    @Override
    public void run() {
        GenerateObjectTask generateObjectTask = new GenerateObjectTask(objProgressText, command);
        generateObjectTask.run();

        if(FileManager.checkGeneratingObj()) {
            File obj = new File(FileManager.GENERATED_OBJ);
            GenerateSfbTask generateSfbTask = new GenerateSfbTask(sfbProgressText, obj.getAbsolutePath(), controller, imageDir, category);
            generateSfbTask.run();
        }
    }
}
