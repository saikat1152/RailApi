package com.jbl.t24.rest.api.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jbl.t24.rest.api.common.model.IResponseCountByDate;
import com.jbl.t24.rest.api.common.model.IResponseTxCountByDate;
//import com.jbl.t24.rest.api.model.RemitterInfo;
//import com.jbl.t24.rest.api.service.RemitterInfoService;

@RestController
@RequestMapping("/rest-report")
public class RestReportController {
//	
//	@Autowired
//	RemitterInfoService remitterInfoService;
//	
//	@GetMapping("/all-exchange")
//	public List<IResponseTxCountByDate> getAllByDate(@RequestParam String responseAt) throws Exception {
//		LocalDate date = LocalDate.parse(responseAt);
//		System.out.println("response date "+ responseAt + " Date "+date);
//		Timestamp ts = Timestamp.valueOf(date.atStartOfDay());
//		System.out.println(ts);
//		return remitterInfoService.findNumberOfTxAtDate( ts);
//	}
//	
//	@GetMapping("/exchangewise")
//	public List<IResponseCountByDate> getExchangeWiseDate(@RequestParam String responseAt, @RequestParam String exchangeHouseName) throws Exception {
//		LocalDate date = LocalDate.parse(responseAt);
//		Timestamp ts = Timestamp.valueOf(date.atStartOfDay());
//		return remitterInfoService.findNumberOfTrByExchangeAndDateWise(ts, exchangeHouseName);
//	}
//	
//	/*
//	 * @GetMapping("branchwise-date") public List<RemitterInfo>
//	 * branchWiseDate(@RequestParam String responseAt, @RequestParam String
//	 * branchCode) { return
//	 * remitterInfoService.findNumberOfTrByBrCodeAndDate(responseAt, branchCode); }
//	 */
//	
//	@GetMapping("/branchwise")
//	public List<IResponseCountByDate> getByBranchCodeAndDate(@RequestParam(name="responseAt") String responseAt, @RequestParam(name="branchCode") String branchCode) throws Exception{
//		//System.out.println(remitterInfoService.findByDummy(responseAt, branchCode).toString());
//		//Object obj[] = 
//		LocalDate date = LocalDate.parse(responseAt);
//		Timestamp ts = Timestamp.valueOf(date.atStartOfDay());
//		return remitterInfoService.findByBranchCodeAndDate(ts, branchCode);
//	}

}
