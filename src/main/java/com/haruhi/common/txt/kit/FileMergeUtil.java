package com.haruhi.common.txt.kit;

import com.haruhi.common.txt.app.Context;
import com.haruhi.common.txt.model.ByteWrapper;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author cppno1
 */
public class FileMergeUtil extends Thread {

    private File[] tempDir;

    @Override
    public void run() {
        this.setName("FileMergeUtil");
        tempDir = Context.taskInfo.getTempDirectory().listFiles();
        try {
            mergeFile2();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    /**
     * 合并文件
     */
    public void mergeFile2() throws IOException {

        // 结果文件输出流
        long finished = 0;
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(Context.taskInfo.getTargetFile()));
        int bufferSize = 102400 * 2;
        Set<ByteWrapper> hashSet = new HashSet<>(Context.MAX_LINE_COUNT, 1);
        for (File file : tempDir) {
            System.out.println(file.getName() + "start>>>>>");
            // 读源文件
            BufferedInputStream bufferedReader = new BufferedInputStream(new FileInputStream(file), bufferSize);
            int b;
            byte[] buffer = new byte[1024 * 100];
            int i = 0;

            while (true) {
                checkWaitSign();
                b = bufferedReader.read();
                if(b == '\n'||b == '\r') {
                    if(i != 0) {
                        byte[] value = new byte[i];
                        System.arraycopy(buffer, 0, value, 0, i);
                        hashSet.add(new ByteWrapper(value));
                    }
                    i = 0;
                } else if(b == -1) {
                    break;
                } else {
                    buffer[i] = (byte) b;
                    i = i + 1;
                }
            }
            System.out.println(file.getName() + "start============");
            for (ByteWrapper byteWrapper : hashSet) {
                checkWaitSign();
                bufferedOutputStream.write(byteWrapper.getBytes());
                bufferedOutputStream.write('\n');
                finished = finished + byteWrapper.getBytes().length;
                Context.mergeTaskProgress.setFinishedSize(finished);
            }
            hashSet.clear();
            bufferedReader.close();
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        Context.mergeTaskProgress.setFinishedSize(Context.mergeTaskProgress.getTotalSize());
        Context.mergeTaskProgress.setFinished(true);


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
