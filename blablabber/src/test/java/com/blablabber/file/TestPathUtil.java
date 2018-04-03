package com.blablabber.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class TestPathUtil {

    /**
     * Returns a mock path that returns the sourceDir when toString is called.
     */
    public static Path getMockPath(final String sourceDir) {
        final Path mock = mock(Path.class);
        doReturn(sourceDir).when(mock).toString();
        return mock;
    }

    public static String copyResourceToDisk(String resource) throws IOException {
        InputStream is = TestPathUtil.class.getResourceAsStream(resource);
        Path target = File.createTempFile("pmd-analysis", null).toPath();
        Files.copy(is, target, REPLACE_EXISTING);
        return target.toString();
    }

}
