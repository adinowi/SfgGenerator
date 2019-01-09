package pl.lodz.p.sfbgenerator.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URLEncoder;

public class DataApi {
    private static String HOST_NAME = "http://localhost:3000/";

    public static String sendDesignObject(String name, String category, File image, File sfb, ResponseHandler<HttpResponse> responseHandler) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addTextBody("name", name)
                    .addTextBody("category", category)
                    .addPart("image", new FileBody(image))
                    .addPart("sfb", new FileBody(sfb))
                    .build();

            HttpPost request = new HttpPost(HOST_NAME + "designobjects");
            request.addHeader(HttpHeaders.ACCEPT, "application/json");
            request.setEntity(entity);

            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(request, responseHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
