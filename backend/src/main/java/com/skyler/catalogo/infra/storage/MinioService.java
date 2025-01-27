package com.skyler.catalogo.infra.storage;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;


@Service
public class MinioService implements ApplicationListener<ApplicationReadyEvent> {

    private final MinioClient minioClient;
    private final String bucket = "catalogosk";
    private final String minioUrl = "https://s3.skyler.com.br";

    public MinioService() {
        this.minioClient =
                MinioClient.builder()
                .endpoint(this.minioUrl)
                .credentials("vQ97tRxPkrkslrk6jkrR","00pIhKHvk0pblr2k1gL26vPUSx7bZeyqnA5RgB4M")
                .build();;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        String object = "eu.png";
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(object)
                            .build()
            );
            this.postBase64();
            this.printFiles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void postBase64() throws Exception{
        String file = Base64StringTest.base64;
        byte[] decoded = Base64.getDecoder().decode(file);
        InputStream inputStream = new java.io.ByteArrayInputStream(decoded);
        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(bucket)
                        .object("loja/Nome.png")
                        .stream(inputStream, decoded.length,-1)
                        .build()
        );
    }

    public void printFiles() throws Exception{
        Iterable<Result<Item>> objects = minioClient.listObjects(
                ListObjectsArgs
                        .builder()
                        .bucket(bucket)
                        .prefix("loja/")
                        .build()
        );
        for (Result<Item> result: objects) {
            Item item = result.get();
            if (!item.isDir()) {
                System.out.println("Arquivo: " + item.objectName());


                // Gerar a URL pública do arquivo
                String fileUrl = this.minioUrl + "/" + bucket + "/" + item.objectName();

                // Imprimir a URL do arquivo
                System.out.println("URL do arquivo: " + fileUrl.toString());
            }
        }
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
