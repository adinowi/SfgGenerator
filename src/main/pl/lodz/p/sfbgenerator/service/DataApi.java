package pl.lodz.p.sfbgenerator.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;

public class DataApi {
    private static String HOST_NAME = "https://interior-design-api.o-and-m.ovh/";

    public static String sendDesignObject(String name, String category, File image, File sfb, String color, ResponseHandler<HttpResponse> responseHandler) {
        return sendObject(name, category, image, sfb,"designobjects/", color, responseHandler);
    }

    public static String sendDesignObject( String category, File image, File sfb, String color, ResponseHandler<HttpResponse> responseHandler) {
        return sendDesignObject(createName(category), category, image, sfb, color, responseHandler);
    }

    private static String createName(String category) {
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());

        return "object_" +  category + "_" + timestamp;
    }

    public static boolean checkModelExist(long id) {
        HttpGet request = new HttpGet(HOST_NAME + "designobjects/" + String.valueOf(id));
        request.addHeader(HttpHeaders.ACCEPT, "application/json");
        Boolean result;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(request);

            if(response.getStatusLine().getStatusCode() == 200) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String sendObject(String name, String category, File image, File sfb, String endpoint, String color, ResponseHandler<HttpResponse> responseHandler) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addTextBody("name", name)
                    .addTextBody("category", category)
                    .addPart("image", new FileBody(image))
                    .addPart("sfb", new FileBody(sfb))
                    .addTextBody("color", color)
                    .build();

            HttpPost request = new HttpPost(HOST_NAME +  endpoint);
            request.addHeader(HttpHeaders.ACCEPT, "application/json");
            request.setEntity(entity);

            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(request, responseHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String sendSubObject(String name, String category, File image, File sfb, String color, long id, ResponseHandler<HttpResponse> responseHandler) {
        String endpoint = "designobjects/" + String.valueOf(id) + "/subdesignobjects/";
         return sendObject(name, category, image, sfb, endpoint, color, responseHandler);
    }

    public static String sendSubObject(String category, File image, File sfb, String color, long id, ResponseHandler<HttpResponse> responseHandler) {
        String endpoint = "designobjects/" + String.valueOf(id) + "/subdesignobjects/";
        return sendObject(createName(category), category, image, sfb, endpoint, color, responseHandler);
    }
}
