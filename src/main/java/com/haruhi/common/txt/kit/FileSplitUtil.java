package com.haruhi.common.txt.kit;

import com.haruhi.common.txt.app.Context;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * @author cppno1
 */
public class FileSplitUtil extends Thread {

    public FileSplitUtil() {
        this.setName("FileSplitUtil");
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
     * 6737
     * 5235
     */
    public void splitFile() throws IOException {
        int bufferSize = 102400;
        // 创建临时文件夹
        FileUtils.forceMkdir(Context.taskInfo.getTempDirectory());
        // 删除临时文件
        FileUtils.cleanDirectory(Context.taskInfo.getTempDirectory());
        // 临时文件数量
        int tempFileCount = (int) (Context.taskInfo.getSourceFile().length() / (50 * 1024 * 1024));
        if (tempFileCount < 1) {
            tempFileCount = 1;
        }
        // 新建文件输出流
        BufferedOutputStream[] bufferedWriters = new BufferedOutputStream[tempFileCount];
        long[] writeCount = new long[tempFileCount];
        for (int i = 0; i < tempFileCount; i++) {
            File newFile = new File(Context.taskInfo.getTempDirectory() + File.separator + i + ".txt");
            bufferedWriters[i] = new BufferedOutputStream(new FileOutputStream(newFile), bufferSize);
            newFile.deleteOnExit();
        }
        // 读源文件
        BufferedInputStream bufferedReader = new BufferedInputStream(new FileInputStream(Context.taskInfo.getSourceFile()), bufferSize);
        int b;
        byte[] buffer = new byte[1024 * 10];
        int i = 0;
        int hash = 1;
        long finished = 0;
        while (true) {
            checkWaitSign();
            b = bufferedReader.read();

            if(b == '\n'||b == '\r') {
                if(i != 0) {
                    int index = Math.abs(hash % tempFileCount);
                    BufferedOutputStream bufferedWriter = bufferedWriters[index];
                    bufferedWriter.write(buffer,0, i);
                    bufferedWriter.write('\n');
                    writeCount[index] = writeCount[index] + 1;
                    finished = finished + i;
                    Context.splitTaskProgress.setFinishedSize(finished);
                }
                hash = 1;
                i = 0;
            } else if(b == -1) {
                int index = hash % tempFileCount;
                BufferedOutputStream bufferedWriter = bufferedWriters[Math.abs(index)];
                bufferedWriter.write(buffer,0, i);
                bufferedWriter.write('\n');
                writeCount[index] = writeCount[index] + 1;
                finished = finished + i;
                Context.splitTaskProgress.setFinishedSize(finished);
                break;
            } else {
                buffer[i] = (byte) b;
                hash = 31 * hash + b;
                i = i + 1;
            }
        }
        for (int j = 0; j < tempFileCount; j++) {
            bufferedWriters[j].flush();
            bufferedWriters[j].close();
            Context.MAX_LINE_COUNT = (int) Math.max(Context.MAX_LINE_COUNT, writeCount[j]);
        }
        Context.MAX_LINE_COUNT = (int) (finished / tempFileCount) ;
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
