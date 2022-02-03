package de.aggromc.confighubhost.utils;

import com.amazonaws.services.s3.*;
import com.amazonaws.client.builder.*;
import com.amazonaws.auth.*;
import java.io.*;
import com.amazonaws.services.s3.model.*;

public class S3Utils
{
    private final AmazonS3 s3;
    
    public S3Utils() {
        final AWSCredentialsProvider doCred = (AWSCredentialsProvider)new AWSStaticCredentialsProvider((AWSCredentials)new BasicAWSCredentials("RZKHZ4Q6UISD2J7JSJCT", "zrndKYFo1KUX4F3SJ5A1sY/0taSZ0dqrOcalR+xesfo"));
        this.s3 = (AmazonS3)((AmazonS3ClientBuilder)((AmazonS3ClientBuilder)AmazonS3ClientBuilder.standard().withCredentials(doCred)).withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://fra1.digitaloceanspaces.com", "fra1"))).build();
    }
    
    public boolean uploadFile(final String uID, final String fname, final String mimetype, final InputStream readInput) throws Exception {
        final String key = "images/" + uID + "/" + fname;
        final ObjectMetadata meta = new ObjectMetadata();
        long nread = 0L;
        final byte[] buf = new byte[8192];
        int n;
        while ((n = readInput.read(buf)) > 0) {
            nread += n;
            if (nread > 20971520L) {
                readInput.close();
                return false;
            }
        }
        meta.setContentLength((long)readInput.available());
        meta.setContentType(mimetype);
        this.s3.putObject(new PutObjectRequest("cfghostcdn-private-no-public-access", key, readInput, meta).withCannedAcl(CannedAccessControlList.PublicRead));
        return true;
    }
    
    public boolean fileExists(final String outPath) {
        return this.s3.doesObjectExist("cfghostcdn-private-no-public-access", outPath);
    }
}
