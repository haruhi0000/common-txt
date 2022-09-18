package com.haruhi.common.txt.kit;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author haruhi0000
 */
public class CommonTextUtils {

    public static Long getLineCount(File sourceFile) throws IOException {
        return Files.lines(Paths.get(sourceFile.getAbsolutePath())).count();
    }
}
