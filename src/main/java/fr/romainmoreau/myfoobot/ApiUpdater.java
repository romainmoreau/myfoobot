package fr.romainmoreau.myfoobot;

import java.math.BigDecimal;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javafx.application.Platform;

@Component
public class ApiUpdater {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiUpdater.class);

	private static final String PM = "pm";
	private static final String TMP = "tmp";
	private static final String HUM = "hum";
	private static final String CO2 = "co2";
	private static final String VOC = "voc";
	private static final String ALLPOLLU = "allpollu";

	@Autowired
	private ApiClient apiClient;

	@Autowired
	private TrayIconUpdater trayIconUpdater;

	@Autowired
	private DetailsView detailsView;

	private String uuid;

	@PostConstruct
	private void postConstruct() {
		Device[] devices = apiClient.getDevices();
		if (devices.length < 1) {
			throw new IllegalStateException("At least one device is mandatory");
		}
		uuid = devices[0].getUuid();
		update();
	}

	private BigDecimal getValue(DataPoints dataPoints, String name) {
		BigDecimal value = null;
		OptionalInt optionalInt = IntStream.range(0, dataPoints.getSensors().length)
				.filter(i -> name.equals(dataPoints.getSensors()[i])).findAny();
		if (optionalInt.isPresent()) {
			value = dataPoints.getDatapoints()[0][optionalInt.getAsInt()];
		}
		return value;
	}

	@Scheduled(cron = "*/30 * * * * *")
	public void update() {
		try {
			DataPoints dataPoints = apiClient.getLastDataPoints(uuid, 0, 0);
			BigDecimal pm = getValue(dataPoints, PM);
			BigDecimal tmp = getValue(dataPoints, TMP);
			BigDecimal hum = getValue(dataPoints, HUM);
			BigDecimal co2 = getValue(dataPoints, CO2);
			BigDecimal voc = getValue(dataPoints, VOC);
			BigDecimal allpollu = getValue(dataPoints, ALLPOLLU);
			LOGGER.debug("pm: {}, tmp: {}, hum: {}, co2: {}, voc: {}, allpollu: {}", pm, tmp, hum, co2, voc, allpollu);
			trayIconUpdater.update(allpollu);
			Platform.runLater(() -> detailsView.update(pm, tmp, hum, co2, voc));
		} catch (Exception e) {
			LOGGER.error("Exception while updating", e);
		}
	}
}
