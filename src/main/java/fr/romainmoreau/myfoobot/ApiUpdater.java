package fr.romainmoreau.myfoobot;

import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApiUpdater {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiUpdater.class);

	private static final String ALL_POLLU = "allpollu";

	@Autowired
	private ApiClient apiClient;

	@Autowired
	private TrayIconUpdater trayIconUpdater;

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

	@Scheduled(cron = "*/30 * * * * *")
	public void update() {
		try {
			DataPoints dataPoints = apiClient.getLastDataPoints(uuid, 0, 0);
			IntStream.range(0, dataPoints.getSensors().length).filter(i -> ALL_POLLU.equals(dataPoints.getSensors()[i]))
					.findAny().ifPresent(i -> {
						int allPollu = dataPoints.getDatapoints()[0][i].intValue();
						LOGGER.info("Last all pollu value: {}", allPollu);
						trayIconUpdater.update(allPollu);
					});
		} catch (Exception e) {
			LOGGER.error("Exception while updating", e);
		}
	}
}
