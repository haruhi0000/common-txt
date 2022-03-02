package com.haruhi.common.txt.concurrent.task;

import com.haruhi.common.txt.CommonTxtApplication;
import com.haruhi.common.txt.app.Context;
import com.haruhi.common.txt.kit.CommonTextUtils;
import com.haruhi.common.txt.kit.FileMergeUtil;
import com.haruhi.common.txt.kit.FileSplitUtil;
import com.haruhi.common.txt.model.TaskProgress;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;


/**
 * @author cppno1
 */
public class UniqueTask extends Task<String> {

    final FileSplitUtil fileSplitUtil = new FileSplitUtil();
    final FileMergeUtil fileMergeUtil = new FileMergeUtil();
    TaskProgress taskProgress;
    private Log log = LogFactory.getLog(UniqueTask.class);
    public UniqueTask() throws IOException {

    }

    @Override
    protected String call() throws IOException {
        Platform.runLater(() -> {
            Context.processProperty.set(Context.progressText[Context.step]);
            Context.infoLabelProperty.set(Context.taskInfo.getSourceFile().getAbsolutePath() + "\n" + "总行数: " + "--" + '\n' + "文件大小: " + Context.taskInfo.getSourceFile().length() + "字节");
        });


        //long lineCount = CommonTextUtils.getLineCount(Context.taskInfo.getSourceFile());
        Context.totalTaskProgress.setStartTimeStamp(System.currentTimeMillis());
        //Platform.runLater(() -> Context.infoLabelProperty.set(Context.taskInfo.getSourceFile().getAbsolutePath() + "\n" + "总行数: " + lineCount + '\n' + "文件大小: " + Context.taskInfo.getSourceFile().length() + "字节"));
        //Context.taskInfo.setLineCount(lineCount);
        Context.splitTaskProgress.setTotalSize(Context.taskInfo.getSourceFile().length());
        Context.mergeTaskProgress.setTotalSize(Context.taskInfo.getSourceFile().length());
        Context.splitTaskProgress.setStartTimeStamp(System.currentTimeMillis());
        Context.totalTaskProgress.setTotalSize(Context.splitTaskProgress.getTotalSize() + Context.mergeTaskProgress.getTotalSize());
        Context.step = 1;
        log.info("start split timestamp: " + Context.splitTaskProgress.getStartTimeStamp());
        fileSplitUtil.start();
        taskProgress = Context.splitTaskProgress;
        updateProgress();
        Context.splitTaskProgress.setFinishedTimeStamp(System.currentTimeMillis());
        log.info("end split timestamp: " + Context.splitTaskProgress.getFinishedTimeStamp());
        log.info("spend time: " + (Context.splitTaskProgress.getFinishedTimeStamp() - Context.splitTaskProgress.getStartTimeStamp()) );
        Context.step = 2;
        //fileMergeUtil.start();
        taskProgress = Context.mergeTaskProgress;

        //updateProgress();
        Platform.runLater(()->{
            CommonTxtApplication.closeProcessScene();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(CommonTxtApplication.mainStage);
            alert.setTitle("文本去重工具");
            alert.setHeaderText("文本去重完成");
            alert.setContentText(String.format("共耗时%d秒", Context.totalTaskProgress.getElapsedTime() / 100));
            alert.show();
            alert.setOnCloseRequest(( dialogEvent )-> System.exit(0));
        });
        return "完成";
    }

    private void updateProgress() {
        while (true) {
            checkWaitSign();
            Context.totalTaskProgress.setElapsedTime(Context.totalTaskProgress.getElapsedTime() + 1000);
            Context.totalTaskProgress.setFinishedSize(Context.splitTaskProgress.getFinishedSize() + Context.mergeTaskProgress.getFinishedSize());
            if(Context.step == 1) {
                Context.splitTaskProgress.setElapsedTime(Context.splitTaskProgress.getElapsedTime() + 1000);
            } else if (Context.step == 2) {
                Context.mergeTaskProgress.setElapsedTime(Context.mergeTaskProgress.getElapsedTime() + 1000);
            }
            double progress = (double) taskProgress.getFinishedSize() / taskProgress.getTotalSize();
            long progress2 = (long) (progress * 100);

            double totalProgress = (double) Context.totalTaskProgress.getFinishedSize() / Context.totalTaskProgress.getTotalSize();
            long totalProgress2 = (long) (totalProgress * 100);
            Platform.runLater(() -> {
                Context.progressProperty.set(progress);
                Context.processProperty.set(String.format(Context.progressText[Context.step], progress2));
                Context.totalProgressProperty.set(totalProgress);
                Context.totalProgressLabelProperty.set(String.format("进度: %s%%", totalProgress2));
                long elapsedTime = Context.totalTaskProgress.getElapsedTime();

                long remainTime;
                long speed;
                speed = Context.totalTaskProgress.getFinishedSize() / Context.totalTaskProgress.getElapsedTime();
                if(speed == 0) {
                    return;
                }
                long totalTime = Context.totalTaskProgress.getTotalSize() / speed;
                remainTime = totalTime - elapsedTime;
                Context.elapsedTimeProperty.set(formatTime(elapsedTime));
                Context.remainTimeProperty.set(formatTime(remainTime));
            });
            if (taskProgress.getFinished()) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String formatTime(long elapsedTime) {
        long second = elapsedTime / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        second = second - minute * 60;
        minute = minute - hour * 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void pause() {
        Context.paused = true;
    }

    public void resume() {
        Context.paused = false;
        synchronized (this) {
            notifyAll();
        }
        synchronized (fileSplitUtil) {
            fileSplitUtil.notifyAll();
        }
        synchronized (fileMergeUtil) {
            fileMergeUtil.notifyAll();
        }
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
