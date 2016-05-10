package edu.upenn.cis550.storage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class S3API {

    public static void main(String[] args) throws IOException     {
        String existingBucketName  = "datalakers.docs"; 
        String keyName             = "123";
        String filePath            = "C:\\Users\\Jitesh\\Desktop\\CurioSity.jpg";   
        
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());        

        InputStream bodyStream = new FileInputStream(new File(filePath));
		s3Client.putObject(new PutObjectRequest(existingBucketName, keyName, bodyStream, null));
    }

}