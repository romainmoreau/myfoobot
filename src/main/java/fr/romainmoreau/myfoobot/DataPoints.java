package fr.romainmoreau.myfoobot;

import java.math.BigDecimal;

public class DataPoints extends AbstractObject {
	private static final long serialVersionUID = 1L;

	private String[] sensors;

	private String[] units;

	private BigDecimal[][] datapoints;

	public String[] getSensors() {
		return sensors;
	}

	public void setSensors(String[] sensors) {
		this.sensors = sensors;
	}

	public String[] getUnits() {
		return units;
	}

	public void setUnits(String[] units) {
		this.units = units;
	}

	public BigDecimal[][] getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(BigDecimal[][] datapoints) {
		this.datapoints = datapoints;
	}
}
