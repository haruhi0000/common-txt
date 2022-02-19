package com.haruhi.common.txt.kit;

import com.haruhi.common.txt.app.Context;

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
            mergeFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 合并文件
     */
    public void mergeFile() throws IOException {

        // 结果文件输出流
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Context.taskInfo.getTargetFile())));
        long finished = 0;
        int treeSerInitialCapacity;
        treeSerInitialCapacity = (Context.taskInfo.getLineCount() / tempDir.length) * 2;
        Set<String> hashSet = new HashSet<>(treeSerInitialCapacity);
        for (File file : tempDir) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    checkWaitSign();
                    hashSet.add(line);
                }
                for (String str : hashSet) {
                    checkWaitSign();
                    writer.write(str);
                    writer.write('\n');
                    finished = finished + 1;
                    Context.mergeTaskProgress.setFinishedSize(finished);
                }
                bufferedReader.close();
                hashSet.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Context.mergeTaskProgress.setFinishedSize(Context.mergeTaskProgress.getTotalSize());
        Context.mergeTaskProgress.setFinished(true);
        writer.flush();

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
