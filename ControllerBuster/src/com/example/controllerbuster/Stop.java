package com.example.controllerbuster;

public class Stop {
	private long id;
	private String stopName;
	private double latitude;
	private double longitude;

	public Stop() {
		this(null, 0, 0);
	}
	
	public Stop(String stopName, double latitude, double longitude) {
		this.stopName = stopName;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
