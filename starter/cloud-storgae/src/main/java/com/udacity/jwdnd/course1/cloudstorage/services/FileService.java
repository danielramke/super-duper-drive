package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFilesFromUserId(Integer userid) {
        return fileMapper.getFilesFromUser(userid);
    }

    public boolean isExist(String filename) {
        return fileMapper.getFileByName(filename) != null;
    }

    public void save(MultipartFile file, Integer userid) throws IOException {
        byte[] data = file.getBytes();
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long fileSize = file.getSize();
        fileMapper.save(new File(null, filename, contentType, String.valueOf(fileSize), userid, data));
    }

    public void delete(Integer fileId, Integer userid) {
        fileMapper.delete(fileId, userid);
    }

    public File getFileByName(String filename) {
        return fileMapper.getFileByName(filename);
    }

}
