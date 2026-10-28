package com.study.skillup.common.dto;

// 응답 형식 고정
public record ResponseDto<T>(String message, T data) {
}
