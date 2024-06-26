package edu.uci.ics.crawler4j.examples;

import java.io.File;
import java.io.IOException;

public class Files {
    public static File createTmpDir(String path) throws IOException {
        File dir = File.createTempFile(path, "");
        dir.delete();
        dir.mkdir();
        return dir;
    }
}
