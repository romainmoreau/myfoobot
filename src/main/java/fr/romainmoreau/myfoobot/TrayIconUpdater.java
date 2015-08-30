package fr.romainmoreau.myfoobot;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

@Component
public class TrayIconUpdater {
	private static final int SIDE = 16;
	private static final int SIZE = 12;
	private static final int LINE_WIDTH = 2;

	@Autowired
	private ApplicationContext applicationContext;

	private TrayIcon trayIcon;

	public void update(int value) {
		Platform.runLater(() -> trayIcon.setImage(generateImage(value)));
	}

	private Image generateImage(Integer value) {
		Canvas canvas = new Canvas(SIDE, SIDE);
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		if (value == null) {
			graphicsContext.setFill(Color.ORANGE);
		} else if (value < 50) {
			graphicsContext.setFill(Color.GREEN);
		} else {
			graphicsContext.setFill(Color.RED);
		}
		graphicsContext.fillRect(0, 0, SIDE, SIDE);
		graphicsContext.setTextAlign(TextAlignment.CENTER);
		graphicsContext.setTextBaseline(VPos.CENTER);
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setLineWidth(LINE_WIDTH);
		graphicsContext.setFont(Font.font("monospaced", FontWeight.BOLD, SIZE));
		if (value == null) {
			graphicsContext.strokeText("?", SIDE / 2, SIDE / 2);
			graphicsContext.fillText("?", SIDE / 2, SIDE / 2);
		} else {
			graphicsContext.strokeText(Integer.toString(value), SIDE / 2, SIDE / 2);
			graphicsContext.fillText(Integer.toString(value), SIDE / 2, SIDE / 2);
		}
		WritableImage writableImage = canvas.snapshot(null, null);
		Image image = SwingFXUtils.fromFXImage(writableImage, null);
		return image;
	}

	@PostConstruct
	private void postConstruct() throws AWTException {
		SystemTray systemTray = SystemTray.getSystemTray();
		PopupMenu popupMenu = new PopupMenu();
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.addActionListener(e -> {
			systemTray.remove(trayIcon);
			SpringApplication.exit(applicationContext);
			Platform.exit();
		});
		popupMenu.add(exitMenuItem);
		trayIcon = new TrayIcon(generateImage(null));
		trayIcon.setImageAutoSize(true);
		trayIcon.setPopupMenu(popupMenu);
		systemTray.add(trayIcon);
	}
}
