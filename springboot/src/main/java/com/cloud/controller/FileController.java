package com.cloud.controller;

import com.cloud.model.FileRecord;
import com.cloud.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    // 文件上传
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
        String username = authentication.getName();
        return fileService.uploadFile(file, username);
    }

    // 获取文件列表
    @GetMapping("/list")
    public List<FileRecord> listFiles(Authentication authentication) {
        String username = authentication.getName();
        return fileService.getFilesByUsername(username);
    }

    // 文件下载
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, Authentication authentication) {
        String username = authentication.getName();
        return fileService.downloadFile(fileId, username);
    }

    // 文件删除
    @DeleteMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Long fileId, Authentication authentication) {
        String username = authentication.getName();
        return fileService.deleteFile(fileId, username);
    }

    // 文件预览
    @GetMapping("/preview/{fileId}")
    public ResponseEntity<Resource> previewFile(@PathVariable Long fileId, Authentication authentication) {
        // If no authentication, return unauthorized
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();
        return fileService.previewFile(fileId, username);
    }
}
