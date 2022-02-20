package com.haruhi.common.txt.controller;

import com.haruhi.common.txt.CommonTxtApplication;
import com.haruhi.common.txt.app.Context;
import com.haruhi.common.txt.app.StandardCharset;
import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
import java.nio.charset.Charset;
import java.util.ResourceBundle;

/**
 * @author cppno1
 */
public class CommonTxtController implements Initializable {
    @FXML
    public Button uniqueButton;
    @FXML
    ChoiceBox<String> charsetChoice;
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
        String charsetChoiceValue = charsetChoice.getValue();
        String[] strings = charsetChoiceValue.split(" ");
        Context.taskInfo.setCharset(Charset.forName(strings[strings.length - 1]));
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
                tempDirectoryLabel.getText(), charsetChoice.getValue()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uniqueButton.setDisable(true);
        charsetChoice.setItems(FXCollections.observableArrayList(StandardCharset.ALL_CHARSET));
        charsetChoice.addEventHandler(EventType.ROOT, (e) -> checkInputValue());
    }

    public void onAboutLinkClick(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/cqqno1/common-txt"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}