package com.jbl.t24.rest.api.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbl.t24.rest.api.common.model.AccountInfo;
import com.jbl.t24.rest.api.common.model.AccountInfoNotFound;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.config.HostIpHandle;
import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.constant.JwtErrorsCBS;
import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.constant.RailOfsSources;
import com.jbl.t24.rest.api.constant.RtgsTruncateString;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.enums.utils.ErrorMessageGenerator;
import com.jbl.t24.rest.api.model.rail.RailFundTransfer;
import com.jbl.t24.rest.api.model.rail.RailLockAccount;
import com.jbl.t24.rest.api.model.rail.RailUnlockAccount;
import com.jbl.t24.rest.api.model.rtgs.AccountQueryInfo;

import com.jbl.t24.rest.api.service.rail.RailServiceHandler;
import com.jbl.t24.rest.api.service.rtgs.AccountQueryInfoService;

import com.jbl.t24.rest.api.tccUtility.TccUtility;

@RestController
@CrossOrigin
@RequestMapping("/rail")
@Validated
public class RailTransferController {


	Logger logger = LogManager.getLogger(RailTransferController.class);

	@Autowired
	AccountQueryInfoService accountQueryInfoService;

	@Autowired
    RailServiceHandler railService;

	// @Value("${test.image.path}")
	@Value("${image.path}")
	private String ROOT_PATH;

	@Value("${channel.name.trx}")
	private String transactionChannel;
	@Value("${channel.name.enq}")
	private String enquiryChannel;



