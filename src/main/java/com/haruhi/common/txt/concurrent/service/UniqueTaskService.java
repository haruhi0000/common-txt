package com.haruhi.common.txt.concurrent.service;

import com.haruhi.common.txt.concurrent.task.UniqueTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;

/**
 * @author cppno1
 */
public class UniqueTaskService extends Service<String> {

    UniqueTask uniqueTask;

    @Override
    protected Task<String> createTask() {
        try {
            uniqueTask = new UniqueTask();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return uniqueTask;
    }
    public void pause() {
        uniqueTask.pause();
    }
    public void resume() {
        uniqueTask.resume();
    }
}
