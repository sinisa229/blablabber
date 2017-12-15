package com.blablabber.file;

import java.nio.file.Path;

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

}
