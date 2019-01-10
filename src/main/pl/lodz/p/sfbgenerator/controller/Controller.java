package pl.lodz.p.sfbgenerator.controller;

import com.google.zxing.WriterException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pl.lodz.p.sfbgenerator.file.FileManager;
import pl.lodz.p.sfbgenerator.qrcode.QRCodeGenerator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private ImageView qrCode;
    @FXML private Button dirChooserButton;
    @FXML private Text choosenDirText;
    private Stage stage;
    private File selectedDirectory;
    @FXML Button generateButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            qrCode.setImage(QRCodeGenerator.getQRCodeImage("ala ma kota", 200, 200));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedDirectory = directoryChooser.showDialog(stage);

        if(selectedDirectory == null){
            choosenDirText.setText("-");
        }else{
            choosenDirText.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void generateObjectModel(ActionEvent event) {
        if(!FileManager.checkDirectory(selectedDirectory)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Directory is empty or contains not only image files");
            alert.showAndWait();
        }
    }
}
