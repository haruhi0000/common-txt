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
     */
    public void splitFile() throws IOException {

        // 创建临时文件夹
        FileUtils.forceMkdir(Context.taskInfo.getTempDirectory());
        // 删除临时文件
        FileUtils.cleanDirectory(Context.taskInfo.getTempDirectory());
        // 临时文件数量
        int tempFileCount = (int) (Context.taskInfo.getSourceFile().length() / (50 * 1024 * 1024));;
        if (tempFileCount < 1) {
            tempFileCount = 1;
        }
        log.debug(tempFileCount);
        // 新建文件输出流
        BufferedOutputStream[] bufferedWriters = new BufferedOutputStream[(int) tempFileCount];
        for (int i = 0; i < tempFileCount; i++) {
            File newFile = new File(Context.taskInfo.getTempDirectory() + File.separator + i + ".txt");
            bufferedWriters[i] = new BufferedOutputStream(new FileOutputStream(newFile));
            //newFile.deleteOnExit();
        }
        // 读源文件
        BufferedInputStream bufferedReader = new BufferedInputStream(new FileInputStream(Context.taskInfo.getSourceFile()));
        int b;
        byte[] buffer = new byte[1024];
        int i = 0;
        while (true) {
            b = bufferedReader.read();
            if(b == '\n'||b == '\r') {
                if(i != 0) {
                    int index = byteHashCode(buffer, 0, i) % tempFileCount;
                    BufferedOutputStream bufferedWriter = bufferedWriters[Math.abs(index)];
                    bufferedWriter.write(buffer,0,i);
                    bufferedWriter.write('\n');
                }
                i = 0;
            } else if(b == -1) {
                int index = byteHashCode(buffer, 0, i) % tempFileCount;
                BufferedOutputStream bufferedWriter = bufferedWriters[Math.abs(index)];
                bufferedWriter.write(buffer,0,1);
                bufferedWriter.write('\n');
                break;
            } else {
                buffer[i] = (byte) b;
                i = i + 1;
            }
        }

        for (int j = 0; j < tempFileCount; j++) {
            bufferedWriters[j].flush();
            bufferedWriters[j].close();
        }

        Context.splitTaskProgress.setFinished(true);
    }
    public static int byteHashCode(byte a[], int off, int len) {
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
