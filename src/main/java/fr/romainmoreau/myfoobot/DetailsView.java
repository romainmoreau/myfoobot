package fr.romainmoreau.myfoobot;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
public class DetailsView extends HBox {
	private Stage stage;

	private DetailView pmDetailView;

	private DetailView tmpDetailView;

	private DetailView humDetailView;

	private DetailView co2DetailView;

	private DetailView vocDetailView;

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
		getChildren().addAll(co2DetailView, pmDetailView, vocDetailView, tmpDetailView, humDetailView);
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
		stage.setHeight(DetailView.SIDE);
		stage.setX(visualBounds.getMaxX() - stage.getWidth());
		stage.setY(visualBounds.getMaxY() - stage.getHeight());
		stage.setScene(new Scene(this));
	}

	public void update(BigDecimal pm, BigDecimal tmp, BigDecimal hum, BigDecimal co2, BigDecimal voc) {
		pmDetailView.update(pm);
		tmpDetailView.update(tmp);
		humDetailView.update(hum);
		co2DetailView.update(co2);
		vocDetailView.update(voc);
	}

	public void show() {
		if (!stage.isShowing()) {
			stage.show();
		}
	}
}
