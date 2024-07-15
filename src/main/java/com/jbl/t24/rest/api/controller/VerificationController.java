package com.jbl.t24.rest.api.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
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
import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.constant.JwtErrorsCBS;
import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.constant.RtgsTruncateString;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.enums.utils.ErrorMessageGenerator;
import com.jbl.t24.rest.api.model.rtgs.AccountQueryInfo;
import com.jbl.t24.rest.api.model.rtgs.SignatureQueryInfo;
import com.jbl.t24.rest.api.service.rtgs.AccountQueryInfoService;
import com.jbl.t24.rest.api.service.rtgs.SignQueryInfoService;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@RestController
@CrossOrigin
@RequestMapping("/verify")
@Validated
public class VerificationController {

	// Logger logger = LoggerFactory.getLogger("splunk.logger");

	Logger logger = LogManager.getLogger(VerificationController.class);

	@Autowired
	AccountQueryInfoService accountQueryInfoService;

	@Autowired
	SignQueryInfoService signQueryInfoService;

	@Value("${test.image.path}")
	// @Value("${live.image.path}")
	private String ROOT_PATH;

	@PostMapping(value = "/sign-check", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signatureImage(@Valid @RequestBody SignatureQueryInfo signatureQueryInfo,
			HttpServletRequest httpServletRequest) throws Exception {

		logger.info("Sign Query Started");
		String accountNo = signatureQueryInfo.getAccountNumber();
		Objects.requireNonNull(accountNo);

		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}
		String host = httpServletRequest.getRemoteHost();
		signatureQueryInfo.setIp(remoteAddr);
		signatureQueryInfo.setHostname(host);

		TccUtility tccUtility = new TccUtility("ISOLIST2");
		logger.info("Fetching CBS image data");
		String requestOFS = String.format(OfsSources.OFS_SIGN_CHECK_ENQUIRY, accountNo);
		String responseData = tccUtility.sendRequest(requestOFS);

		if (responseData.startsWith("40")) {
			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
			signatureQueryInfo.setStatus("3");
			logger.info("Account Query Error " + jwtErrorResponse.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		}

		String[] allData = responseData.split(",");

		String imageRelativePath = allData[2].replace("\"", "");
		String imageFormat = "";

		if (!imageRelativePath.toLowerCase().contains("no images to display")) {

			/*
			 * finding the correct imageRelativePath with the .jpg
			 */

			for (int i = 2; i <= allData.length; i++) {
				if (allData[i].toLowerCase().contains(".jpg") || allData[i].toLowerCase().contains(".png")
						|| allData[i].toLowerCase().contains(".jpeg") || allData[i].toLowerCase().contains(".bmp")) {
					imageRelativePath = allData[i].replace("\"", "");
					if ((allData[i].toLowerCase().contains(".jpg"))) {
						imageFormat = "jpg";
					} else if ((allData[i].toLowerCase().contains(".png"))) {
						imageFormat = "png";
					} else if ((allData[i].toLowerCase().contains(".jpeg"))) {
						imageFormat = "jpeg";
					} else if ((allData[i].toLowerCase().contains(".bmp"))) {
						imageFormat = "bmp";
					}
					break;
				}
			}

			imageRelativePath = imageRelativePath.substring(1, imageRelativePath.length());
			String imagePath = ROOT_PATH + imageRelativePath;

			/**
			 * Sending image as API return
			 */
			try {
				URL imageURL = new URL(imagePath);
				logger.info("Fetching Image from URL");
				BufferedImage signImage = ImageIO.read(imageURL);
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				ImageIO.write(signImage, imageFormat, bao);

				byte[] contents = bao.toByteArray();
				HttpHeaders headers = new HttpHeaders();

				headers.setCacheControl(CacheControl.noCache().getHeaderValue());

				byte[] encodedImage = Base64Utils.encode(contents);

				Map<String, byte[]> jsonMap = new HashMap<>();
				jsonMap.put("image", encodedImage);

				/**
				 * Saving query info into database
				 */

				signatureQueryInfo.setStatus("2");
				String response = Mapper.mapToJsonString(jsonMap);
				signatureQueryInfo.setResponseData(response.substring(0, 50));

				signQueryInfoService.save(signatureQueryInfo);
				logger.info("Image Fetching Done");
				return ResponseEntity.status(HttpStatus.OK).headers(headers).body(jsonMap);

			} catch (IOException e) {

				logger.info("Image Fetching Error: " + e.getStackTrace());

				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
						ResponseStatus.FOURZ25.getText(), ResponseStatus.FOURZ25.getValue());

				AccountInfoNotFound accountInfoNotFound = new AccountInfoNotFound(ResponseStatus.FOURZ25.getText(),
						ResponseStatus.FOURZ25.getValue(), false);
				logger.info("Image Not Found Error " + jwtErrorResponse.getMessage());

				signatureQueryInfo.setStatus("3");
				String response = Mapper.mapToJsonString(jwtErrorResponse);
				signatureQueryInfo.setResponseData(response);

				signQueryInfoService.save(signatureQueryInfo);

				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
			}
		} else {

			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FOURZ25.getText(),
					ResponseStatus.FOURZ25.getValue());

			AccountInfoNotFound accountInfoNotFound = new AccountInfoNotFound(ResponseStatus.FOURZ25.getText(),
					ResponseStatus.FOURZ25.getValue(), false);

			logger.info("Image Not Found Error " + jwtErrorResponse.getMessage());

			signatureQueryInfo.setStatus("3");
			String response = Mapper.mapToJsonString(jwtErrorResponse);
			signatureQueryInfo.setResponseData(response);

			signQueryInfoService.save(signatureQueryInfo);

			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		}

	}

	/**
	 * Method for saving image to local
	 */

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

		TccUtility tccUtility = new TccUtility("ISOLIST2");

		String requestOFS = String.format(OfsSources.OFS_ACC_ENQUIRY, accountNo);
		String responseData = tccUtility.sendRequest(requestOFS);
		// responseData = ",ALL.DATA::ALL.DATA,\"Successful~|200~|1~|0100001149495~|003336000642~|BANGLADESH PETROLEUM CORPORATION~|01701000003 01701000007~|767221182221~|TIN~|BD0010033~|S K Mujib Road Corp~|8346107661.96~|NO DATA~|NO DATA~|NO DATA~|BDT~|NA~|Special Notice Deposit~|6009\"";

		accountQueryInfo.setOfsResponse(RtgsTruncateString.truncateString(responseData, 2000));

		if (responseData.startsWith("40")) {
			// JwtErrorResponse jwtErrorResponse =
			// JwtErrorsCBS.getCbsJwtError(responseData);
			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
			jwtErrorResponse.setMessage("JBOSS server is unreacheable");
			accountQueryInfo.setStatus("3");
			logger.info("Account Query Error " + jwtErrorResponse.getMessage() + "account No:  " + accountNo);
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {
			try {

				String[] spiltData = responseData.split("\"");
				String[] secondPart = spiltData[1].split("\\~\\|");
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

						accountInfo.setCusNidNum(secondPart[16]);
						accountInfo.setCoCode(secondPart[9].replace("BD001", ""));
						accountInfo.setCoName(secondPart[10].replace("\"", ""));
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
						accountInfo.setMessage(ResponseStatus.TWOZ0.getText());
						accountInfo.setResponseCode(ResponseStatus.TWOZ0.getValue());
						accountInfo.setAccountType(secondPart[17]);
						accountInfo.setBinNumber(secondPart[18]);
						accountInfo.setAccountCatCode(secondPart[19]);

						if(secondPart.length >= 21){
							boolean isClosed = secondPart[20].equals("CLOSED");
							accountInfo.setClosed(isClosed);
						}

						if (flagValue.equals("1")) {
							accountInfo.setInactive(false);
						} else {
							accountInfo.setInactive(true);
						}

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
						System.out.println(accountQueryInfo);
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
				logger.info("Account Query Error " + jwtErrorResponse.getMessage());
				String response = Mapper.mapToJsonString(jwtErrorResponse);
				accountQueryInfo.setResponseData(response);

				accountQueryInfoService.save(accountQueryInfo);

				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
			}

		}

	}

}
