package com.corona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corona.domain.WorldDailyReport;
import com.corona.mapper.WorldDailyReportMapper;

@Service
public class WorldDailyReportServiceImpl implements WorldDailyReportService {
	
	@Autowired
	private WorldDailyReportMapper mapper;

	@Override
	public List<WorldDailyReport> getWorldList() {
		return mapper.getWorldList();
		
	}

	@Override
	public void insertWorldList(List<WorldDailyReport> info) {
		mapper.insertWorldList(info);
	}

}