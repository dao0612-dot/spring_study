package com.study.skillup.common.file;


import com.study.skillup.common.exception.ErrorCode;
import com.study.skillup.common.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

// 업로드 파일을 디스크(설정의 file.upload-dir)에 저장
@Slf4j
@Component
public class FileStore {
    // 허용 확장자 목록(소문자, 점 없이)
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg","jpeg","png","gif","pdf","txt","hwp","docx","xlsx","pptx");
    private final String uploadDir;

    public FileStore(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    // 파일을 업로드 폴더에 새 이름(UUID + 확장자)으로 저장하고 정보를 돌려준다
    // 허용 목록에 없는 확장자면 InvalidValueException
    public StoredFile store(MultipartFile file) {
        String originalName = file.getOriginalFilename();

        String ext = "";
        int dot = originalName.lastIndexOf(".");
        if (dot != -1) {
            ext = originalName.substring(dot);              // ".pdf" (점 포함)
        }

        if (!ALLOWED_EXTENSIONS.contains(ext.replace(".","").toLowerCase())) {
            throw new InvalidValueException(ErrorCode.FILE_TYPE_NOT_ALLOWED, "파일 업로드: 형식 불일치 ext="+ext);
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

    // 저장한 파일을 지운다 (DB 작업 실패 시 정리용)
    // 지우다 실패해도 원래 예외를 덮지 않도록 여기서 로그만 남긴다
    public void delete(String saveName){
        try {
            Files.deleteIfExists(Path.of(uploadDir).resolve(saveName));
        } catch (IOException e) {
            log.warn("파일 삭제 실패: {}", saveName, e);
        }
    }


    // 저장 이름으로 디스크의 파일을 찾아 돌려준다 (내려받기용)
    // 파일이 없거나 읽을 수 없으면 null
    public Resource load(String saveName){
        Resource resource = new FileSystemResource(Path.of(uploadDir).resolve(saveName));

        if (!resource.exists() || !resource.isReadable()){
            return null;
        }
        return resource;
    }
}
