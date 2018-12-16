package com.g2.weather.model;

import java.time.LocalDate;

import com.g2.weather.weatherApp.enums.ObservatoryEnum;

public class Record {

	private LocalDate date;
	private String location;
	private double temp;
	private ObservatoryEnum observatory;
	
	
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public void setObservatory(ObservatoryEnum observatory) {
		this.observatory = observatory;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	public ObservatoryEnum getObservatory() {
		return observatory;
	}
	public Record(LocalDate date, double temp, ObservatoryEnum observatory,String location) {
		super();
		this.date = date;
		this.temp = temp;
		this.observatory = observatory;
		this.location = location;
	}
	public Record() {
		
	}
	
}
