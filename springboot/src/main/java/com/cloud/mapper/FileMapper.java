package com.cloud.mapper;

import com.cloud.model.FileRecord;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface FileMapper {
    // 插入文件记录
    int insertFile(FileRecord file);

    // 根据用户 ID 获取文件列表
    List<FileRecord> getFilesByUserId(Long userId);

    // 根据文件 ID 删除文件
    int deleteFile(Long id);

    // 如果需要，您可以添加更多方法
}

