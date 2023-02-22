package com.liuyun.github.service;

import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.upload.UploadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UploadService {

    @Autowired
    TusFileUploadService tusFileUploadService;

    private String tempDir = "/home/liuyun/Downloads/temp/";
    private String path = "/home/liuyun/Downloads/test/";

    public void complete(String uri) {
        try {
            UploadInfo uploadInfo = tusFileUploadService.getUploadInfo(uri);
            String path1 = path + "uploads/" + uploadInfo.getId();
            System.out.println(path1);
            if (uploadInfo != null && !uploadInfo.isUploadInProgress()) {
                try (InputStream is = tusFileUploadService.getUploadedBytes(uri)) {
                    final Path temp = Paths.get(tempDir);
                    if (!temp.toFile().exists()) {
                        Files.createDirectory(temp);
                    }
                    final Path filePath = temp.resolve(uploadInfo.getFileName());
                    Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
