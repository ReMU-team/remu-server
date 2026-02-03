package com.remu.global.common;

import com.remu.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 테스트용 컨트롤러라 추후 삭제 예정
 */

@RestController
@RequestMapping("/api/test/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    /**
     * 1. 단일 파일 업로드 테스트
     * Postman: POST -> Body -> form-data -> Key: "file" (File 타입) / Key: "folder" (Text 타입)
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public S3Service.S3TotalResponse uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "test") String folder) {

        return s3Service.uploadFile(file, folder);
    }

    /**
     * 2. 다중 파일 업로드 테스트
     * Postman: POST -> Body -> form-data -> Key: "files" (File 타입으로 여러 개 추가)
     */
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<S3Service.S3TotalResponse> uploadFiles(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam(value = "folder", defaultValue = "test") String folder) {

        return s3Service.uploadFiles(files, folder);
    }

    /**
     * 3. 파일 조회 테스트 (기존 DB에 저장된 fileName으로 URL 생성)
     * Postman: GET -> /api/test/s3/view?fileName=test/uuid_filename.png
     */
    @GetMapping("/view")
    public S3Service.S3TotalResponse getFileUrl(@RequestParam("fileName") String fileName) {
        return s3Service.getPresignedViewUrl(fileName);
    }

    /**
     * 4. 파일 삭제 테스트
     */
    @DeleteMapping("/delete")
    public S3Service.S3TotalResponse deleteFile(@RequestParam("fileName") String fileName) {
        return s3Service.deleteFile(fileName);
    }
}