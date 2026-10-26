package com.study.board.file;

// 디스크에 저장한 파일 정보: 원래 이름, 저장 이름, 크기(바이트)
public record StoredFile(String originalName, String saveName, long size) {}
