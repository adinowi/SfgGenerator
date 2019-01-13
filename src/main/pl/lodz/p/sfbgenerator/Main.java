package pl.lodz.p.sfbgenerator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import pl.lodz.p.sfbgenerator.controller.Controller;
import pl.lodz.p.sfbgenerator.service.DataApi;

import java.io.File;
import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/view.fxml"));
        Parent root = (Parent)loader.load();
        Controller controller = (Controller)loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
        /*String a = DataApi.sendDesignObject("armchair", "armchair",
                new File("C:\\Users\\adino\\Desktop\\test\\texturedmesh.jpg"),
                new File("C:\\Users\\adino\\Desktop\\test\\texturedMesh.sfb"),
                new ResponseHandler<HttpResponse>() {
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            System.out.println(endpoint);

                            return httpResponse;
                        }
                        return null;
                    }
                });
        System.out.println(a);*/


    }


    public static void main(String[] args) {
        launch(args);
    }
}
