package com.DeBM.ApiDeBM.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {
    @Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials =
                new BasicAWSCredentials("AKIATQBE4NQ55MTF7NDQ", "Rne5jxGrOAijzRr6vQfxnvZXsBPsKbdK3yWfAExG");
        return AmazonS3ClientBuilder
                .standard()
                .withRegion("eu-west-3")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

    }
}