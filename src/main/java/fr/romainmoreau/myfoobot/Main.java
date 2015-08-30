package fr.romainmoreau.myfoobot;

import org.springframework.boot.SpringApplication;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		SpringApplication springApplication = new SpringApplication(MyFoobotConfiguration.class);
		springApplication.setHeadless(false);
		springApplication.run();
	}

	public static final void main(String[] args) {
		launch(args);
	}
}
