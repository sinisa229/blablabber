package com.blablabber.file;

import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;

public class FileOperationsIT {

    @Test
    public void shouldCreateSubdirs() throws Exception {
        final FileOperations fileOperations = new FileOperations();
        final Path someDir = fileOperations.createTempDir("someDir");
        Path child = fileOperations.createDir(someDir, "child");
        assertThat(child.toString(), startsWith(someDir.toString()));
        assertThat(child.toString(), endsWith("child"));
    }
}