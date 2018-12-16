package com.g2.weather.weatherApp.resources;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.g2.weather.model.OptionsToShow;
import com.g2.weather.model.Record;
import com.g2.weather.weatherApp.enums.ObservatoryEnum;
import com.g2.weather.weatherApp.resources.service.WeatherService;
import com.g2.weather.weatherApp.utility.WeatherConstants;
import com.g2.weather.weatherApp.utility.WeatherUtility;

@Controller
@RequestMapping(value = "/weather")
public class WeatherResource {

	private List<Record> records = new ArrayList<>();
	private Map<ObservatoryEnum, Long> ocurrence = new HashMap<>();
	@Autowired
	private WeatherService weatherService;

	@GetMapping
	public String showHome(Model model) {
		OptionsToShow options = new OptionsToShow();
		model.addAttribute("options", options);
		return "home";
	}

	@PostMapping
	public String showHome(@ModelAttribute OptionsToShow options, Model model) {

		Map<ObservatoryEnum, List<Record>> recordsByObservatory = records.stream()
				.collect(Collectors.groupingBy(Record::getObservatory));
		Stream<Double> sumGrades = null;
		double minTemp = 0;
		double maxTemp = 0;
		double meanTemp = 0;
		double distTotal = 0;

		weatherService.getOcurrencesInObservatory(ocurrence, recordsByObservatory);
		sumGrades = weatherService.convertToSpecificGrade(options, recordsByObservatory);
		distTotal = weatherService.convertToSpecificDistanceAndSum(records, options);
		DoubleSummaryStatistics summaryStatistics = sumGrades.mapToDouble(d -> d).summaryStatistics();
		if (options.getGenerateReport() != null && options.getGenerateReport().equals(WeatherConstants.REPORT)) {
			weatherService.generateReport(records,options.getDistance(),options.getTemperature());
		}

		minTemp = summaryStatistics.getMin();
		maxTemp = summaryStatistics.getMax();
		meanTemp = summaryStatistics.getAverage();

		model.addAttribute("min", minTemp);
		model.addAttribute("max", maxTemp);
		model.addAttribute("mean", meanTemp);
		model.addAttribute("ocurence", ocurrence);
		model.addAttribute("distance", distTotal);
		model.addAttribute("options", options);
		return "result";
	}

	@RequestMapping(value = "showTest", method = RequestMethod.GET)
	public List<Record> showData() {
		return records;
	}

	@RequestMapping(value = "generateReport", method = RequestMethod.GET)
	public List<Record> generateReport() {
		return records;
	}

	/***********************************/
	/* create dummy data and save file */
	/**********************************/
	@PostConstruct
	public void init() {
		Record record;
		int temp;
		int x;
		int y;
		ObservatoryEnum observatory;
		LocalDate ld;
		StringBuilder location;
		StringBuilder sb = new StringBuilder();
		for (int a = 0; a < 10; a++) {
			location = new StringBuilder();
			temp = new Random().ints(1, -150, 150).findFirst().getAsInt();
			x = new Random().ints(1, 1, 100).findFirst().getAsInt();
			y = new Random().ints(1, 1, 100).findFirst().getAsInt();
			location.append(x).append(",").append(y);
			observatory = ObservatoryEnum.getRandomObservatoryEnum();
			ld = WeatherUtility.getRandomDate(LocalDate.of(1900, 1, 1), LocalDate.of(2010, 1, 1));

			record = new Record();
			record.setDate(ld);
			record.setLocation(location.toString());
			record.setTemp(temp);
			record.setObservatory(observatory);

			records.add(record);

			sb.append(ld).append("|").append(location.toString()).append("|").append(temp).append("|")
					.append(observatory).append(System.getProperty("line.separator"));
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"));
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
