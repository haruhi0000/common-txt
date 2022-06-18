package com.haruhi.common.txt.controller;

import com.haruhi.common.txt.CommonTxtApplication;
import com.haruhi.common.txt.app.Context;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * @author cppno1
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


        Context.taskInfo.setCharset(StandardCharsets.UTF_8);
    }


    /**
     * 选择源文件
     */
    @FXML
    protected void onOpenFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        File file = fileChooser.showOpenDialog(CommonTxtApplication.mainStage);
        if (file != null) {
            sourceFilePathLabel.setText(file.getAbsolutePath());
            tempDirectoryLabel.setText(file.getParent() + File.separator + "temp");
            targetFilePathLabel.setText(file.getParent() + File.separator + file.getName() + ".unique.txt");
        }
        checkInputValue();
    }


    /**
     * 选择临时文件夹
     */
    @FXML
    protected void onSelectTempDirectoryButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择临时文件夹");
        File tempDirectory = directoryChooser.showDialog(CommonTxtApplication.mainStage);
        if (tempDirectory != null) {
            tempDirectoryLabel.setText(tempDirectory.getAbsolutePath());
        }
        checkInputValue();
    }


    /**
     * 开始任务按钮点击
     */
    @FXML
    protected void onStartButton2Click() {
        setTaskInfo();
        if(Context.taskInfo.getTargetFile().getParentFile().getFreeSpace() - Context.taskInfo.getSourceFile().length()
                + Context.taskInfo.getTempDirectory().getFreeSpace() - Context.taskInfo.getSourceFile().length() < 0) {
            CommonTxtApplication.closeProcessScene();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(CommonTxtApplication.mainStage);
            alert.setTitle("文本去重工具");
            alert.setHeaderText("剩余储存空间不足");
            alert.show();
            alert.setOnCloseRequest(( dialogEvent )-> System.exit(0));
        }


        CommonTxtApplication.switchProcessScene();
    }

    @FXML
    public void onTargetFileSelectClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        if (!StringUtils.isEmpty(targetFilePathLabel.getText())) {
            File targetFile = new File(targetFilePathLabel.getText());
            fileChooser.setInitialDirectory(targetFile.getParentFile());
            fileChooser.setInitialFileName(targetFile.getName());
        }
        File file = fileChooser.showSaveDialog(CommonTxtApplication.mainStage);
        if (file != null) {
            targetFilePathLabel.setText(file.getAbsolutePath());
        }
        checkInputValue();
    }


    private void checkInputValue() {
        uniqueButton.setDisable(StringUtils.isAnyEmpty(targetFilePathLabel.getText(), sourceFilePathLabel.getText(),
                tempDirectoryLabel.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uniqueButton.setDisable(true);
    }

    public void onAboutLinkClick(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/haruhi0000/common-txt"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}