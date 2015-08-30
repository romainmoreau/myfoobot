package fr.romainmoreau.myfoobot;

public class Device extends AbstractObject {
	private static final long serialVersionUID = 1L;

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
