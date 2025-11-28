package com.line7studio.boulderside.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

	@Value("${cloud.aws.s3.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.s3.credentials.secret-key}")
	private String secretKey;

	@Value("${cloud.aws.s3.region}")
	private String region;

	@Bean
	public AmazonS3 amazonS3() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

		// 버킷명에 .이 포함되지 않는 이상 Virtual-hosted–style가 기본 동작
		return AmazonS3ClientBuilder.standard()
			.withRegion(region)
			.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
			.build();
	}
}
