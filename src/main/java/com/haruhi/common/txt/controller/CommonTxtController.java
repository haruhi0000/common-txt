package com.haruhi.common.txt.controller;

import com.haruhi.common.txt.CommonTxtApplication;
import com.haruhi.common.txt.concurrent.task.Context;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author 61711
 */
public class CommonTxtController implements Initializable {
    @FXML
    public Button uniqueButton;
    @FXML
    Label targetFilePathLabel;
    @FXML
    Label sourceFilePathLabel;
    @FXML
    Label tempDirectoryLabel;

    private void setTaskInfo() {
        Context.taskInfo.setTargetFile(new File(targetFilePathLabel.getText()));
        Context.taskInfo.setSourceFile(new File(sourceFilePathLabel.getText()));
        Context.taskInfo.setTempDirectory(new File(tempDirectoryLabel.getText()));
    }


    /**
     * 选择源文件
     */
    @FXML
    protected void onOpenFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.setInitialDirectory(new File("C:\\Users\\61711\\Documents\\test"));
        File file = fileChooser.showOpenDialog(CommonTxtApplication.mainStage);
        if (file != null) {
            sourceFilePathLabel.setText(file.getAbsolutePath());
            tempDirectoryLabel.setText(file.getParent() + File.separator + "temp");
            targetFilePathLabel.setText(file.getParent() + File.separator + file.getName() + ".unique.txt");
            uniqueButton.setDisable(false);
        }
    }


    /**
     * 选择临时文件夹
     */
    @FXML
    protected void onSelectTempDirectoryButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择临时文件夹");
        File tempDirectory = directoryChooser.showDialog(CommonTxtApplication.mainStage);
        tempDirectoryLabel.setText(tempDirectory.getAbsolutePath());
    }


    /**
     * 开始任务按钮点击
     */
    @FXML
    protected void onStartButton2Click() {
        setTaskInfo();
        CommonTxtApplication.switchProcessScene();
    }

    @FXML
    public void onTargetFileSelectClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        if (!StringUtils.isEmpty(targetFilePathLabel.getText())) {
            fileChooser.setInitialDirectory(Context.taskInfo.getSourceFile().getParentFile());
            fileChooser.setInitialFileName(Context.taskInfo.getSourceFile().getName() + ".unique.txt");
        }
        File file = fileChooser.showSaveDialog(CommonTxtApplication.mainStage);
        if (file != null) {
            targetFilePathLabel.setText(file.getAbsolutePath());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uniqueButton.setDisable(true);
    }
}