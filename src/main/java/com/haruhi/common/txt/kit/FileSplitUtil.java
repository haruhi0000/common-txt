package com.haruhi.common.txt.kit;

import com.haruhi.common.txt.app.Context;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Arrays;

/**
 * @author cppno1
 */
public class FileSplitUtil extends Thread {

    private final BufferedOutputStream[] bufferedWriters;

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
        bufferedWriters = new BufferedOutputStream[(int) tempFileCount];
        for (int i = 0; i < tempFileCount; i++) {
            File newFile = new File(Context.taskInfo.getTempDirectory() + File.separator + i + ".txt");
            bufferedWriters[i] = new BufferedOutputStream(new FileOutputStream(newFile));
            //newFile.deleteOnExit();
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
        BufferedInputStream bufferedReader = new BufferedInputStream(new FileInputStream(Context.taskInfo.getSourceFile()));
        byte[] buffer;
        if(Context.taskInfo.getSourceFile().length() < 1024) {
            buffer = new byte[(int) Context.taskInfo.getSourceFile().length()];
        } else {
            buffer = new byte[1024];
        }
        buffer = new byte[1];
        int off = 0;
        int len = buffer.length;
        int readLen;
        int strLen = 0;
        boolean flag = false;
        while ((readLen = bufferedReader.read(buffer, off, len)) != -1) {
            checkWaitSign();
            int strOff = 0;
            for (int i = 0; i < readLen; i++) {
                if (buffer[i] == '\r' || buffer[i] == '\n') {
                    if(flag) {
                        strOff = strOff + 1;
                        finishSize = finishSize + 1;
                        Context.splitTaskProgress.setFinishedSize(finishSize);
                        continue;
                    }
                    byte[] value = Arrays.copyOfRange(buffer, strOff, strOff + strLen);
                    int index = Math.abs(Arrays.hashCode(value) % tempFileCount);
                    BufferedOutputStream bufferedWriter = bufferedWriters[index];
                    bufferedWriter.write(value);
                    bufferedWriter.write('\n');
                    bufferedWriter.flush();
                    finishSize = finishSize + strLen + 1;
                    Context.splitTaskProgress.setFinishedSize(finishSize);
                    strOff = strOff + strLen + 1;
                    strLen = 0;
                    flag = true;
                } else {
                    flag = false;
                    strLen = strLen + 1;
                }
            }
            if(strLen == readLen) {
                byte[] newBuffer = new byte[buffer.length * 2];
                off = buffer.length;
                len = buffer.length;
                System.arraycopy(buffer,0, newBuffer, 0, buffer.length);
                buffer = newBuffer;

            } else if(strOff != readLen) {
                if (readLen - strOff >= 0) {
                    System.arraycopy(buffer, strOff, buffer, 0, readLen - strOff);
                    off = readLen - strOff;
                    len = buffer.length - off;
                }
            } else {
                off = 0;
                len = buffer.length;
            }
        }
        if(!flag) {
            byte[] value = Arrays.copyOfRange(buffer, 0, off);
            int index = Math.abs(Arrays.hashCode(value) % tempFileCount);
            BufferedOutputStream bufferedWriter = bufferedWriters[index];
            bufferedWriter.write(value);
            bufferedWriter.write('\n');
            bufferedWriter.flush();
            finishSize = finishSize + off;
            Context.splitTaskProgress.setFinishedSize(finishSize);
        }
        for (int i = 0; i < tempFileCount; i++) {
            bufferedWriters[i].close();
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
