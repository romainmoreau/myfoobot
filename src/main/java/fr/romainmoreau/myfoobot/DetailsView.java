package fr.romainmoreau.myfoobot;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.springframework.stereotype.Component;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
public class DetailsView extends VBox {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
			.ofLocalizedDateTime(FormatStyle.MEDIUM);

	private Stage stage;

	private DetailView pmDetailView;

	private DetailView tmpDetailView;

	private DetailView humDetailView;

	private DetailView co2DetailView;

	private DetailView vocDetailView;

	private Label statusLabel;

	public DetailsView() {
		initView();
		initStage();
	}

	private void initView() {
		pmDetailView = new DetailView("PM", new BigDecimal(25));
		tmpDetailView = new DetailView("TMP", null);
		humDetailView = new DetailView("HUM", null);
		co2DetailView = new DetailView("CO2", new BigDecimal(1300));
		vocDetailView = new DetailView("VOC", new BigDecimal(300));
		statusLabel = new Label();
		statusLabel.setAlignment(Pos.CENTER);
		statusLabel.getStyleClass().add("neutral");
		statusLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		VBox.setVgrow(statusLabel, Priority.ALWAYS);
		HBox hBox = new HBox();
		hBox.getChildren().addAll(co2DetailView, pmDetailView, vocDetailView, tmpDetailView, humDetailView);
		getChildren().addAll(statusLabel, hBox);
		setOnMouseClicked(e -> {
			if (stage.isShowing()) {
				stage.hide();
			}
		});
	}

	private void initStage() {
		stage = new Stage();
		stage.getIcons().add(new Image("/myfoobot.png"));
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setAlwaysOnTop(true);
		stage.setWidth(DetailView.SIDE * 5);
		stage.setHeight(DetailView.SIDE * 3 / 2);
		stage.setX(visualBounds.getMaxX() - stage.getWidth());
		stage.setY(visualBounds.getMaxY() - stage.getHeight());
		Scene scene = new Scene(this);
		scene.getStylesheets().add("/myfoobot.css");
		stage.setScene(scene);
	}

	public void update(ZonedDateTime zonedDateTime, BigDecimal pm, BigDecimal tmp, BigDecimal hum, BigDecimal co2,
			BigDecimal voc) {
		pmDetailView.update(pm);
		tmpDetailView.update(tmp);
		humDetailView.update(hum);
		co2DetailView.update(co2);
		vocDetailView.update(voc);
		statusLabel.setText(DATE_TIME_FORMATTER.format(zonedDateTime));
	}

	public void show() {
		if (!stage.isShowing()) {
			stage.show();
		}
	}
}
