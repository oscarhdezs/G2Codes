package com.g2.weather.model;

public class OptionsToShow {

	private String minTemp;
	private String maxTemp;
	private String meanTemp;
	private String countObservations;
	private String totalDistance;
	private String distance;
	private String temperature;
	private String generateReport;
	
	public String getGenerateReport() {
		return generateReport;
	}
	public void setGenerateReport(String generateReport) {
		this.generateReport = generateReport;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getMinTemp() {
		return minTemp;
	}
	public void setMinTemp(String minTemp) {
		this.minTemp = minTemp;
	}
	public String getMaxTemp() {
		return maxTemp;
	}
	public void setMaxTemp(String maxTemp) {
		this.maxTemp = maxTemp;
	}
	public String getMeanTemp() {
		return meanTemp;
	}
	public void setMeanTemp(String meanTemp) {
		this.meanTemp = meanTemp;
	}
	public String getCountObservations() {
		return countObservations;
	}
	public void setCountObservations(String countObservations) {
		this.countObservations = countObservations;
	}
	public String getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}
	
}
