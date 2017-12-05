package com.blablabber.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileOperations.class);

    public Path saveFile(Path directory, String fileName, byte[] bytes) {
        try {
            if (bytes == null) {
                return directory;
            }
            LOGGER.trace("Saving file with content \n{}", new String(bytes));
            Path tempFile = Files.createFile(directory.resolve(fileName));
            Files.write(tempFile, bytes);
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path createTempDir(String prefix) {
        try {
            return Files.createTempDirectory(prefix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path createDir(final Path baseDir, final String s) {
        try {
            return Files.createDirectory(baseDir.resolve(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
