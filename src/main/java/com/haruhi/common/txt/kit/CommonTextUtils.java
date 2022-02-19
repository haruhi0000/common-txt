package com.haruhi.common.txt.kit;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * @author cppno1
 */
public class CommonTextUtils {

    public static Integer getLineCount(File sourceFile) throws IOException {
        FileReader in = new FileReader(sourceFile);
        LineNumberReader reader = new LineNumberReader(in);
        long skipNumber = reader.skip(Long.MAX_VALUE);
        System.out.println("skipNumber: " + skipNumber);
        int lines = reader.getLineNumber() + 1;
        reader.close();
        return lines;
    }
}
