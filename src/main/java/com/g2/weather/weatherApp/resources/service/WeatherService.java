package com.g2.weather.weatherApp.resources.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.g2.weather.model.OptionsToShow;
import com.g2.weather.model.Record;
import com.g2.weather.weatherApp.enums.ObservatoryEnum;

public interface WeatherService {

	void getOcurrencesInObservatory(Map<ObservatoryEnum, Long> ocurrence,
			Map<ObservatoryEnum, List<Record>> recordsByObservatory);

	Stream<Double> convertToSpecificGrade(OptionsToShow options,
			Map<ObservatoryEnum, List<Record>> recordsByObservatory);

	double convertToSpecificDistanceAndSum(List<Record> records, OptionsToShow options);
	
	void generateReport( List<Record> records, String distance, String temperature);
}
