package br.com.hyper.utils;

import br.com.hyper.constants.BaseUrls;
import br.com.hyper.constants.ErrorCodes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Files;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalFileStorageUtil {



    public static String saveFile(MultipartFile file, String owner, String path, String fileNameOverride) {
        try {
            Path storageBase = Path.of(BaseUrls.BASE_URL, owner, path);

            if (!Files.exists(storageBase)) {
                Files.createDirectories(storageBase);
            }

            String originalName = file.getOriginalFilename();
            String extension = getExtension(originalName);
            String fileName = sanitize(fileNameOverride) + "." + extension;

            Path fullPath = storageBase.resolve(fileName);
            file.transferTo(fullPath.toFile());

            Path publicPath = Path.of(BaseUrls.STORAGE, owner, path, fileName);
            return "/" + publicPath.toString().replace("\\", "/");

        } catch (IOException e) {
            throw new RuntimeException(ErrorCodes.FILE_STORAGE_ERROR.getMessage(), e);
        }
    }

    public static Resource searchFile(String path) {
        try {
            Path filePath = Path.of(BaseUrls.BASE_URL, path);

            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                throw new RuntimeException(ErrorCodes.FILE_STORAGE_ERROR.getMessage());
            }

            return new UrlResource(filePath.toUri());

        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro ao acessar recurso local: " + e.getMessage(), e);
        }
    }

    public static boolean removeFile(String path){
        try {
            Path filePath = Path.of(BaseUrls.BASE_URL, path);

            if (Files.exists(filePath)) {
                return Files.deleteIfExists(filePath);
            }
            return false;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao remover arquivo local: " + e.getMessage(), e);
        }
    }

    private static String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "bin";
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }

    private static String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }
}
