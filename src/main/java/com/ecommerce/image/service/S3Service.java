package com.ecommerce.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public List<String> upload(List<MultipartFile> multipartFiles) {
    List<String> imgUrlList = new ArrayList<>();

    if (multipartFiles == null || multipartFiles.isEmpty()) {
      throw new CustomException(ErrorCode.NOT_FOUND_IMAGE);
    }

    for (MultipartFile file : multipartFiles) {
      String fileName = createFileName(file.getOriginalFilename());
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(file.getSize());
      objectMetadata.setContentType(file.getContentType());

      try (InputStream inputStream = file.getInputStream()) {
        s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        imgUrlList.add(s3Client.getUrl(bucket, fileName).toString());
      } catch (IOException e) {
        throw new CustomException(ErrorCode.UPLOAD_ERROR_IMAGE);
      }
    }

    return imgUrlList;
  }

  private String createFileName(String fileName) {
    return UUID.randomUUID().toString().concat(getFileExtension(fileName));
  }

  // 파일 유효성 검사
  private String getFileExtension(String originalFileName) {
    if (originalFileName.isEmpty()) {
      throw new CustomException(ErrorCode.REQUIRED_IMAGE);
    }
    List<String> fileValidate = List.of(".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG");
    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
    if (!fileValidate.contains(fileExtension)) {
      throw new CustomException(ErrorCode.VALID_ERROR_IMAGE);
    }
    return fileExtension;
  }

  public void deleteFile(String originalFileName){
    DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, originalFileName);
    s3Client.deleteObject(deleteObjectRequest);
  }
}
