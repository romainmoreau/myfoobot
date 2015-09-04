package fr.romainmoreau.myfoobot;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DetailView extends VBox {
	public static final int SIDE = 64;

	private static final int MAX_DIGITS = 4;
	private static final String LABEL_STYLE = "-fx-font-family:'monospace';-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:white;-fx-effect:dropshadow(three-pass-box,black,2,0.5,0,0)";

	private BigDecimal maxValue;

	private Label valueLabel;

	public DetailView(String text, BigDecimal maxValue) {
		setAlignment(Pos.CENTER);
		setMinSize(SIDE, SIDE);
		setPrefSize(SIDE, SIDE);
		setMaxSize(SIDE, SIDE);
		this.maxValue = maxValue;
		Label label = new Label(text);
		label.setStyle(LABEL_STYLE);
		label.setAlignment(Pos.CENTER);
		label.setMaxWidth(SIDE);
		valueLabel = new Label(text);
		valueLabel.setStyle(LABEL_STYLE);
		valueLabel.setAlignment(Pos.CENTER);
		valueLabel.setMaxWidth(SIDE);
		getChildren().addAll(label, valueLabel);
		updateBackgroundColor(null);
	}

	private void updateBackgroundColor(BigDecimal value) {
		if (maxValue == null) {
			setStyle("-fx-background-color:blue");
		} else if (value == null) {
			setStyle("-fx-background-color:orange");
		} else if (value.compareTo(maxValue) == -1) {
			setStyle("-fx-background-color:green");
		} else {
			setStyle("-fx-background-color:red");
		}
	}

	public void update(BigDecimal value) {
		updateBackgroundColor(value);
		int newScale = MAX_DIGITS - value.precision() + value.scale();
		if (newScale >= 0) {
			value = value.setScale(newScale, RoundingMode.HALF_EVEN);
		}
		value = value.stripTrailingZeros();
		valueLabel.setText(value.toPlainString());
	}
}
