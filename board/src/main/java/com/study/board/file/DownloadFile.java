package com.study.board.file;

import org.springframework.core.io.Resource;

// 내려받기에 필요한 것: 브라우저에 줄 원래 이름 + 디스크 파일(Resource)
public record DownloadFile(String originalName, Resource resource) {
}
