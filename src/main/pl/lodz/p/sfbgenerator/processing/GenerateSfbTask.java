package pl.lodz.p.sfbgenerator.processing;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import pl.lodz.p.sfbgenerator.controller.Controller;
import pl.lodz.p.sfbgenerator.service.DataApi;

import java.io.File;
import java.io.IOException;

public class GenerateSfbTask implements Runnable {
    private Process process;
    private String fileDir;
    private Controller controller;
    private String imageDir;
    private String category;

    public GenerateSfbTask(String fileDir, Controller controller, String imageDir, String category) {
        this.fileDir = fileDir;
        this.controller = controller;
        this.imageDir = imageDir;
        this.category = category;
    }

    @Override
    public void run() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("converter.exe", fileDir);
            process = processBuilder.start();
            controller.getSfbProgressText().setText("Generating...");
            process.waitFor();
            controller.getSfbProgressText().setText("Generated sfb");
            controller.getQrProgress().setText("Generating...");
            String sfbName = (new File(fileDir).getName().split("[.]"))[0] + ".sfb";
            Color color = controller.getColorPicker().getValue();
            String hexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
            ResponseHandler<HttpResponse> responseHandler = new ResponseHandler<HttpResponse>() {
                @Override
                public HttpResponse handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    if(httpResponse.getStatusLine().getStatusCode() == 201) {
                        HttpEntity entity = httpResponse.getEntity();
                        String endpoint = "";
                        if (entity != null) {
                            String retSrc = EntityUtils.toString(entity);
                            // parsing JSON

                            try {
                                JSONObject result = new JSONObject(retSrc); //Convert String to JSON Object

                                endpoint = result.getString("endpoint");
                                controller.getStatusIdModel().setText(endpoint.split("[/]")[1]);
                                controller.generateQrCode(endpoint);
                                controller.getQrProgress().setText("Created QR code");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println(endpoint);

                        return httpResponse;
                    }
                    return null;
                }
            };
            if(!controller.getIsSub().isSelected()) {
                DataApi.sendDesignObject(category, new File(imageDir), new File(sfbName), hexColor, responseHandler);
            } else {
                DataApi.sendSubObject(category, new File(imageDir), new File(sfbName), hexColor, Long.valueOf(controller.getIdInput().getText()), responseHandler);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
