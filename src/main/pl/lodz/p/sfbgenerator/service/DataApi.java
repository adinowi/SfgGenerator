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
import java.sql.Timestamp;

public class DataApi {
    private static String HOST_NAME = "https://interior-design-api.o-and-m.ovh/";

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

    public static String sendDesignObject( String category, File image, File sfb, ResponseHandler<HttpResponse> responseHandler) {
        return sendDesignObject(createName(category), category, image, sfb, responseHandler);
    }

    private static String createName(String category) {
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());

        return "object_" +  category + "_" + timestamp;
    }
}
