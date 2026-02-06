package com.remu.global.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum S3Directory {
    PROFILE("profiles"),
    STAR("stars"),
    TEST("test"); // 테스트용 폴더

    private final String directory;
}
