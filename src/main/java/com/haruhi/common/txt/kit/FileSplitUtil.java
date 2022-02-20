package com.haruhi.common.txt.kit;

import com.haruhi.common.txt.app.Context;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * @author cppno1
 */
public class FileSplitUtil extends Thread {

    private final BufferedWriter[] bufferedWriters;

    /**
     * 临时文件数量
     */
    private int tempFileCount;

    private long finishSize = 0L;

    public FileSplitUtil() throws IOException {
        this.setName("FileSplitUtil");
        // 创建临时文件夹
        FileUtils.forceMkdir(Context.taskInfo.getTempDirectory());
        // 删除临时文件
        FileUtils.cleanDirectory(Context.taskInfo.getTempDirectory());

        tempFileCount = (int) (Context.taskInfo.getSourceFile().length() / (50 * 1024 * 1024));
        if (tempFileCount < 1) {
            tempFileCount = 1;
        }
        // 新建文件输出流
        bufferedWriters = new BufferedWriter[(int) tempFileCount];
        for (int i = 0; i < tempFileCount; i++) {
            File newFile = new File(Context.taskInfo.getTempDirectory() + File.separator + i + ".txt");
            bufferedWriters[i] = new BufferedWriter(new FileWriter(newFile, Context.taskInfo.getCharset()));
            newFile.deleteOnExit();
        }
    }

    @Override
    public void run() {
        try {
            splitFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 分割文件
     */
    public void splitFile() throws IOException {
        // 读源文件
        BufferedReader bufferedReader = new BufferedReader(new FileReader(Context.taskInfo.getSourceFile(), Context.taskInfo.getCharset()));
        String line;
        int[] lineNumbers = new int[tempFileCount];

        while ((line = bufferedReader.readLine()) != null) {
            checkWaitSign();
            // 哈希分组运算，把字符串写到指定的文件
            int index = Math.abs(line.hashCode() % tempFileCount);
            BufferedWriter bufferedWriter = bufferedWriters[index];
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            lineNumbers[index] = lineNumbers[index] + 1;
            finishSize = finishSize + 1;
            Context.splitTaskProgress.setFinishedSize(finishSize);
        }
        for (int i = 0; i < tempFileCount; i++) {
            bufferedWriters[i].close();
            Context.MAX_LINE_COUNT = Math.max(Context.MAX_LINE_COUNT, lineNumbers[i]);
        }

        Context.splitTaskProgress.setFinished(true);
    }

    private void checkWaitSign() {
        if (Context.paused) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
