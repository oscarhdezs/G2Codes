package com.g2.weather.weatherApp.resources.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.g2.weather.model.OptionsToShow;
import com.g2.weather.model.Record;
import com.g2.weather.weatherApp.enums.ObservatoryEnum;
import com.g2.weather.weatherApp.utility.WeatherConstants;
import com.g2.weather.weatherApp.utility.WeatherUtility;

@Component
public class WeatherServiceImp implements WeatherService {

	@Override
	public void getOcurrencesInObservatory(Map<ObservatoryEnum, Long> ocurrence,
			Map<ObservatoryEnum, List<Record>> recordsByObservatory) {
		ocurrence.put(ObservatoryEnum.AU, this.getOcurrence(recordsByObservatory, ObservatoryEnum.AU));
		ocurrence.put(ObservatoryEnum.US, this.getOcurrence(recordsByObservatory, ObservatoryEnum.US));
		ocurrence.put(ObservatoryEnum.FR, this.getOcurrence(recordsByObservatory, ObservatoryEnum.FR));
		ocurrence.put(ObservatoryEnum.OT, this.getOcurrence(recordsByObservatory, ObservatoryEnum.OT));
	}

	private long getOcurrence(Map<ObservatoryEnum, List<Record>> recordsByObservatory, ObservatoryEnum enumType) {
		long count = 0;
		if (recordsByObservatory.get(enumType) != null) {
			count = recordsByObservatory.get(enumType).stream().mapToDouble(d -> d.getTemp()).count();
		}
		return count;
	}

	@Override
	public Stream<Double> convertToSpecificGrade(OptionsToShow options,
			Map<ObservatoryEnum, List<Record>> recordsByObservatory) {

		Stream<Double> sumGrades = null;
		if (options.getTemperature().equals(WeatherConstants.CELSIUS)) {
			if (recordsByObservatory.get(ObservatoryEnum.AU) != null) {
				Stream<Double> map = recordsByObservatory.get(ObservatoryEnum.AU).stream().map(d -> d.getTemp());
				sumGrades = sumGrades != null ? Stream.concat(sumGrades, map) : map;
			}
			if (recordsByObservatory.get(ObservatoryEnum.US) != null) {
				Stream<Double> map2 = recordsByObservatory.get(ObservatoryEnum.US).stream()
						.map(d -> ((d.getTemp() - 32) * 5) / 9);

				sumGrades = sumGrades != null ? Stream.concat(sumGrades, map2) : map2;
			}
			if (recordsByObservatory.get(ObservatoryEnum.FR) != null
					|| recordsByObservatory.get(ObservatoryEnum.OT) != null) {
				Stream<Double> map3 = recordsByObservatory.get(ObservatoryEnum.FR).stream()
						.map(d -> d.getTemp() - 273.15);
				sumGrades = sumGrades != null ? Stream.concat(sumGrades, map3) : map3;
			}

		} else if (options.getTemperature().equals(WeatherConstants.FAHRENHEIT)) {
			if (recordsByObservatory.get(ObservatoryEnum.US) != null) {
				Stream<Double> map = recordsByObservatory.get(ObservatoryEnum.US).stream().map(d -> d.getTemp());
				sumGrades = sumGrades != null ? Stream.concat(sumGrades, map) : map;
			}

			if (recordsByObservatory.get(ObservatoryEnum.AU) != null) {
				Stream<Double> map2 = recordsByObservatory.get(ObservatoryEnum.AU).stream()
						.map(d -> 32 + ((9 * d.getTemp()) / 5));
				sumGrades = sumGrades != null ? Stream.concat(sumGrades, map2) : map2;
			}

			if (recordsByObservatory.get(ObservatoryEnum.FR) != null
					|| recordsByObservatory.get(ObservatoryEnum.OT) != null) {
				Stream<Double> map3 = recordsByObservatory.get(ObservatoryEnum.FR).stream()
						.map(d -> ((d.getTemp() - 273.15) * 1.8) + 32);
				sumGrades = sumGrades != null ? Stream.concat(sumGrades, map3) : map3;
			}

		} else if (options.getTemperature().equals(WeatherConstants.KELVIN)) {

			if (recordsByObservatory.get(ObservatoryEnum.FR) != null
					|| recordsByObservatory.get(ObservatoryEnum.OT) != null) {
				Stream<Double> map = recordsByObservatory.get(ObservatoryEnum.FR).stream().map(d -> d.getTemp());
				sumGrades = sumGrades != null ? Stream.concat(sumGrades, map) : map;
			}
			if (recordsByObservatory.get(ObservatoryEnum.US) != null) {
				Stream<Double> map3 = recordsByObservatory.get(ObservatoryEnum.US).stream()
						.map(d -> ((d.getTemp() - 32) * 1.8) + 273.15);
				sumGrades = sumGrades != null ? Stream.concat(sumGrades, map3) : map3;
			}
			if (recordsByObservatory.get(ObservatoryEnum.AU) != null) {
				Stream<Double> map4 = recordsByObservatory.get(ObservatoryEnum.AU).stream()
						.map(d -> (d.getTemp() + 273.15));
				sumGrades = sumGrades != null ? Stream.concat(sumGrades, map4) : map4;
			}
		}
		return sumGrades;

	}

