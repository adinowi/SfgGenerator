package pl.lodz.p.sfbgenerator.controller;

import com.google.zxing.WriterException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.lodz.p.sfbgenerator.file.FileManager;
import pl.lodz.p.sfbgenerator.processing.GenerateAllTask;
import pl.lodz.p.sfbgenerator.processing.GenerateObjectTask;
import pl.lodz.p.sfbgenerator.processing.GenerateSfbTask;
import pl.lodz.p.sfbgenerator.qrcode.QRCodeGenerator;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private ImageView qrCode;
    @FXML private Button dirChooserButton;
    @FXML private Text choosenImagesDirText;
    @FXML private Text choosenObjDirText;
    private Stage stage;
    private File selectedImagesDirectory;
    private File selectedObjDirectory;
    @FXML private Button generateButton;
    @FXML private Text objProgressText;
    @FXML private Text sfbProgressText;
    @FXML private Spinner<String> categorySpinner;
    private File choosenImgDir;
    @FXML private ImageView image;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            qrCode.setImage(QRCodeGenerator.getQRCodeImage("ala ma kota", 200, 200));
            ObservableList<String> category = FXCollections.observableArrayList(//
                    "armchair", "bed", "couch", "lamp", //
                    "decoration", "wardrobe", "table");
            SpinnerValueFactory<String> valueFactory = //
                    new SpinnerValueFactory.ListSpinnerValueFactory<String>(category);
            categorySpinner.setValueFactory(valueFactory);

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedImagesDirectory = directoryChooser.showDialog(stage);

        if(selectedImagesDirectory == null){
            choosenImagesDirText.setText("-");
        }else{
            choosenImagesDirText.setText(selectedImagesDirectory.getAbsolutePath());
        }
    }

    public void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        //DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedObjDirectory = fileChooser.showOpenDialog(stage);

        if(selectedObjDirectory == null){
            choosenObjDirText.setText("-");
        }else{
            choosenObjDirText.setText(selectedObjDirectory.getAbsolutePath());
        }
    }

    public void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        //DirectoryChooser directoryChooser = new DirectoryChooser();
        choosenImgDir = fileChooser.showOpenDialog(stage);

        if(choosenImgDir == null){

        }else{
            Image image = new Image(choosenImgDir.toURI().toString());
            this.image.setImage(image);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void generateObjectModel(ActionEvent event) {
        if(!FileManager.checkDirectory(selectedImagesDirectory)) {
        //if(false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Directory is empty or contains not only image files");
            alert.showAndWait();
        } else {
            File tempDir = FileManager.createTempDir();
            try {

                String command = "\"\"" + new File(FileManager.CLI_EXE) + "\" \"" + tempDir.getCanonicalPath() + "\" \"" +
                        selectedImagesDirectory.getPath() + "\" \"" + new File(FileManager.MESH_ROOM_BIN) + "\" " +
                        String.valueOf(FileManager.getFilesCount(selectedImagesDirectory)) + " " + FileManager.RUN_OPTION + "\"";

                new Thread(new GenerateObjectTask(objProgressText, command)).start();



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void generateSfbModel(ActionEvent event) {
        if(false) {
            //if(false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Directory is empty or contains not only image files");
            alert.showAndWait();
        } else {
            try {
                new Thread(new GenerateSfbTask(sfbProgressText, selectedObjDirectory.getCanonicalPath(), this,
                        choosenImgDir.getAbsolutePath(), categorySpinner.getValue())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void generateQrCode(String text) {
        try {
            qrCode.setImage(QRCodeGenerator.getQRCodeImage(text, 200, 200));

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateAll(ActionEvent event) {
        File tempDir = FileManager.createTempDir();
        try {

            String command = "\"\"" + new File(FileManager.CLI_EXE) + "\" \"" + tempDir.getCanonicalPath() + "\" \"" +
                    selectedImagesDirectory.getPath() + "\" \"" + new File(FileManager.MESH_ROOM_BIN) + "\" " +
                    String.valueOf(FileManager.getFilesCount(selectedImagesDirectory)) + " " + FileManager.RUN_OPTION + "\"";

            new Thread(new GenerateAllTask(objProgressText, command, sfbProgressText,
                    this, choosenImgDir.getAbsolutePath(), categorySpinner.getValue())).start();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


