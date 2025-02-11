package com.cloud.service;

import com.cloud.mapper.FileMapper;
import com.cloud.mapper.UserMapper;
import com.cloud.model.FileRecord;
import com.cloud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserMapper userMapper;

    // 从配置文件中读取文件保存路径
    @Value("${file.upload-dir}")
    private String FILE_BASE_PATH;

    // 上传文件
    public String uploadFile(MultipartFile file, String username) {
        // 获取用户信息
        User user = userMapper.loadUserByUsername(username);
        if (user == null) {
            return "用户不存在";
        }

        // 创建用户文件夹
        String userDirPath = FILE_BASE_PATH + File.separator + user.getId();
        File userDir = new File(userDirPath);
        if (!userDir.exists()) {
            if (!userDir.mkdirs()) {
                System.out.println("无法创建目录：" + userDirPath);
                return "无法创建目录";
            }
        }

        // 保存文件
        String originalFilename = file.getOriginalFilename();
        String filePath = userDirPath + File.separator + originalFilename;
        System.out.println("文件保存路径：" + filePath);
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败";
        }

        // 保存文件记录到数据库
        FileRecord fileRecord = new FileRecord();
        fileRecord.setUserId(user.getId());
        fileRecord.setFileName(originalFilename);
        fileRecord.setFilePath(filePath);
        fileRecord.setFileSize(file.getSize());
        fileRecord.setUploadTime(new Date());
        fileMapper.insertFile(fileRecord);

        return "文件上传成功";
    }

    // 获取用户的文件列表
    public List<FileRecord> getFilesByUsername(String username) {
        User user = userMapper.loadUserByUsername(username);
        if (user == null) {
            return null;
        }
        return fileMapper.getFilesByUserId(user.getId());
    }

    // 下载文件
    public ResponseEntity<Resource> downloadFile(Long fileId, String username) {
        User user = userMapper.loadUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        FileRecord fileRecord = fileMapper.getFileById(fileId);
        if (fileRecord == null || !fileRecord.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        File file = new File(fileRecord.getFilePath());
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileRecord.getFileName() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    // 删除文件
    public String deleteFile(Long fileId, String username) {
        User user = userMapper.loadUserByUsername(username);
        if (user == null) {
            return "用户不存在";
        }

        FileRecord fileRecord = fileMapper.getFileById(fileId);
        if (fileRecord == null || !fileRecord.getUserId().equals(user.getId())) {
            return "无权删除该文件";
        }

        // 删除文件
        File file = new File(fileRecord.getFilePath());
        if (file.exists()) {
            file.delete();
        }

        // 删除数据库记录
        fileMapper.deleteFile(fileId);

        return "文件删除成功";
    }

    // 预览文件
    public ResponseEntity<Resource> previewFile(Long fileId, String username) {
        try {
            User user = userMapper.loadUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            FileRecord fileRecord = fileMapper.getFileById(fileId);
            if (fileRecord == null || !fileRecord.getUserId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            File file = new File(fileRecord.getFilePath());
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Resource resource = new FileSystemResource(file);
            HttpHeaders headers = new HttpHeaders();
            String extension = getFileExtension(fileRecord.getFileName()).toLowerCase();
            String contentType = switch (extension) {
                case "pdf" -> "application/pdf";
                case "png" -> "image/png";
                case "jpg", "jpeg" -> "image/jpeg";
                case "gif" -> "image/gif";
                default -> "application/octet-stream";
            };
            
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add("Content-Disposition", "inline; filename=\"" + fileRecord.getFileName() + "\"");
            // Remove X-Frame-Options header for preview
            headers.remove("X-Frame-Options");
            // Add cache control headers
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return fileName.substring(lastIndexOf + 1);
    }
}
