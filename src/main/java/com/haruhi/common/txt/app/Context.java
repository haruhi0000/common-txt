package com.haruhi.common.txt.app;

import com.haruhi.common.txt.model.TaskInfo;
import com.haruhi.common.txt.model.TaskProgress;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author cppno1
 */
public class Context {
    public static final TaskInfo taskInfo = new TaskInfo();
    public static boolean paused = false;
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
}
