package com.haruhi.common.txt.kit;

import com.haruhi.common.txt.concurrent.task.Context;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * @author 61711
 */
public class FileSplitUtil extends Thread {

    private final BufferedWriter[] bufferedWriters;

    /**
     * 临时文件数量
     */
    private long tempFileCount;

    private long finishSize = 0L;

    public FileSplitUtil() throws IOException {
        this.setName("FileSplitUtil");
        // 创建临时文件夹
        FileUtils.forceMkdir(Context.taskInfo.getTempDirectory());
        // 删除临时文件
        FileUtils.cleanDirectory(Context.taskInfo.getTempDirectory());

        tempFileCount = Context.taskInfo.getSourceFile().length() / (50 * 1024 * 1024);
        if (tempFileCount < 1) {
            tempFileCount = 1;
        }
        // 新建文件输出流
        bufferedWriters = new BufferedWriter[(int) tempFileCount];
        for (int i = 0; i < tempFileCount; i++) {
            File newFile = new File(Context.taskInfo.getTempDirectory() + File.separator + i + ".txt");
            bufferedWriters[i] = new BufferedWriter(new FileWriter(newFile));
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
        BufferedReader bufferedReader = new BufferedReader(new FileReader(Context.taskInfo.getSourceFile()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            checkWaitSign();
            // 哈希分组运算，把字符串写到指定的文件
            BufferedWriter bufferedWriter = bufferedWriters[(int) Math.abs(line.hashCode() % tempFileCount)];
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            finishSize = finishSize + 1;
            Context.splitTaskProgress.setFinishedSize(finishSize);
        }
        for (BufferedWriter writer : bufferedWriters) {
            writer.close();
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
