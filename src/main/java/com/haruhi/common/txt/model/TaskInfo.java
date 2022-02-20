package com.haruhi.common.txt.model;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author cppno1
 */
public class TaskInfo {
    private File sourceFile;
    private File targetFile;
    private File tempDirectory;
    private File outDirectory;
    private Integer lineCount;
    private Charset charset;

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Integer getLineCount() {
        return lineCount;
    }

    public void setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
    }

    public TaskInfo() {
    }

    public TaskInfo(File sourceFile, File targetFile, File tempDirectory, File outDirectory) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
        this.tempDirectory = tempDirectory;
        this.outDirectory = outDirectory;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public File getTempDirectory() {
        return tempDirectory;
    }

    public void setTempDirectory(File tempDirectory) {
        this.tempDirectory = tempDirectory;
    }

    public File getOutDirectory() {
        return outDirectory;
    }

    public void setOutDirectory(File outDirectory) {
        this.outDirectory = outDirectory;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "sourceFile=" + sourceFile +
                ", targetFile=" + targetFile +
                ", tempDirectory=" + tempDirectory +
                ", outDirectory=" + outDirectory +
                '}';
    }
}
