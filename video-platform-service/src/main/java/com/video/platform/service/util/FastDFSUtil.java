package com.video.platform.service.util;


import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.video.platform.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;


@Component
public class FastDFSUtil {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public String getFileType(MultipartFile file){
        if(file == null){
            throw new ConditionException("File error");
        }
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index+1);
    }

    // Upload
    public String uploadCommonFile(MultipartFile file) throws Exception {
        Set<MetaData> metaDataSet = new HashSet<>();
        String fileType = this.getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile( file.getInputStream(),
                                                                file.getSize(),
                                                                fileType,
                                                                metaDataSet);
        return storePath.getPath();
    }

    // delete
    public void deleteFile(String filePath){

        fastFileStorageClient.deleteFile(filePath);
    }


}
