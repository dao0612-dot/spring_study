package com.study.board.file;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

// 업로드 파일을 디스크(설정의 file.upload-dir)에 저장
@Component
public class FileStore {
    private final String uploadDir;

    public FileStore(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    // 파일을 업로드 폴더에 새 이름(UUID + 확장자)으로 저장하고 정보를 돌려준다
    public StoredFile store(MultipartFile file) {
        String originalName = file.getOriginalFilename();

        String ext = "";
        int dot = originalName.lastIndexOf(".");
        if (dot != -1) {
            ext = originalName.substring(dot);              // ".pdf" (점 포함)
        }

        String saveName = UUID.randomUUID() + ext;

        Path dir = Path.of(uploadDir);
        try{
            Files.createDirectories(dir);                        // 폴더가 없으면 만듦(있으면 그냥 지나감)
            file.transferTo(dir.resolve(saveName));              // 디스크에 씀
        } catch (IOException e) {
            throw new UncheckedIOException("파일 저장 실패: " + saveName, e);
        }

        return new StoredFile(originalName, saveName, file.getSize());
    }

}
