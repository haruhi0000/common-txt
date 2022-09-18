package com.haruhi.common.txt;


import com.haruhi.common.txt.concurrent.service.UniqueTaskService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * @author haruhi0000
 */
public class CommonTxtApplication extends Application {

	public static Stage mainStage;
	public static Scene mainScene;
	public static Scene processScene;
	public static int windowWidth = 400;
	public static int windowHeight = 270;
	public static final UniqueTaskService uniqueTaskService = new UniqueTaskService();
	static System.Logger logger;
	public CommonTxtApplication() {
		logger = System.getLogger("MainApplication");
	}
	private final Log log = LogFactory.getLog(CommonTxtApplication.class);
	@Override
	public void start(Stage stage)  {
		log.info("debug model");
		CommonTxtApplication.mainStage = stage;
		initMainScene();
		stage.setTitle("文本去重工具");
		stage.setScene(mainScene);
		stage.setResizable(false);
		stage.show();
		stage.setOnCloseRequest((event)-> System.exit(0));
	}
	public static void switchProcessScene() {
		uniqueTaskService.start();
		initProcessScene();
		mainStage.setScene(processScene);
	}

	public static void initProcessScene() {
		FXMLLoader fxmlLoader = new FXMLLoader(CommonTxtApplication.class.getResource("process-view.fxml"));
		try {
			processScene = new Scene(fxmlLoader.load(), 450, 300);
			processScene.getStylesheets().add("app.css");
		} catch (IOException e) {
			logger.log(System.Logger.Level.ERROR, e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
	public static void closeProcessScene() {
		CommonTxtApplication.mainStage.hide();
	}
	public void initMainScene() {
		FXMLLoader fxmlLoader = new FXMLLoader(CommonTxtApplication.class.getResource("application-view.fxml"));

		try {
			mainScene = new Scene(fxmlLoader.load(), windowWidth, windowHeight);
			mainScene.getStylesheets().add("app.css");
		} catch (IOException e) {
			logger.log(System.Logger.Level.ERROR, e.getMessage());
			System.exit(1);
		}
	}
	public static void main(String[] args) {
		launch();
	}
}
