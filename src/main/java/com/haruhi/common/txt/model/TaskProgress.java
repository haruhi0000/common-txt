package com.haruhi.common.txt.model;

/**
 * @author haruhi0000
 */
public class TaskProgress {
    private Long    totalSize;
    private Long    finishedSize;
    private Double  progress;
    private Long    startTimeStamp;
    private Long    finishedTimeStamp;
    private Long    elapsedTime;
    private Long    remainTime;
    private Boolean finished;
    public TaskProgress() {
        totalSize = 0L;
        finishedSize = 0L;
        progress = 0.0;
        startTimeStamp = 0L;
        finishedTimeStamp = 0L;
        elapsedTime = 0L;
        remainTime = 0L;
        finished = false;
    }
    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Long getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(Long remainTime) {
        this.remainTime = remainTime;
    }

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Long getFinishedTimeStamp() {
        return finishedTimeStamp;
    }

    public void setFinishedTimeStamp(Long finishedTimeStamp) {
        this.finishedTimeStamp = finishedTimeStamp;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getFinishedSize() {
        return finishedSize;
    }

    public void setFinishedSize(Long finishedSize) {
        this.finishedSize = finishedSize;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }
}
