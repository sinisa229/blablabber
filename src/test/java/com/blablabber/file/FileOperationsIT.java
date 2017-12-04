package com.blablabber.file;

import org.junit.Test;

import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.*;

public class FileOperationsIT {

    @Test
    public void shouldCreateSubdirs() throws Exception {
        List<Path> dirs = new FileOperations().createTempDirs("myPrefix", "subdir1", "subdir2");
        assertThat(dirs.get(0).toString(), endsWith("subdir1"));
        assertThat(dirs.get(0).toString(), endsWith("subdir2"));
    }
}