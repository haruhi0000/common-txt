package com.haruhi.common.txt.kit;

import com.haruhi.common.txt.app.Context;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * @author cppno1
 */
public class FileSplitUtil extends Thread {

    private Log log = LogFactory.getLog(FileSplitUtil.class);
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
        int tempFileCount = (int) (Context.taskInfo.getSourceFile().length() / (50 * 1024 * 1024));;
        if (tempFileCount < 1) {
            tempFileCount = 1;
        }
        // 新建文件输出流
        BufferedOutputStream[] bufferedWriters = new BufferedOutputStream[(int) tempFileCount];
        for (int i = 0; i < tempFileCount; i++) {
            File newFile = new File(Context.taskInfo.getTempDirectory() + File.separator + i + ".txt");
            bufferedWriters[i] = new BufferedOutputStream(new FileOutputStream(newFile), bufferSize);
            //newFile.deleteOnExit();
        }
        // 读源文件
        BufferedInputStream bufferedReader = new BufferedInputStream(new FileInputStream(Context.taskInfo.getSourceFile()), bufferSize);
        int b;
        byte[] buffer = new byte[1024 * 10];
        int i = 0;
        int hash = 1;
        while (true) {
            b = bufferedReader.read();
            if(b == '\n'||b == '\r') {
                if(i != 0) {
                    int index = hash % tempFileCount;
                    BufferedOutputStream bufferedWriter = bufferedWriters[Math.abs(index)];
                    bufferedWriter.write(buffer,0,i);
                    bufferedWriter.write('\n');
                }
                hash = 1;
                i = 0;
            } else if(b == -1) {
                int index = hash % tempFileCount;
                BufferedOutputStream bufferedWriter = bufferedWriters[Math.abs(index)];
                bufferedWriter.write(buffer,0,1);
                bufferedWriter.write('\n');
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
        }

        Context.splitTaskProgress.setFinished(true);
    }
    /**
     * 分割文件
     */
    public void splitFile2() throws IOException {
        // 创建临时文件夹
        FileUtils.forceMkdir(Context.taskInfo.getTempDirectory());
        // 删除临时文件
        FileUtils.cleanDirectory(Context.taskInfo.getTempDirectory());

        /*
          临时文件数量
         */
        int tempFileCount = (int) (Context.taskInfo.getSourceFile().length() / (50 * 1024 * 1024));
        if (tempFileCount < 1) {
            tempFileCount = 1;
        }
        // 新建文件输出流
        BufferedWriter[] bufferedWriters = new BufferedWriter[(int) tempFileCount];
        for (int i = 0; i < tempFileCount; i++) {
            File newFile = new File(Context.taskInfo.getTempDirectory() + File.separator + i + ".txt");
            bufferedWriters[i] = new BufferedWriter(new FileWriter(newFile, Context.taskInfo.getCharset()));
            newFile.deleteOnExit();
        }
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
        }
        for (int i = 0; i < tempFileCount; i++) {
            bufferedWriters[i].close();
            Context.MAX_LINE_COUNT = Math.max(Context.MAX_LINE_COUNT, lineNumbers[i]);
        }

        Context.splitTaskProgress.setFinished(true);
    }
    public static int byteHashCode(byte[] a, int off, int len) {
        if (a == null)
            return 0;

        int result = 1;
        for (int i = off; i < len; i++) {
            result = 31 * result + a[i];
        }
        return result;
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
