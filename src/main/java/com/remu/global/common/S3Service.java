package com.remu.global.common;

import com.remu.global.apiPayload.code.GeneralErrorCode;
import com.remu.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * @param file       업로드할 파일
     * @param folderName S3 내 저장될 폴더명 (예: "profiles", "stars")
     */

    /**
     * [단일 업로드] 파일을 S3에 업로드하고 정보(파일명, 미리보기 URL)를 반환
     */
    public S3TotalResponse uploadFile(MultipartFile file, String folderName) {
        // 1, 파일이 있는지 체크
        if (file == null || file.isEmpty()) {
            throw new GeneralException(GeneralErrorCode.NOT_FOUND);
        }

        String fileName = generateFileName(file.getOriginalFilename(), folderName);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // S3 파일 전송
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return new S3TotalResponse(fileName, getPresignedUrl(fileName));
        } catch (IOException e) {
            throw new RuntimeException("S3 파일 업로드 실패: " + fileName, e);
        }
    }

    /**
     * [다중 업로드] 파일을 S3에 업로드하고 정보(파일명, 미리보기 URL)를 반환
     */
    public List<S3TotalResponse> uploadFiles(List<MultipartFile> files, String folderName) {
        if (files == null || files.isEmpty()) return Collections.emptyList();

        return files.stream()
                .map(file -> uploadFile(file, folderName))
                .toList();
    }

    /**
     * [조회] DB에 저장된 fileName을 접근 가능한 Presigned URL로 변환
     */
    public S3TotalResponse getPresignedViewUrl(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return null;
        }
        String presignedUrl = getPresignedUrl(fileName);
        return new S3TotalResponse(fileName, presignedUrl);
    }

    /**
     * [다중 조회] DB에 저장된 fileName을 접근 가능한 Presigned URL로 변환
     */
    public List<S3TotalResponse> getPresignedViewUrls(List<String> fileNames) {
        if (fileNames == null || fileNames.isEmpty()) return Collections.emptyList();

        return fileNames.stream()
                .map(this::getPresignedViewUrl)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * [삭제] S3에 저장된 파일을 삭제.
     */
    public S3TotalResponse deleteFile(String fileName) {
        if (fileName == null || fileName.isBlank()) return null;

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            return new S3TotalResponse(fileName, null);
        } catch (Exception e) {
            throw new GeneralException(GeneralErrorCode.BAD_REQUEST);
        }
    }
    
    // 1시간 동안 유효 링크 생성
    public String getPresignedUrl(String fileName) {
        if (fileName == null || fileName.isBlank()) return null;

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1)) // 유효 시간 1시간
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    // S3 저장 경로 및 파일명 생성 (folder/UUID_name)
    private String generateFileName(String originalFilename, String folderName) {
        return String.format("%s/%s_%s", folderName, UUID.randomUUID(), originalFilename);
    }

    // 응답용 레코드
    public record S3TotalResponse(String fileName, String fileUrl) {}
}
