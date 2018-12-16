package com.g2.weather.weatherApp.utility;

import java.time.LocalDate;
import java.util.Random;

public class WeatherUtility {

	private static Random random;
	
	public static LocalDate getRandomDate(LocalDate minDate, LocalDate maxDate) {
		random = new Random();
		int startRangeDate = (int) minDate.toEpochDay();
        int endRangeDate = (int) maxDate.toEpochDay();
        long randomDay = startRangeDate + random.nextInt(endRangeDate - startRangeDate);
        return LocalDate.ofEpochDay(randomDay);
	}
	
	public StringBuilder convertMileToMeter(StringBuilder sb) {
		String[] dist = sb.toString().split(",");
		double lat = Double.parseDouble(dist[0]);
		double alt = Double.parseDouble(dist[1]);
		lat = lat * 1609.344;
		alt = alt * 1609.344;
		return sb = new StringBuilder(lat + "," + alt);
	}

	public StringBuilder convertKmToMeter(StringBuilder sb) {
		String[] dist = sb.toString().split(",");
		double lat = Double.parseDouble(dist[0]);
		double alt = Double.parseDouble(dist[1]);
		lat = lat * 1000;
		alt = alt * 1000;
		return sb = new StringBuilder(lat + "," + alt);
	}

	public StringBuilder convertMeterToMile(StringBuilder sb) {
		String[] dist = sb.toString().split(",");
		double lat = Double.parseDouble(dist[0]);
		double alt = Double.parseDouble(dist[1]);
		lat = lat / 1609.344;
		alt = alt / 1609.344;
		return sb = new StringBuilder(lat + "," + alt);
	}

	public StringBuilder convertKmMille(StringBuilder sb) {
		String[] dist = sb.toString().split(",");
		double lat = Double.parseDouble(dist[0]);
		double alt = Double.parseDouble(dist[1]);
		lat = lat / 1.609;
		alt = alt / 1.609;
		return sb = new StringBuilder(lat + "," + alt);
	}

	public StringBuilder convertMeterToKm(StringBuilder sb) {
		String[] dist = sb.toString().split(",");
		double lat = Double.parseDouble(dist[0]);
		double alt = Double.parseDouble(dist[1]);
		lat = lat / 1000;
		alt = alt / 1000;
		return sb = new StringBuilder(lat + "," + alt);
	}

	public StringBuilder convertMilesToKm(StringBuilder sb) {
		String[] dist = sb.toString().split(",");
		double lat = Double.parseDouble(dist[0]);
		double alt = Double.parseDouble(dist[1]);
		lat = lat * 1.609;
		alt = alt * 1.609;
		return sb = new StringBuilder(lat + "," + alt);
	}
	
	public double convertCelsiusToFahrenheit(double temp) {
		temp = 32 + ((9 * temp) / 5);
		return temp;
	}

	public double convertFahrenheitToCelsius(double temp) {
		temp = ((temp - 32) * 5) / 9;
		return temp;
	}

	public double convertKelvinToCelsius(double temp) {
		temp = temp - 273.15;
		return temp;
	}

	public double convertKelvinToFahrenheit(double temp) {
		temp = ((temp - 273.15) * 1.8) + 32;
		return temp;
	}

	public double convertCelsiusToKelvin(double temp) {
		temp = (temp + 273.15);
		return temp;
	}

	public double convertFahrenheitToKelvin(double temp) {
		temp = ((temp - 32) * 1.8) + 273.15;
		return temp;
	}
	
}
