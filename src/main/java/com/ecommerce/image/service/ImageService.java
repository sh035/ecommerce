package com.ecommerce.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ecommerce.image.domain.entity.Image;
import com.ecommerce.image.domain.enums.ImageType;
import com.ecommerce.image.exception.DeleteImageFailException;
import com.ecommerce.image.exception.UploadImageFailException;
import com.ecommerce.image.repository.ImageRepository;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Client s3Client;
    private final ImageRepository imageRepository;

    private static final String IMAGE_NAME = "imageName";
    private static final String IMAGE_URL = "imageUrl";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<Image> getImages(List<MultipartFile> files) {
        List<Image> images = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            Map<String, String> s3Image = s3Upload(files.get(i));

            Image image = Image.builder()
                .imageName(s3Image.get(IMAGE_NAME))
                .imageUrl(s3Image.get(IMAGE_URL))
                .imageType(i == 0 ? ImageType.THUMBNAIL : ImageType.NORMAL)
                .build();
            images.add(image);
        }

        return images;
    }

    public Map<String, String> s3Upload(MultipartFile file) {
        Map<String, String> images = new HashMap<>();

            String fileName = UUID.randomUUID() + file.getOriginalFilename();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(
                    new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                images.put(IMAGE_NAME, fileName);
                images.put(IMAGE_URL, s3Client.getUrl(bucket, fileName).toString());
            } catch (IOException e) {
                throw new UploadImageFailException();
            }
        return images;
    }


    public void deleteFile(String filename) {
        try {
            s3Client.deleteObject(bucket, filename);
        } catch (AmazonS3Exception e) {
            throw new DeleteImageFailException();
        }

        Image image = imageRepository.findByImageName(filename)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이미지입니다."));

        image.delete(LocalDateTime.now());

        imageRepository.save(image);
    }
}
