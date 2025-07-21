package br.com.hyper.utils;

import br.com.hyper.constants.BaseUrls;
import br.com.hyper.constants.ErrorCodes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalFileStorageUtil {

    private static final Path ROOT_STORAGE = Path.of(System.getProperty("user.dir"), BaseUrls.BASE_URL);

    public static String saveFile(MultipartFile file, String customerId, String path, String fileNameOverride) {
        try {
            Path storageBase = ROOT_STORAGE.resolve(customerId).resolve(path);

            if (!Files.exists(storageBase)) {
                Files.createDirectories(storageBase);
            }

            String originalName = file.getOriginalFilename();
            String extension = getExtension(originalName);
            String fileName = sanitize(fileNameOverride) + "." + extension;

            Path fullPath = storageBase.resolve(fileName);
            file.transferTo(fullPath.toFile());

            Path publicPath = Path.of(BaseUrls.BASE_URL, customerId, path, fileName);
            return "/" + publicPath.toString().replace("\\", "/");

        } catch (IOException e) {
            throw new RuntimeException(ErrorCodes.FILE_STORAGE_ERROR.getMessage(), e);
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
