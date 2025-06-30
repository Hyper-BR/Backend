package br.com.hyper.utils;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.exceptions.AwsConnectionException;
import br.com.hyper.exceptions.InvalidCollectionDataException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

@Component
public class AmazonBucketS3 {

    private static final String BUCKET_NAME = "hyper-tracks";
    private static final String SECRET_ID = "Access_Key/Hyper";

    public void uploadArtistTrack(String path, MultipartFile file) {
        try {
            AwsBasicCredentials awsCredentials = getAwsCredentials();
            S3Client s3Client = createS3Client(awsCredentials);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(path)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            s3Client.close();
        } catch (Exception e) {
            handleException(e, ErrorCodes.AWS_CONNECTION_ERROR);
        }
    }

    public byte[] downloadTrack(String path) {
        try {
            AwsBasicCredentials awsCredentials = getAwsCredentials();
            S3Client s3Client = createS3Client(awsCredentials);

            ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                    .bucket(BUCKET_NAME)
                    .prefix(path)
                    .build();

            List<byte[]> trackBytesList = new ArrayList<>();

            s3Client.listObjectsV2Paginator(listObjectsRequest).contents().forEach(object -> {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(object.key())
                        .build();

                ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getObjectRequest);
                byte[] trackBytes = responseBytes.asByteArray();
                trackBytesList.add(trackBytes);
            });

            s3Client.close();

            return compressBytes(concatenateByteArrays(trackBytesList));
        } catch (Exception e) {
            return handleException(e, ErrorCodes.AWS_CONNECTION_ERROR);
        }
    }

    public String getTrackUrl(String path) {

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hora
        expiration.setTime(expTimeMillis);

        AwsBasicCredentials awsCredentials = getAwsCredentials();
        S3Client s3Client = createS3Client(awsCredentials);

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(BUCKET_NAME)
                .key(path)
                .build();

        URL url = s3Client.utilities().getUrl(getUrlRequest);

        return url.toString();
    }

    private AwsBasicCredentials getAwsCredentials() {
        try {
            SecretsManagerClient secretsManager = SecretsManagerClient.create();
            GetSecretValueResponse secretValueResponse = secretsManager.getSecretValue(GetSecretValueRequest.builder()
                    .secretId(SECRET_ID)
                    .build());
            secretsManager.close();

            JsonNode secretJson = new ObjectMapper().readTree(secretValueResponse.secretString());
            String awsAccessKeyId = secretJson.get("aws_access_key_id").asText();
            String awsSecretAccessKey = secretJson.get("aws_secret_access_key").asText();

            return AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey);
        } catch (Exception e) {
            throw new InvalidCollectionDataException(ErrorCodes.AWS_SECRETS_ERROR,
                    ErrorCodes.AWS_SECRETS_ERROR.getMessage());
        }
    }

    private S3Client createS3Client(AwsBasicCredentials awsCredentials) {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    private byte[] concatenateByteArrays(List<byte[]> byteArrays) {
        int totalLength = byteArrays.stream().mapToInt(arr -> arr.length).sum();
        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] byteArray : byteArrays) {
            System.arraycopy(byteArray, 0, result, currentIndex, byteArray.length);
            currentIndex += byteArray.length;
        }
        return result;
    }

    public static byte[] compressBytes(byte[] input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(input);
        gzip.close();
        return bos.toByteArray();
    }

    private <T> T handleException(Exception e, ErrorCodes errorCode) {
        System.out.println(e);
        throw new AwsConnectionException(errorCode, errorCode.getMessage());
    }
}
