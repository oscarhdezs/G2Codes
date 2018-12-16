package com.g2.weather.weatherApp.enums;

import java.util.Random;

public enum ObservatoryEnum {
	AU,US,FR,OT;
	
	public static ObservatoryEnum getRandomObservatoryEnum() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}
}
