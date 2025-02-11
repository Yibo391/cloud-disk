package com.cloud.mapper;

import com.cloud.model.FileRecord;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface FileMapper {
    // Insert file record
    int insertFile(FileRecord file);

    // Get file list by user ID
    List<FileRecord> getFilesByUserId(Long userId);

    // Delete file by file ID
    int deleteFile(Long id);

    // Get file information by file ID
    FileRecord getFileById(Long id);
}

