package com.liuyun.github;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusExecutor;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TusUploadTest {

    public static final String token = "124242353";
    public static final String project = "verify-upload";
    public static final String filePath = "/home/liuyun/Downloads/packages/target/dota/ota-output/dota_100.zip";

    public static void main(String[] args) throws IOException, ProtocolException {
        long start = System.currentTimeMillis();
        TusClient client = new TusClient();
        String uploadUrl = "https://ota-verify-upload-dev.na.onecloud.cerenceapi.com/dota/v1/package/" + project + "/upload";
        client.setUploadCreationURL(new URL(uploadUrl));
        client.enableResuming(new TusURLMemoryStore());
        client.setHeaders(new HashMap() {
            {
                this.put("Authorization", "Bearer " + token);
                this.put("x-account-id", "system@cerence.com");
                this.put("x-publish-method", "automatic");
            }
        });

        File file = new File(filePath);
        final TusUpload upload = new TusUpload(file);
        System.out.println("Starting upload...");

        TusExecutor executor = new TusExecutor() {
            @SneakyThrows
            @Override
            protected void makeAttempt() {
                TusUploader uploader = client.resumeOrCreateUpload(upload);
                uploader.setChunkSize(10485760);

                do {
                    long totalBytes = upload.getSize();
                    long bytesUploaded = uploader.getOffset();
                    double progress = (double) bytesUploaded / totalBytes * 100;
                    System.out.printf("Upload at %06.2f%%.\n", progress);
                } while (uploader.uploadChunk() > -1);

                uploader.finish();
                System.out.println("Upload finished.");
                System.out.format("Upload available at: %s\n", uploader.getUploadURL().toString());
                long end = System.currentTimeMillis();
                System.out.println("总耗时(秒)： " + TimeUnit.MILLISECONDS.toSeconds(end - start));
            }
        };
        executor.makeAttempts();
    }

}
