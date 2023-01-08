package com.jbl.t24.rest.api.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.validation.Valid;

import org.bouncycastle.util.encoders.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.ServletContextResource;

import com.google.common.collect.Lists;
// import com.google.common.net.HttpHeaders;
import com.jbl.t24.rest.api.common.model.AccountInfo;
import com.jbl.t24.rest.api.common.model.AccountInfoNotFound;
import com.jbl.t24.rest.api.common.model.FtResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.custom.validation.CustomValidation;
import com.jbl.t24.rest.api.enums.CBSResponseStr;
import com.jbl.t24.rest.api.enums.RemitterStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.BeftnInwardInfo;
import com.jbl.t24.rest.api.service.BeftnInfoService;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.servlet.ServletContext;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("/beftn_in")
@Slf4j
@Validated
public class BeftnInwardTransaction {

	@Autowired
	private BeftnInfoService beftnInfoService;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/account-check", method = RequestMethod.GET)
	public ResponseEntity<?> accountInfo(@Valid @RequestParam Map<String, String> requestParams,
			HttpServletRequest httpServletRequest) throws Exception {

		String accountNo = requestParams.get("accountNo");
		Objects.requireNonNull(accountNo);
		log.info("account number provided " + accountNo);
		CustomValidation customValidation = new CustomValidation();

		if (customValidation.isBlankString(accountNo)) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
					ResponseStatus.FOURZ8.getText(), ResponseStatus.FOURZ8.getValue());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		}

		if (!customValidation.isAccountLengthValid(accountNo)) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.BAD_REQUEST,
					ResponseStatus.FOURZ12.getText(), ResponseStatus.FOURZ12.getValue());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		}

		TccUtility tccUtility = new TccUtility();

		// new enquiry
		// ENQUIRY.SELECT,,,E.JBL.API.BBR,ACCOUNT.NUMBER:EQ=

		String requestOFS = "ENQUIRY.SELECT,,,E.JBL.API,ACCOUNT.NUMBER:EQ=" + accountNo;
		// String requestOFS = "ENQUIRY.SELECT,,,E.JBL.API,ACCOUNT.NUMBER:EQ=" +
		// accountNo;
		// requestOFS = "ENQUIRY.SELECT,,MONWAR1/Ss1234567*,CURRENCY.LIST";
		// ENQUIRY.SELECT,,MONWAR1/FFSEFDEEE,E.JBL.API,ACCOUNT.NUMBER = 0100001425310

		// log.info(requestOFS);
		String responseData = tccUtility.sendRequest(requestOFS);
	
		if (responseData.equals("400") || responseData.equals("401") || responseData.equals("402")
				|| responseData.equals("403")) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ3.getText(),
					ResponseStatus.FIVEZ3.getValue());

			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {
			try {
				String[] spiltData = responseData.split(",");
				String[] secondPart = spiltData[2].split("\\*");
				String message = secondPart[0];
				String statusCode = secondPart[1];
				if (statusCode.equals("200")) {
					String flagValue = secondPart[2];
					if (flagValue.equals("1")) {
						AccountInfo accountInfo = new AccountInfo();
						accountInfo.setAccountNo(secondPart[3]);
						accountInfo.setLeagcyAccountNo(secondPart[4]);
						accountInfo.setAccountTitle(secondPart[5]);
						// accountInfo.setMobile(secondPart[6]);
						// accountInfo.setNid(secondPart[7]);
						accountInfo.setCoCode(secondPart[8].replace("BD001", ""));
						accountInfo.setCoName(secondPart[9].replace("\"", ""));
						accountInfo.setValid(true);
						accountInfo.setMessage(ResponseStatus.TWOZ0.getText());
						accountInfo.setResponseCode(ResponseStatus.TWOZ0.getValue());
						return ResponseEntity.status(HttpStatus.OK).body(accountInfo);
					} else {
						AccountInfoNotFound accountInfoNotFound = new AccountInfoNotFound(
								ResponseStatus.FOURZ13.getText(), ResponseStatus.FOURZ13.getValue(), false);
						return ResponseEntity.status(HttpStatus.OK).body(accountInfoNotFound);
					}
				} else {
					AccountInfoNotFound accountInfoNotFound = new AccountInfoNotFound(ResponseStatus.FOURZ7.getText(),
							ResponseStatus.FOURZ7.getValue(), false);
					return ResponseEntity.status(HttpStatus.OK).body(accountInfoNotFound);
				}

			} catch (Exception e) {
				AccountInfoNotFound accountInfoNotFound = new AccountInfoNotFound(ResponseStatus.FIVEZ0.getText(),
						ResponseStatus.FIVEZ0.getValue(), false);
				return ResponseEntity.status(HttpStatus.OK).body(accountInfoNotFound);
			}
		}

	}

	// @HystrixCommand(fallbackMethod = "getInwardFallBack",
	// commandProperties = {
	// @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
	// value = "000"),
	//// @HystrixProperty(name = "circuitBreaker.circuitBreaker.enabled", value =
	// "True"),
	// @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value =
	// "5"),
	// @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value =
	// "50"),
	// @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value =
	// "5000"),
	// }
	//
	// )

	@RequestMapping(value = "/inward", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> beftnTrasferInward(@Valid @RequestBody BeftnInwardInfo beftnInfo,
			HttpServletRequest httpServletRequest) throws Exception {

		String debitAccNo = beftnInfo.getDebitAccNo();
		// String currency = beftnInfo.getCurrency();
		String creditAccNo = beftnInfo.getCreditAccNo();
		Double debitAmount = beftnInfo.getDebitAmount();
		String narrative = beftnInfo.getNarrative();
		Integer version = beftnInfo.getVersion();

		// System.out.println("debitAccNo "+ debitAccNo
		//// +" debitCurrency " + currency
		// + " creditAccNo " + creditAccNo
		// + " debitAmount " + debitAmount
		// + " Narrative " + narrative
		// + " version " + version);

		Objects.requireNonNull(debitAccNo);
		// Objects.requireNonNull(currency);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(narrative);
		Objects.requireNonNull(version);

		CustomValidation customValidation = new CustomValidation();

		// if (customValidation.isBlankString(debitAccNo)
		// || customValidation.isBlankString(creditAccNo)
		// || customValidation.isBlankString(narrative)
		// || customValidation.isBlankDouble(debitAmount)
		// || customValidation.isBlankInteger(version)) {
		//
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ18.getText(), ResponseStatus.FOURZ18.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		//
		// }
		//
		// if (!customValidation.isRequiredLength(narrative, 16)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ9.getText(), ResponseStatus.FOURZ9.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }
		//
		// // Debit Account and Credit Account Length Check
		//
		// if (!customValidation.isAccountLengthValid(debitAccNo)
		// || !customValidation.isAccountLengthValid(creditAccNo)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ12.getText(), ResponseStatus.FOURZ12.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }
		//
		// //Version Validation Checking
		// //1 for Credit, 2 for Debit, 3 for Return
		// if(!customValidation.isVersionValidInward(version)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ20.getText(), ResponseStatus.FOURZ20.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }

		String requestOFS = "";
		// FUNDS.TRANSFER,BEFTN
		if (version == 1) {
			requestOFS = "FUNDS.TRANSFER,BEFTN/I/PROCESS//0,BD0010102"
					+ ",,DEBIT.CURRENCY=BDT,"
					+ "DEBIT.ACCT.NO=" + debitAccNo + ","
					+ "CREDIT.ACCT.NO=" + creditAccNo + ","
					+ "DEBIT.AMOUNT=" + debitAmount + ","
					+ "FT.CR.DETAILS=" + narrative;
		} else if (version == 2) {
			requestOFS = "FUNDS.TRANSFER,BEFTN.DR/I/PROCESS//0,BD0010102" // BD0010102 thi is the branch code. will be
																			// changed accordingly
					+ ",,DEBIT.CURRENCY=BDT,"
					+ "DEBIT.ACCT.NO=" + debitAccNo + ","
					+ "CREDIT.ACCT.NO=" + creditAccNo + ","
					+ "DEBIT.AMOUNT=" + debitAmount + ","
					+ "FT.DR.DETAILS=" + narrative;
		}

		else {
			System.out.println("OFS Message Error Dise. Generate hoy nai");
		}

		// 102 - FT.CR.DETAILS
		// 101 - FT.DR.DETAILS
		// + "DEBIT.VALUE.DATE=20200201";

		BeftnInwardInfo beftnInfoSave = null;

		// check this beftn inward if already exist or not in database by narrative
		// Id, if exist and success just return error
		BeftnInwardInfo beftnInfoExist = beftnInfoService.findByNarrative(narrative);

		if (beftnInfoExist != null) {
			int statusExist = beftnInfoExist.getStatus();
			if (statusExist == RemitterStatus.SUCCESS.getValue()) {
				FtResponse ftResponse = new FtResponse();
				ftResponse.setStatus(HttpStatus.OK);
				ftResponse.setNarrative(narrative);
				String ofsResponse = beftnInfoExist.getOfsResponse();
				String[] spiltDataOfs = ofsResponse.split(",");
				String[] firstPart = spiltDataOfs[0].split("/");
				String statusFlag = firstPart[2];
				if (statusFlag.equals("1")) {
					String ftRef = firstPart[0];
					ftResponse.setFtRef(ftRef);
					ftResponse.setMessage(ResponseStatus.TWOZ2.getText());
					ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());

					// if (ofsResponse.contains(CBSResponseStr.accountNaturePersonal.getText())) {
					// ftResponse.setCreditAccountCategory(AccountNature.PERSONAL.getValue());
					// ftResponse.setAdditionalInfo(AccountNature.PERSONAL.getText());
					// } else {
					// ftResponse.setCreditAccountCategory(AccountNature.OTHER.getValue());
					// ftResponse.setAdditionalInfo(AccountNature.OTHER.getText());
					// }
					return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
				} else if (ofsResponse.contains(CBSResponseStr.alreadySuccessDuplicate.getText())) {

					String duplicateSuccess[] = ofsResponse.split(",");
					String[] secondPart = duplicateSuccess[1].split("-");
					String ftRef = secondPart[2];
					String category = secondPart[3];
					// if (category.equals("Personal")) {
					// ftResponse.setCreditAccountCategory(AccountNature.PERSONAL.getValue());
					// } else {
					// ftResponse.setCreditAccountCategory(AccountNature.OTHER.getValue());
					// }
					ftResponse.setFtRef(ftRef);

					ftResponse.setMessage(ResponseStatus.TWOZ2.getText());
					ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());
					return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
				} else {
					JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
							ResponseStatus.TWOZ3.getText(), ResponseStatus.TWOZ3.getValue());
					return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
				}
			}
			beftnInfoSave = beftnInfoExist;

		} else {

			// save remmitter data into RemitterInfo table

			String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null) {
				remoteAddr = httpServletRequest.getRemoteAddr();
			}
			String host = httpServletRequest.getRemoteHost();
			beftnInfo.setIp(remoteAddr);
			beftnInfo.setHostName(host);

			beftnInfo.setStatus(RemitterStatus.PENDING.getValue());
			beftnInfo.setOfsRequest(requestOFS);
			// remitterInfo.setRequestAt(new Timestamp(System.currentTimeMillis()));
			// log.info(requestOFS);
			beftnInfoSave = beftnInfoService.save(beftnInfo);
			// log.info("Saved Data");
			// System.out.println("remitterInfoSave: " + remitterInfoSave);

		}
		TccUtility tccUtility = new TccUtility();

		// TccUtilityNew tccUtility = new TccUtilityNew("ISOLIST2");

		// String responseData = tccUtility.sendRequest(requestOFS);
		String responseData = beftnInfoService.getTccOfsResponse(requestOFS);
		// CompletableFuture<String> response =
		// beftnInfoService.getTccOfsResponse(requestOFS);
		// String responseData = response.get();

		//
		// --------------------
		// RequestOfs requestOfs = new RequestOfs();
		// requestOfs.setReqOfs(requestOFS);
		// String responseData = beftnInfoService.getTccOfsResponse(requestOfs);
		// -------------------------------
		//

		beftnInfoSave.setOfsResponse(responseData);
		// remitterInfoSave.setResponseAt(new Timestamp(System.currentTimeMillis()));
		System.out.println("responseData " + responseData);

		if (responseData.equals("400") || responseData.equals("401") || responseData.equals("402")
				|| responseData.equals("403")) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ3.getText(),
					ResponseStatus.FIVEZ3.getValue());
			beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
			beftnInfoService.save(beftnInfoSave);
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {
			FtResponse ftResponse = new FtResponse();
			ftResponse.setStatus(HttpStatus.OK);
			ftResponse.setNarrative(narrative);

			try {

				String[] spiltData = responseData.split(",");
				String[] firstPart = spiltData[0].split("/");
				String statusFlag = firstPart[2];

				if (statusFlag.equals("1")) {
					String ftRef = firstPart[0];
					ftResponse.setFtRef(ftRef);
					ftResponse.setMessage(ResponseStatus.TWOZ0.getText());
					ftResponse.setResponseCode(ResponseStatus.TWOZ0.getValue());

					// if (responseData.contains(CBSResponseStr.accountNaturePersonal.getText())) {
					// ftResponse.setCreditAccountCategory(AccountNature.PERSONAL.getValue());
					// ftResponse.setAdditionalInfo(AccountNature.PERSONAL.getText());
					// } else {
					// ftResponse.setCreditAccountCategory(AccountNature.OTHER.getValue());
					// ftResponse.setAdditionalInfo(AccountNature.OTHER.getText());
					// }
					beftnInfoSave.setStatus(RemitterStatus.SUCCESS.getValue()); // Later needs to be changed at Remitter
																				// Status
					beftnInfoService.save(beftnInfoSave);
				} else {

					if (responseData.contains(CBSResponseStr.alreadySuccessDuplicate.getText())) {

						String duplicateSuccess[] = responseData.split(",");
						String[] secondPart = duplicateSuccess[1].split("-");
						String ftRef = secondPart[2];
						String category = secondPart[3];
						// if (category.equals("Personal")) {
						// ftResponse.setCreditAccountCategory(AccountNature.PERSONAL.getValue());
						// } else {
						// ftResponse.setCreditAccountCategory(AccountNature.OTHER.getValue());
						// }
						ftResponse.setFtRef(ftRef);

						ftResponse.setMessage(ResponseStatus.TWOZ2.getText());
						ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());
						beftnInfoSave.setStatus(RemitterStatus.SUCCESS.getValue());
						beftnInfoService.save(beftnInfoSave);
					} else if (responseData.contains(CBSResponseStr.failedDuplicate.getText())) {
						ftResponse.setMessage(ResponseStatus.TWOZ3.getText());
						ftResponse.setResponseCode(ResponseStatus.TWOZ3.getValue());
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
						beftnInfoService.save(beftnInfoSave);
					} else if (responseData.contains(CBSResponseStr.debitAccountMissing.getText())
							|| responseData.contains(CBSResponseStr.customDebitAccountMissing.getText())) {
						ftResponse.setMessage(ResponseStatus.FOURZ10.getText());
						ftResponse.setResponseCode(ResponseStatus.FOURZ10.getValue());
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
						beftnInfoService.save(beftnInfoSave);
					} else if (responseData.contains(CBSResponseStr.creditAccountMissing.getText())
							|| responseData.contains(CBSResponseStr.customCreditAccountMissing.getText())) {
						ftResponse.setMessage(ResponseStatus.FOURZ7.getText());
						ftResponse.setResponseCode(ResponseStatus.FOURZ7.getValue());
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
						beftnInfoService.save(beftnInfoSave);
					}
					// else if
					// (responseData.contains(CBSResponseStr.customDebitAccountCoCodeMissing.getText()))
					// {
					// ftResponse.setMessage(ResponseStatus.FOURZ11.getText());
					// ftResponse.setResponseCode(ResponseStatus.FOURZ11.getValue());
					// beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
					// beftnInfoService.save(beftnInfoSave);
					// }
					else if (responseData.contains(CBSResponseStr.debitAccountLessBalance.getText())) {
						ftResponse.setMessage(ResponseStatus.FOURZ14.getText());
						ftResponse.setResponseCode(ResponseStatus.FOURZ14.getValue());
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
						beftnInfoService.save(beftnInfoSave);
					} else {
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
						beftnInfoService.save(beftnInfoSave);
						ftResponse.setMessage(ResponseStatus.TWOZ1.getText());
						ftResponse.setResponseCode(ResponseStatus.TWOZ1.getValue());
					}
					ftResponse.setAdditionalInfo(responseData);
				}
				return ResponseEntity.status(HttpStatus.OK).body(ftResponse);

			} catch (Exception e) {
				beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
				beftnInfoService.save(beftnInfoSave);
				ftResponse.setMessage(ResponseStatus.TWOZ1.getText());
				ftResponse.setResponseCode(ResponseStatus.TWOZ1.getValue());
				ftResponse.setAdditionalInfo(responseData);
				return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
			}
		}

	}

	@RequestMapping(value = "/inward-bulk", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> beftnTrasferInwardList(@Valid @RequestBody List<BeftnInwardInfo> beftnInfolist,
			HttpServletRequest httpServletRequest) throws Exception {

		beftnInfolist.stream().forEach(e -> System.out.println(e.toString()));
		List<List<BeftnInwardInfo>> partitionedList = Lists.partition(beftnInfolist, 10);
		List<Object> responseBodyList = new ArrayList<Object>();
		partitionedList.stream().parallel().forEach(e -> {
			try {
				System.out.println(e); // .get()
				responseBodyList.addAll(beftnInfoService.getBulkOfsResponse(e, httpServletRequest).get());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		// return beftnInfoService.getBulkOfsResponse(beftnInfolist,
		// httpServletRequest).get();
		System.out.println(responseBodyList);
		return ResponseEntity.status(HttpStatus.OK).body(responseBodyList);
	}

}
