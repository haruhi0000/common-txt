package com.haruhi.common.txt.controller;

import com.haruhi.common.txt.CommonTxtApplication;
import com.haruhi.common.txt.concurrent.task.Context;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author 61711
 */
public class ProcessController implements Initializable {
    @FXML
    public Label targetFileLabel;
    @FXML
    public Label elapsedTimeLabel;
    @FXML
    public Label remainTimeLabel;
    @FXML
    public ProgressBar totalProgressBar;
    @FXML
    public Label totalProgressLabel;
    @FXML
    public Label infoLabel;

    public ProcessController() {

    }

    @FXML
    ProgressBar taskProgressBar;

    @FXML
    Label processLabel;

    @FXML
    Button controllerButton;

    @FXML
    public void onCancelButtonClick() {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 进度条
        taskProgressBar.progressProperty().bind(Context.progressProperty);
        // 进度数值
        processLabel.textProperty().bind(Context.processProperty);

        targetFileLabel.setText(Context.taskInfo.getTargetFile().getName());
        // 已用时间
        elapsedTimeLabel.textProperty().bind(Context.elapsedTimeProperty);
        // 剩余时间
        remainTimeLabel.textProperty().bind(Context.remainTimeProperty);
        totalProgressBar.progressProperty().bind(Context.totalProgressProperty);
        totalProgressLabel.textProperty().bind(Context.totalProgressLabelProperty);
        infoLabel.textProperty().bind(Context.infoLabelProperty);

    }

    public void onPauseButtonClick() {

        if (Context.paused) {
            CommonTxtApplication.uniqueTaskService.resume();
            controllerButton.setText("暂停");
        } else {
            CommonTxtApplication.uniqueTaskService.pause();
            controllerButton.setText("继续");
        }
    }
}
