package com.skyler.catalogo.infra.storage;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import jakarta.validation.constraints.Min;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Service
public class MinioService implements ApplicationListener<ApplicationReadyEvent> {

    private final MinioClient authenticatedMinioClient;
    private final String bucket = "catalogosk";
    private final String minioUrl = "https://s3.skyler.com.br";

    public MinioService() {
        this.authenticatedMinioClient =
                MinioClient.builder()
                .endpoint(this.minioUrl)
                .credentials("vQ97tRxPkrkslrk6jkrR","00pIhKHvk0pblr2k1gL26vPUSx7bZeyqnA5RgB4M")
                .build();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        String object = "eu.png";
        try {
            authenticatedMinioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(object)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void postBase64(String base64, String fileNameWithExtension, String pathFromBucketStartWithoutPointAndSlash) throws Exception{
        byte[] decoded = Base64.getDecoder().decode(base64);
        InputStream inputStream = new java.io.ByteArrayInputStream(decoded);
        authenticatedMinioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(bucket)
                        .object(pathFromBucketStartWithoutPointAndSlash + fileNameWithExtension)
                        .stream(inputStream, decoded.length,-1)
                        .build()
        );
    }

    public List<String> getFileUrlsFromPath(String pathFromBucketStartWithoutPointAndSlash) throws Exception{
        List<String> output = new ArrayList<>();
        Iterable<Result<Item>> objects = authenticatedMinioClient.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(bucket)
                        .prefix(pathFromBucketStartWithoutPointAndSlash)
                        .build()
        );
        for (Result<Item> result: objects) {
            Item item = result.get();
            if (!item.isDir()) {
                System.out.println("Arquivo: " + item.objectName());


                // Gerar a URL pública do arquivo
                String fileUrl = this.minioUrl + "/" + bucket + "/" + item.objectName();
                output.add(fileUrl);

                // Imprimir a URL do arquivo
                System.out.println("URL do arquivo: " + fileUrl.toString());
            }
        }
        return output;
    }

    public String encodeToBase64(InputStream inputStream) throws Exception {
        // Usar ByteArrayOutputStream para armazenar o conteúdo do arquivo
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Ler o arquivo do InputStream e escrever no ByteArrayOutputStream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        // Codificar em Base64
        byte[] encoded = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
        return new String(encoded);
    }
}
