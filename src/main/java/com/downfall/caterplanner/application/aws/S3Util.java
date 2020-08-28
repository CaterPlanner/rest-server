package com.downfall.caterplanner.application.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class S3Util {


    @Autowired
    private AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String upload(String filePath, MultipartFile file) throws IOException {

        String contentType = file.getContentType().split("/")[0];

        byte[] bytes = file.getBytes();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("Content-Type", contentType);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucket, filePath, byteArrayInputStream, metadata
        );

        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putObjectRequest);


        return s3Client.getUrl(bucket, filePath).toString();
    }

    public void delete(String filePath){
        s3Client.deleteObject(bucket, filePath);
    }



}
