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
import pl.lodz.p.sfbgenerator.service.DataApi;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
    @FXML private Text qrProgress;
    @FXML private Text idText;
    @FXML private Text statusIdModel;
    @FXML private TextField idInput;
    @FXML private CheckBox isSub;
    @FXML private ColorPicker colorPicker;

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
        if(checkObjGeneration()) {
            File tempDir = FileManager.createTempDir();
            try {

                String command = "\"\"" + new File(FileManager.CLI_EXE) + "\" \"" + tempDir.getCanonicalPath() + "\" \"" +
                        selectedImagesDirectory.getPath() + "\" \"" + new File(FileManager.MESH_ROOM_BIN) + "\" " +
                        String.valueOf(FileManager.getFilesCount(selectedImagesDirectory)) + " " + FileManager.RUN_OPTION + "\"";

                new Thread(new GenerateObjectTask(command, this)).start();



            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void generateSfbModel(ActionEvent event) {

            if(checkSfbGeneration()) {
                try {
                    new Thread(new GenerateSfbTask(selectedObjDirectory.getCanonicalPath(), this,
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
        if(checkImgIsSub() && checkObjGeneration()) {
            try {

                String command = "\"\"" + new File(FileManager.CLI_EXE) + "\" \"" + tempDir.getCanonicalPath() + "\" \"" +
                        selectedImagesDirectory.getPath() + "\" \"" + new File(FileManager.MESH_ROOM_BIN) + "\" " +
                        String.valueOf(FileManager.getFilesCount(selectedImagesDirectory)) + " " + FileManager.RUN_OPTION + "\"";

                new Thread(new GenerateAllTask(command,
                        this, choosenImgDir.getAbsolutePath(), categorySpinner.getValue())).start();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Text getObjProgressText() {
        return objProgressText;
    }

    public Text getSfbProgressText() {
        return sfbProgressText;
    }

    public Text getQrProgress() {
        return qrProgress;
    }

    public void changeIsSub() {
        if(isSub.isSelected()) {
            idText.setOpacity(1);
            idInput.setOpacity(1);
        } else {
            idText.setOpacity(0);
            idInput.setOpacity(0);
        }
    }

    private boolean checkSfbGeneration() {
        if(selectedObjDirectory == null || !FileManager.checkObjValidation(selectedObjDirectory)) {
            showError("It is wrong file! You need obj, fbx, gltf extension");
            return false;
        }

        if(!checkImgIsSub()) {
            return false;
        }
        return true;
    }

    private boolean checkImgIsSub() {
        if(choosenImgDir != null) {
            Path path = choosenImgDir.toPath();
            try {
                String mimeType = Files.probeContentType(path);
                if (!mimeType.matches("image\\/.*")) {
                    showError("Wrong image");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                showError("Wrong image");
                return false;
            }
        } else {
            showError("No choosen img");
            return false;
        }

        if(isSub.isSelected()) {
            try {
                long id = Long.valueOf(idInput.getText());
                if(!DataApi.checkModelExist(id)) {
                    showError("Id is incorrect");
                    return false;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showError("Id is incorrect");
                return false;
            }
        }

        return true;
    }

    private boolean checkObjGeneration() {
        if(!FileManager.checkDirectory(selectedImagesDirectory)) {
            showError("Directory is empty or contains not only image files");
            return false;
        }
         return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public Text getStatusIdModel() {
        return statusIdModel;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public CheckBox getIsSub() {
        return isSub;
    }

    public TextField getIdInput() {
        return idInput;
    }
}