	@Override
	public double convertToSpecificDistanceAndSum(List<Record> records, OptionsToShow options) {

		double distTotal = 0;
		final List<String> list = new ArrayList<>();
		records.stream().forEach(d -> {
			list.add(d.getLocation() + "," + d.getObservatory());
			System.out.println(list);
		});
		double x1 = 0, y1 = 0, x2 = 0, y2 = 0;

		if (options.getDistance().equals(WeatherConstants.KILOMETER)) {
			for (int x = 0; x < list.size() - 1; x++) {
				String[] current = list.get(x).split(",");
				String[] next = list.get(x + 1).split(",");
				if (current[2].equals(WeatherConstants.AU) || current[2].equals(WeatherConstants.OT)) {
					x1 = Double.parseDouble(current[0]);
					y1 = Double.parseDouble(current[1]);
				} else if (current[2].equals(WeatherConstants.US)) {
					x1 = Double.parseDouble(current[0]) * 1.60934;
					y1 = Double.parseDouble(current[1]) * 1.60934;
				} else if (current[2].equals(WeatherConstants.FR)) {
					x1 = Double.parseDouble(current[0]) / 1000;
					y1 = Double.parseDouble(current[1]) / 1000;
				}

				if (next[2].equals(WeatherConstants.AU) || next[2].equals(WeatherConstants.OT)) {
					x2 = Double.parseDouble(next[0]);
					y2 = Double.parseDouble(next[1]);
				} else if (next[2].equals(WeatherConstants.US)) {
					x2 = Double.parseDouble(next[0]) * 1.60934;
					y2 = Double.parseDouble(next[1]) * 1.60934;
				} else if (next[2].equals(WeatherConstants.FR)) {
					x2 = Double.parseDouble(next[0]) / 1000;
					y2 = Double.parseDouble(next[1]) / 1000;
				}
				distTotal += this.calculateDistance(x1, y1, x2, y2);
				System.out.println(distTotal);
			}
		} else if (options.getDistance().equals(WeatherConstants.MILE)) {
			for (int x = 0; x < list.size() - 1; x++) {
				String[] current = list.get(x).split(",");
				String[] next = list.get(x + 1).split(",");
				if (current[2].equals(WeatherConstants.US)) {
					x1 = Double.parseDouble(current[0]);
					y1 = Double.parseDouble(current[1]);
				} else if (current[2].equals(WeatherConstants.AU) || current[2].equals(WeatherConstants.OT)) {
					x1 = Double.parseDouble(current[0]) / 1.60934;
					y1 = Double.parseDouble(current[1]) / 1.60934;
				} else if (current[2].equals(WeatherConstants.FR)) {
					x1 = Double.parseDouble(current[0]) / 1609.344;
					y1 = Double.parseDouble(current[1]) / 1609.344;
				}

				if (next[2].equals(WeatherConstants.US)) {
					x2 = Double.parseDouble(next[0]);
					y2 = Double.parseDouble(next[1]);
				} else if (next[2].equals(WeatherConstants.AU) || next[2].equals(WeatherConstants.OT)) {
					x2 = Double.parseDouble(next[0]) / 1.60934;
					y2 = Double.parseDouble(next[1]) / 1.60934;
				} else if (next[2].equals(WeatherConstants.FR)) {
					x2 = Double.parseDouble(next[0]) / 1609.344;
					y2 = Double.parseDouble(next[1]) / 1609.344;
				}
				distTotal += this.calculateDistance(x1, y1, x2, y2);
				System.out.println(distTotal);
			}
		} else if (options.getDistance().equals(WeatherConstants.METER)) {
			for (int x = 0; x < list.size() - 1; x++) {
				String[] current = list.get(x).split(",");
				String[] next = list.get(x + 1).split(",");
				if (current[2].equals(WeatherConstants.FR)) {
					x1 = Double.parseDouble(current[0]);
					y1 = Double.parseDouble(current[1]);
				} else if (current[2].equals(WeatherConstants.AU) || current[2].equals(WeatherConstants.OT)) {
					x1 = Double.parseDouble(current[0]) * 1000;
					y1 = Double.parseDouble(current[1]) * 1000;
				} else if (current[2].equals(WeatherConstants.US)) {
					x1 = Double.parseDouble(current[0]) * 1609.344;
					y1 = Double.parseDouble(current[1]) * 1609.344;
				}

				if (next[2].equals(WeatherConstants.FR)) {
					x2 = Double.parseDouble(next[0]);
					y2 = Double.parseDouble(next[1]);
				} else if (next[2].equals(WeatherConstants.AU) || next[2].equals(WeatherConstants.OT)) {
					x2 = Double.parseDouble(next[0]) * 1000;
					y2 = Double.parseDouble(next[1]) * 1000;
				} else if (next[2].equals(WeatherConstants.US)) {
					x2 = Double.parseDouble(next[0]) * 1609.344;
					y2 = Double.parseDouble(next[1]) * 1609.344;
				}
				distTotal += this.calculateDistance(x1, y1, x2, y2);
				System.out.println(distTotal);
			}
		}
		return distTotal;

	}

