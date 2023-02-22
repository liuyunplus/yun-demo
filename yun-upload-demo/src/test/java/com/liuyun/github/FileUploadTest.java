package com.liuyun.github;

import com.alibaba.fastjson2.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUploadTest {

    @Autowired
    private RestTemplate restTemplate;

    public static final String fileLocal = "01.png";

    @Test
    public void testCreateTaskId() throws Exception {
        String url = "http://127.0.0.1:9090/createTask";
        File file = new File(fileLocal);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap();
        param.add("fileName", fileLocal);
        param.add("fileMd5", DigestUtils.md5DigestAsHex(new FileInputStream(file)));
        param.add("fileSize", file.length());
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(param);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        System.out.println(responseEntity.getBody());
    }

    String s = "{\"id\":\"681aff74-ee43-4d2d-9488-7568854315c7\",\"sliceNum\":57,\"size\":1024,\"fileMd5\":\"55b1bfaa8360f333082956790a10ca8f\",\"fileName\":\"01.png\",\"fileSize\":58185}\n";

    @Test
    public void testUploadFile() throws Exception {
        String url = "http://127.0.0.1:9090/uploadFile";
        JSONObject jsonObject = JSONObject.parseObject(s);
        String id = jsonObject.getString("id");
        Integer sliceNum = jsonObject.getInteger("sliceNum");
        Integer size = jsonObject.getInteger("size");
        sliceFile(new File(fileLocal), size, (no, bytes) -> {
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("taskId", id);
            param.add("md5", DigestUtils.md5DigestAsHex(bytes));
            param.add("no", no);
            File file = new File(id + ".tmp." + no);
            try {
                java.nio.file.Files.write(Paths.get(file.toURI()), bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileSystemResource resource = new FileSystemResource(file);
            param.add("file", resource);
            sendFile(url, param);
        });
    }

    @Test
    public void mergeFile() {
        JSONObject jsonObject = JSONObject.parseObject(s);
        String id = jsonObject.getString("id");
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("taskId", id);
        sendFile("http://127.0.0.1:9090/mergeFile", param);
    }

    public void sendFile(String url, MultiValueMap<String, Object> param) {
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(param);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        System.out.println(responseEntity.getBody());
    }

    public static void sliceFile(File file, int size, BiConsumer<Long, byte[]> consumer) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        long length = file.length();
        long count = length / size;
        if (file.length() % count != 0) {
            count++;
        }

        long sum = 0;
        for (long i = 0; i < count; i++) {
            try {
                byte[] bytes;
                if (i + 1 == count) {
                    bytes = new byte[(int) (length - sum)];
                    randomAccessFile.read(bytes, 0, bytes.length);
                } else {
                    bytes = new byte[size];
                    sum += size;
                    randomAccessFile.read(bytes, 0, size);
                    randomAccessFile.seek(sum);
                }
                consumer.accept(i, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
