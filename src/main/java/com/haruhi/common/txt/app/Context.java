package com.haruhi.common.txt.app;

import com.haruhi.common.txt.model.TaskInfo;
import com.haruhi.common.txt.model.TaskProgress;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;

/**
 * @author haruhi0000
 */
public class Context {
    /**
     * 去重任务信息
     */
    public static final TaskInfo taskInfo = new TaskInfo();
    /**
     * 是否暂停去重
     */
    public static boolean paused = false;
    /**
     * 分割文件进度
     */
    public static final TaskProgress splitTaskProgress = new TaskProgress();
    public static final TaskProgress mergeTaskProgress = new TaskProgress();
    public static final TaskProgress totalTaskProgress = new TaskProgress();
    public static final SimpleStringProperty elapsedTimeProperty = new SimpleStringProperty();
    public static final SimpleStringProperty remainTimeProperty = new SimpleStringProperty();
    public static final SimpleStringProperty processProperty = new SimpleStringProperty();
    public static final SimpleDoubleProperty progressProperty = new SimpleDoubleProperty();
    public static final SimpleStringProperty totalProgressLabelProperty = new SimpleStringProperty();
    public static final SimpleDoubleProperty totalProgressProperty = new SimpleDoubleProperty();
    public static int step = 0;
    public static String[] progressText = {"初始化", "分割文件: %s %%","合并文件: %s %%"};
    public static SimpleStringProperty infoLabelProperty = new SimpleStringProperty();
    public static int MAX_LINE_COUNT = 0;

    /**
     * 软件名称
     */
    public static String appName = "common-txt";
    public static File initSourceFileDir;
}
