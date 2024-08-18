package rs.ac.uns.ftn.service;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private MinioClient minioClient;

    public void createBucketIfNotExists(String bucketName) {
        try {
            // Check if the bucket exists
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                // Create the bucket if it does not exist
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(String productCode, MultipartFile file) throws IOException {
        String bucketName = getProductBucketName(productCode);
        createBucketIfNotExists(bucketName);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(file.getOriginalFilename())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IOException("Error occurred while uploading file", e);
        }
    }

    public InputStream downloadFile(String productCode, String fileName) throws IOException {
        String bucketName = getProductBucketName(productCode);
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (MinioException e) {
            throw new IOException("Error occurred while downloading file", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> listFiles(String productCode) throws IOException {
        String bucketName = getProductBucketName(productCode);
        List<String> fileNames = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            for (Result<Item> result : results) {
                fileNames.add(result.get().objectName());
            }
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new IOException("Error occurred while listing files", e);
        }
        return fileNames;
    }

    private String getProductBucketName(String productCode) {
        return "product-" + productCode.toLowerCase();
    }


}
