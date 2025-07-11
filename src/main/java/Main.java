import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.entity.ContentType;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        String data = getData();
        String url = getURL(data);
        getFile(url);
    }

    public static void getFile(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("Local Network Agent")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(url);

        CloseableHttpResponse response = httpClient.execute(request);
        InputStream bodyStream = response.getEntity().getContent();

        String savesPath = "D://Courses.IT//NETOLOGY.Java Developer//Модуль 04 Java Core//Games//";
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        FileOutputStream fout = new FileOutputStream(savesPath + fileName);
        fout.write(bodyStream.readAllBytes());

        fout.flush();
        fout.close();
        response.close();
        httpClient.close();
    }

    public static String getData()  throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("Local Network Agent")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=");
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);

        // Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
        InputStream bodyStream = response.getEntity().getContent();
        String body = new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);

        response.close();
        httpClient.close();

        return body;
    }

    public static String getURL(String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        NASAResource resource = mapper.readValue(
                data,
                new TypeReference<>() {}
        );

        return resource.getURL();

    }
}
