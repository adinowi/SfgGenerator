package pl.lodz.p.sfbgenerator.controller;

import com.google.zxing.WriterException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import pl.lodz.p.sfbgenerator.qrcode.QRCodeGenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private ImageView qrCode;


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
}