	private double calculateDistance(double x1, double y1, double x2, double y2) {
		double cateto1 = x2 - x1;
		double cateto2 = y2 - y1;
		double distance = Math.sqrt(cateto1 * cateto1 + cateto2 * cateto2);
		return distance;
	}

	@Override
	public void generateReport(List<Record> records, String distance, String temperature) {
		Record record = null;
		StringBuilder location;
		StringBuilder file = new StringBuilder("The units in this report are in ");
		file.append(distance)
		.append(" distance")
		.append(" and ")
		.append(temperature)
		.append(" grades")
		.append(System.getProperty("line.separator"));
		double temp = 0;
		for (int x = 0; x < records.size(); x++) {
			record = records.get(x);
			location = new StringBuilder(record.getLocation());

			location = this.convertDistance(location, distance, record.getObservatory());
			temp = this.convertTemp(record.getTemp(), temperature, record.getObservatory());
			System.out.println(temp);
			file.append(record.getDate()).append("|").append(location.toString()).append("|").append(temp).append("|")
					.append(record.getObservatory()).append(", ");
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("ReportStatics.cvs"));
			writer.write(file.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(file.toString());
	}

	private double convertTemp(double temp, String toUnit, ObservatoryEnum observatory) {
		WeatherUtility util = new WeatherUtility();
		if (toUnit.equals(WeatherConstants.CELSIUS)) {
			switch (observatory) {
			case US:
				temp = util.convertFahrenheitToCelsius(temp);
				break;

			case FR:
			case OT:
				temp = util.convertKelvinToCelsius(temp);
				break;

			}
		} else if (toUnit.equals(WeatherConstants.FAHRENHEIT)) {
			switch (observatory) {
			case AU:
				temp = util.convertCelsiusToFahrenheit(temp);
				break;

			case FR:
			case OT:
				temp = util.convertKelvinToFahrenheit(temp);
				break;

			}
		}else if (toUnit.equals(WeatherConstants.KELVIN)) {
			switch (observatory) {
			case AU:
				temp = util.convertCelsiusToKelvin(temp);
				break;

			case US:
				temp = util.convertFahrenheitToKelvin(temp);
				break;

			}
		}

		return temp;
	}

	private StringBuilder convertDistance(StringBuilder sb, String toUnit, ObservatoryEnum observatory) {
		WeatherUtility util = new WeatherUtility();
		if (toUnit.equals(WeatherConstants.KILOMETER)) {
			switch (observatory) {
			case US:
				sb = util.convertMilesToKm(sb);
				break;
			case FR:
				sb = util.convertMeterToKm(sb);
				break;
			}
		} else if (toUnit.equals(WeatherConstants.MILE)) {
			switch (observatory) {
			case FR:
				sb = util.convertMeterToMile(sb);
				break;

			case AU:
			case OT:
				sb = util.convertKmMille(sb);
				break;

			}
		} else if (toUnit.equals(WeatherConstants.METER)) {
			switch (observatory) {
			case US:
				sb = util.convertMileToMeter(sb);
				break;

			case AU:
			case OT:
				sb = util.convertKmToMeter(sb);
				break;
			}
		}
		return sb;
	}

}