	@PostMapping(value = "/acct-check", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> accountInfoNew(@Valid @RequestBody AccountQueryInfo accountQueryInfo,
			HttpServletRequest httpServletRequest) throws Exception {

		logger.info("Account Query Check Started");
		// String accountNo = requestParams.get("accountNo");
		String accountNo = accountQueryInfo.getAccountNumber();
		Objects.requireNonNull(accountNo);

		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}
		String host = httpServletRequest.getRemoteHost();
		accountQueryInfo.setIp(remoteAddr);
		accountQueryInfo.setHostname(host);

		// TccUtility tccUtility = new TccUtility("ISOLIST2");
		TccUtility tccUtility = new TccUtility(enquiryChannel);

		String requestOFS = String.format(RailOfsSources.ACCOUNT_ENQUIRY_STRING, accountNo);
		String responseData = tccUtility.sendRequest(requestOFS);

		// logger.info("---Account Query Response Data:----" +responseData);
		accountQueryInfo.setOfsResponse(RtgsTruncateString.truncateString(responseData, 2000));

		if (responseData.startsWith("40")) {
			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
			jwtErrorResponse.setMessage("JBOSS server is unreacheable");
			accountQueryInfo.setStatus("3");
			logger.info("Account Query Error " + jwtErrorResponse.getMessage() + "account No:  " + accountNo);

			String response = Mapper.mapToJsonString(jwtErrorResponse);
			accountQueryInfo.setResponseData(response);

			accountQueryInfoService.save(accountQueryInfo);
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {
			try {

				String[] spiltData = responseData.split("\"");
				String[] secondPart = spiltData[1].split("\\*");
				String message = secondPart[0];
				String statusCode = secondPart[1];
				if (statusCode.equals("200")) {
					String flagValue = secondPart[2];
					if (flagValue.equals("1")) {
						AccountInfo accountInfo = new AccountInfo();
						accountInfo.setAccountNo(secondPart[3]);
						accountInfo.setLeagcyAccountNo(secondPart[4]);
						accountInfo.setAccountTitle(secondPart[5]);
						 accountInfo.setCusMobNum(secondPart[6].split(" ")[0]);
						
						String[] allIdNumbersNames = secondPart[8].split("\n");
						String[] allIds = secondPart[7].split("\n");

						// accountInfo.setCusNidNum(secondPart[16]);
						if("NO DATA".equals(secondPart[7])){
							accountInfo.setCusNidNum(secondPart[16]);
						}else{
							accountInfo.setCusNidNum(secondPart[7]);
						}
						accountInfo.setCoCode(secondPart[8].replace("BD001", ""));
						accountInfo.setCoName(secondPart[10].replace("\"", ""));
						
      /**
						accountInfo.setAccBalance(secondPart[11]);

						try {
							accountInfo.setPostingRestrictionCode(Integer.parseInt(secondPart[12]));
						} catch (Exception e) {
							accountInfo.setPostingRestrictionCode(0);
						}

						accountInfo.setPostingResTrictionType(secondPart[13].replace("\"", ""));

						accountInfo.setPostingRestrictionDescription(secondPart[14].replace("\"", ""));
						accountInfo.setCurrency(secondPart[15]);
						accountInfo.setValid(true);
						
						*/
						
						accountInfo.setMessage(ResponseStatus.TWOZ0.getText());
						accountInfo.setResponseCode(ResponseStatus.TWOZ0.getValue());
/**
			
						accountInfo.setAccountType(secondPart[17]);
						accountInfo.setBinNumber(secondPart[18]);
						accountInfo.setAccountCatCode(secondPart[19]);

						// if(secondPart.length >= 21){
						if (secondPart[20].equals("CLOSED")) {
							// boolean isClosed = secondPart[20].equals("CLOSED");
							accountInfo.setClosed(true);
						}

						if (flagValue.equals("1")) {
							accountInfo.setInactive(false);
						} else {
							accountInfo.setInactive(true);
						}
     

						 * 
	*/
			
						ObjectMapper mapper = new ObjectMapper();
						String accountInfoStr = mapper.writeValueAsString(accountInfo);

						/**
						 * Saving the information into the database
						 */
						accountQueryInfo.setAccountDetails(accountInfoStr);
						accountQueryInfo.setStatus("2");
						String response = Mapper.mapToJsonString(accountQueryInfo);
						accountQueryInfo.setResponseData(response);

						accountQueryInfoService.save(accountQueryInfo);
						logger.info("Account Query Done", Mapper.mapToJsonString(accountQueryInfo));
						// System.out.println(accountQueryInfo);
						return ResponseEntity.status(HttpStatus.OK).body(accountInfo);

					} else {
						accountQueryInfo.setStatus("3");

						JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
								ResponseStatus.FOURZ13.getText(), ResponseStatus.FOURZ13.getValue());
						AccountInfoNotFound accountInfoNotFound = new AccountInfoNotFound(message,
								ResponseStatus.FOURZ13.getValue(), false);
						logger.info("Account Query Error " + jwtErrorResponse.getMessage());
						String response = Mapper.mapToJsonString(jwtErrorResponse);
						accountQueryInfo.setResponseData(response);

						accountQueryInfoService.save(accountQueryInfo);

						return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
					}

				} else {
					accountQueryInfo.setStatus("3");

					JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
							ResponseStatus.FOURZ7.getText(), ResponseStatus.FOURZ7.getValue());

					AccountInfoNotFound accountInfoNotFound = new AccountInfoNotFound(message,
							ResponseStatus.FOURZ13.getValue(), false);

					String response = Mapper.mapToJsonString(jwtErrorResponse);
					accountQueryInfo.setResponseData(response);

					accountQueryInfoService.save(accountQueryInfo);
					logger.info("Account Query Error " + jwtErrorResponse.getMessage());
					return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
				}

			} catch (Exception e) {

				accountQueryInfo.setStatus("3");

				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
						ErrorMessageGenerator.getErrorMessage(e), ResponseStatus.FIVEZ0.getValue());

				AccountInfoNotFound accountInfoNotFound = new AccountInfoNotFound(ResponseStatus.FIVEZ0.getText(),
						ResponseStatus.FIVEZ0.getValue(), false);
				logger.info("Verification Response :: {}" + responseData);
				logger.error("Account Query Error " + jwtErrorResponse.getMessage());
				String response = Mapper.mapToJsonString(jwtErrorResponse);
				accountQueryInfo.setResponseData(response);

				accountQueryInfoService.save(accountQueryInfo);

				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
			}

		}

	}



	@PostMapping(value = {"/transfer"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> railFundTrasfer(@Valid @RequestBody RailFundTransfer railFundTransfer,
			HttpServletRequest httpServletRequest) throws Exception {

				logger.debug("Rail Fund Transfer On Process");

				String uniqueFtId = railFundTransfer.getUniqueftId();
				String coCode = railFundTransfer.getCoCode();
				String companyCode = railFundTransfer.getCompanyCode() + coCode;
				railFundTransfer.setCompanyCode(companyCode);
				String txType = railFundTransfer.getTxType();
				String debitAccNo = railFundTransfer.getDebitAccNo();
				String currency = railFundTransfer.getCurrency();
				String debitAmount = String.format("%.2f", railFundTransfer.getDebitAmount());
				String creditAccNo = railFundTransfer.getCreditAccNo();
				String debitDetails = railFundTransfer.getDebitDetails().trim();
				String creditDetails = railFundTransfer.getCreditDetails().trim();

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                                                       .withZone(ZoneId.systemDefault());
        String issueDate = formatter.format(Instant.now());     
        Timestamp t =Timestamp.from(Instant.now());
		railFundTransfer.setIssueDate(t);


		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		String host = httpServletRequest.getRemoteHost();

		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}

		railFundTransfer.setIp(remoteAddr);
		railFundTransfer.setHostname(host);

		// final String markerRequestOFS;

		// markerRequestOFS = String.format(RailOfsSources.MARKER_FT_STRING, companyCode, debitAccNo, companyCode);

		final String requestOFS;

		requestOFS = String.format(RailOfsSources.FUND_TRANSFER_STRING, companyCode, txType,currency, debitAccNo,
		creditAccNo, debitAmount, debitDetails, creditDetails, coCode, uniqueFtId);

		

		ResponseEntity<?> response = null;

		try {
			response= railService.handleFtTransaction(requestOFS, railFundTransfer);
		} catch (Exception e) {
			//making the status failed in case of any un handled transaction situation.
			// eftnInfoOut.setStatus(FtStatus.FAILED.getValue());
			// eftInfoOutwardService.save(eftnInfoOut);
			throw e;
		}

		// System.out.println(response.getBody());
		logger.info("Rail Fund Transfer Process Finished");
		return response;

		}

}
