package com.fatepet.petrest.common.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Slf4j
public class S3Config {

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.credentials.access-key}")
    private String accessKey;

    @Value("${aws.credentials.secret-key}")
    private String secretKey;




    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    @Bean
    public String s3BucketName() {
        return bucketName;
    }

    @Bean
    public Region s3Region() {
        return Region.of(region);
    }

//    @Value("${aws.credentials.access-key}")
//    private String accessKey;
//
//    @Value("${aws.credentials.secret-key}")
//    private String secretKey;
//
//    @Value("${aws.s3.region}")
//    private String region;
//
//    @Value("${aws.s3.bucket-name}")
//    private String bucketName;
//
//    @Bean
//    public S3Client s3Client() {
//        S3ClientBuilder builder = S3Client.builder()
//                .region(Region.of(region));
//
//        if (accessKey != null && !accessKey.isEmpty() && secretKey != null && !secretKey.isEmpty()) {
//            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
//            builder.credentialsProvider(StaticCredentialsProvider.create(awsCreds));
//        }
//
//        return builder.build();
//    }
//
//    @Bean
//    public String s3BucketName() {
//        return bucketName;
//    }
//
//    @Bean
//    public Region s3Region() {
//        return Region.of(region);
//    }

}
