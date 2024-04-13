package com.example.spring.demo.spring.integration.app.clients;

import com.example.spring.demo.spring.integration.app.model.EntityModel;
import com.example.spring.demo.spring.integration.app.model.UpsertEntityRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OkHttpClientSender {

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    @Value("${app.integration.base-url}")
    private String baseUrl;

    @SneakyThrows
    public String uploadFile(MultipartFile file) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",
                        file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file.getBytes())
                );

        Request request = new Request.Builder()
                .url(baseUrl + "/api/v1/file/upload")
                .header("Content-Type", "multipart/form-data")
                .post(builder.build())
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Error while uploading file");
                return "Error!";
            }
            return new String(response.body().bytes());
        }
    }

    public Resource downloadFile(String filename) {
        Request request = new Request.Builder()
                .url(baseUrl + "/api/v1/file/download" + filename)
                .header("Accept", "application/octet-stream")
                .get()
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Error downloading file");
                return null;
            }
            return new ByteArrayResource(response.body().bytes());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EntityModel> getEntityList() {
        Request request = new Request.Builder()
                .url(baseUrl + "/api/v1/entity")
                .build();
        return processResponses(request, new TypeReference<>() {
        });
    }

    public EntityModel getEntityByName(String name) {
        Request request = new Request.Builder()
                .url(baseUrl + "/api/v1/entity/" + name)
                .build();
        return processResponses(request, new TypeReference<>() {
        });
    }

    @SneakyThrows
    public EntityModel createEntity(UpsertEntityRequest request) {
        MediaType JSON = MediaType.get("application/json;charset=utf-8");
        String requestBody = objectMapper.writeValueAsString(request);

        RequestBody body = RequestBody.create(requestBody, JSON);

        Request httpRequest = new Request.Builder()
                .url(baseUrl + "/api/v1/entity/")
                .post(body)
                .build();
        return processResponses(httpRequest, new TypeReference<>() {
        });
    }

    @SneakyThrows
    public EntityModel updateEntity(UUID id, UpsertEntityRequest request) {
        MediaType JSON = MediaType.get("application/json;charset=utf-8");
        String requestBody = objectMapper.writeValueAsString(request);

        RequestBody body = RequestBody.create(requestBody, JSON);

        Request httpRequest = new Request.Builder()
                .url(baseUrl + "/api/v1/entity/" + id)
                .put(body)
                .build();
        return processResponses(httpRequest, new TypeReference<>() {
        });
    }

    @SneakyThrows
    public void deleteEntityById(UUID id) {
        Request request = new Request.Builder()
                .url(baseUrl + "/api/v1/entity/" + id)
                .delete()
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected response code: " + response);
            }
        }
    }

    @SneakyThrows
    private <T> T processResponses(Request request, TypeReference<T> typeReference) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected response code: " + response);
            }
            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                String stringBody = responseBody.string();
                return objectMapper.readValue(stringBody, typeReference);
            } else {
                throw new RuntimeException("Response body is empty");
            }
        }
    }


}
