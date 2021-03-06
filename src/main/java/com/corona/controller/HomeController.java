package com.corona.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.corona.domain.WorldDailyReport;
import com.corona.service.WorldDailyReportService;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private WorldDailyReportService service;
	

	/**
	 * 메인 화면
	 * 
	 * @param  모델
	 * @return 페이지 명
	 * @throws Exception 
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) throws Exception {
		logger.info("Main page");
		
		// 코로나 정보를 취득한다
		List<WorldDailyReport> result = service.getWorldList();
		
		// 모델에 데이터를 담아서 보낸다 (코로나정보, 갱신일자)
		model.addAttribute("worldList", result);
		if(result.size() > 0) {
			model.addAttribute("updatedDate", result.get(0).getUpdatedDate());
		} else {
			model.addAttribute("updatedDate", "");
		}
		
		return "home";
	}
	
	
	/**
	 * 상세 화면
	 * 
	 * @param  국가ID
	 * @param  모델
	 * @return 페이지 명
	 * @throws Exception 
	 */
	@RequestMapping(value="/{countryId}", method = RequestMethod.GET)
	public String detail(@PathVariable("countryId")String country, Model model) throws Exception {
		logger.info("Detail page");
		
		// 특정 국가의 코로나 정보를 취득한다
		List<WorldDailyReport> result = service.getDetailList(country);
		
		for(int i = 0; i < result.size()-1; i++) {
			// 기준이 되는 날짜의 데이터
			int newConfirmed = result.get(i).getConfirmed();
			int newDeaths = result.get(i).getDeaths();
			int newRecovered = result.get(i).getRecovered();
			// 비교대상이 되는 날짜의 데이터
			int oldConfirmed = result.get(i+1).getConfirmed();
			int oldDeaths = result.get(i+1).getDeaths();
			int oldRecovered = result.get(i+1).getRecovered();
			
			// 증감수치를 구한다
			result.get(i).setConfirmedGap(newConfirmed - oldConfirmed);
			result.get(i).setDeathsGap(newDeaths - oldDeaths);
			result.get(i).setRecoveredGap(newRecovered - oldRecovered);
		}
		
		// 모델에 데이터를 담아서 보낸다 (코로나정보, 갱신일자, 국가명)
		model.addAttribute("worldList", result);
		if(result.size() > 0) {
			String[] updateDate = result.get(0).getUpdatedDate().split("-");
			model.addAttribute("updatedDate",  updateDate[0] + "년" + updateDate[1] + "월" + updateDate[2] + "일 기준");
			model.addAttribute("countryName", result.get(0).getCountryName());
		} else {
			model.addAttribute("updatedDate", "");
			model.addAttribute("countryName", "");
		}
		
		return "detail";
	}
	
}
