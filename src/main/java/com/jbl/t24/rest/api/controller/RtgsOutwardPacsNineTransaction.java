package com.jbl.t24.rest.api.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.config.HostIpHandle;
import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.service.rtgs.FtHandlerServiceN;
import com.jbl.t24.rest.api.service.rtgs.RtgsInfoPacsNineOutwardService;
import com.jbl.t24.rest.api.enums.ResponseStatus;

@RestController
@CrossOrigin
@RequestMapping("/rtgs_out")
@Validated
public class RtgsOutwardPacsNineTransaction {

	@Autowired
	private FtHandlerServiceN ftHandlerServiceN;

	@Autowired
	private RtgsInfoPacsNineOutwardService outwardService;


	@Value("${use.fixed.issue.date}")
	 private Boolean useFixedIssueDate;
	 @Value("${fixed.issue.date}")
	private String fixedIssueDate;
	@PostMapping(value = "/pacs09/outward", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> rtgsTrasferOutward(@Valid @RequestBody RtgsInfoPacsNineOutward rtgsInfoPacsNineOut,
			HttpServletRequest httpServletRequest) throws Exception {

		String uniqueFtId = rtgsInfoPacsNineOut.getUniqueOutwardRtgsId();
		String coCode = rtgsInfoPacsNineOut.getCoCode();
		String companyCode = rtgsInfoPacsNineOut.getCompanyCode() + coCode;
		rtgsInfoPacsNineOut.setCompanyCode(companyCode);
		String txCategory = rtgsInfoPacsNineOut.getTxCategory();
		String txType = RtgsTransactionConstants.rtgsConstants.get(txCategory).getTransactionType();
		rtgsInfoPacsNineOut.setTxType(txType);
		rtgsInfoPacsNineOut.setCategoryCode(RtgsTransactionConstants.rtgsConstants.get(txCategory).getCode());
		String debitAccNo = rtgsInfoPacsNineOut.getDebitAccNo().trim();
		String currency = rtgsInfoPacsNineOut.getCurrency();
		// String debitAmount = String.format("%.2f", rtgsInfoPacsNineOut.getDebitAmount());
		String debitAmount =  rtgsInfoPacsNineOut.getDebitAmountStr().trim();
		String creditAccNo = rtgsInfoPacsNineOut.getCreditAccNo().trim();
		String debitDetails = rtgsInfoPacsNineOut.getDebitDetails().trim();
		String creditDetails = rtgsInfoPacsNineOut.getCreditDetails().trim();
		String commissionCode = rtgsInfoPacsNineOut.getCommissionCode();
		String commissionType = rtgsInfoPacsNineOut.getCommissionType();
		boolean isFc = rtgsInfoPacsNineOut.getCurrency().contains("BDT") ? false : true;
		rtgsInfoPacsNineOut.setIsFc(isFc);
		String otherInfo = rtgsInfoPacsNineOut.getOtherInfo();
		String billDescription = rtgsInfoPacsNineOut.getBillDescription();
		String lcNumber = rtgsInfoPacsNineOut.getLcNumber();
		String partyName = rtgsInfoPacsNineOut.getPartyName();
		String instructionInfo = rtgsInfoPacsNineOut.getInstructionInfo();
		String tradeFinanceInfo = rtgsInfoPacsNineOut.getTradeFinanceInfo();
		
		rtgsInfoPacsNineOut.setDebitAmountStr(debitAmount);
		rtgsInfoPacsNineOut.setDebitAccNo(debitAccNo);
		rtgsInfoPacsNineOut.setCreditAccNo(creditAccNo);
		rtgsInfoPacsNineOut.setDebitDetails(debitDetails);
		rtgsInfoPacsNineOut.setCreditDetails(creditDetails);

		// String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
		// Timestamp t = new Timestamp(new Date().getTime());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                                                       .withZone(ZoneId.systemDefault());
		String issueDate = formatter.format(Instant.now());													   
		
		// Timestamp t = new Timestamp(new Date().getTime());
		Instant instant = Instant.now();
		Timestamp t =Timestamp.from(instant);
		rtgsInfoPacsNineOut.setIssueDate(t);

		Objects.requireNonNull(uniqueFtId);
		Objects.requireNonNull(coCode);
		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(currency);

		// String requestOFS = "";

		if(useFixedIssueDate && fixedIssueDate != null && !fixedIssueDate.trim().isEmpty())
		{
			issueDate = fixedIssueDate;
		}
		

		RtgsInfoPacsNineOutward refrenceTx = null;

		if(rtgsInfoPacsNineOut.getRefUniqueOutwardRtgsId() != null && 
		!rtgsInfoPacsNineOut.getRefUniqueOutwardRtgsId().isBlank()){
			refrenceTx = outwardService.findByUniqueOutwardRtgsId(rtgsInfoPacsNineOut.getRefUniqueOutwardRtgsId());

			if(refrenceTx !=null ){
				debitDetails = "RERVESE AGAINST "+refrenceTx.getCbsFtno();
				refrenceTx.setStatus(4);
				refrenceTx.setReverseDate(Timestamp.valueOf(LocalDateTime.now()));
				outwardService.save(refrenceTx);
			}else{
				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
				ResponseStatus.FOURZ31.getText(), ResponseStatus.FOURZ31.getValue());
				return ResponseEntity.badRequest().body(jwtErrorResponse);
			}

		}

		

		

		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		rtgsInfoPacsNineOut.setHostname(hostIpData.get("host"));
		rtgsInfoPacsNineOut.setIp(hostIpData.get("remoteAddr"));

		final String requestOFS = String.format(OfsSources.REQUEST_OFS_STRING_P9, companyCode, txType, debitAccNo,
				currency, debitAmount, issueDate, creditAccNo, debitDetails, creditDetails, commissionCode,
				commissionType, uniqueFtId, otherInfo, billDescription, lcNumber, partyName, instructionInfo,
				tradeFinanceInfo);

//		ResponseEntity<?> response = ftHandlerService.handleRtgsOutwardPacsNineTransaction(requestOFS, rtgsInfoPacsNineOut, httpServletRequest);
		ResponseEntity<?> response = ftHandlerServiceN.handleFtTransaction(requestOFS, rtgsInfoPacsNineOut, uniqueFtId);

		System.out.println(response.getBody());
		// return ResponseEntity.status(HttpStatus.OK).body(response);
		return response;

	}

}