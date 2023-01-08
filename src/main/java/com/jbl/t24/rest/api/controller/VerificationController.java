package com.jbl.t24.rest.api.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
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

import com.jbl.t24.rest.api.common.model.AccountInfo;
import com.jbl.t24.rest.api.common.model.AccountInfoNotFound;
import com.jbl.t24.rest.api.common.model.AccountQueryInfo;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.SignatureQueryInfo;
import com.jbl.t24.rest.api.custom.validation.CustomValidation;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.service.AccountQueryInfoService;
import com.jbl.t24.rest.api.service.SignQueryInfoService;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("/verify")
@Slf4j
@Validated
public class VerificationController {

	@Autowired
	AccountQueryInfoService accountQueryInfoService;

	@Autowired
	SignQueryInfoService signQueryInfoService;

	@Value("${test.image.path}")
	private String ROOT_PATH;

	@RequestMapping(value = "/sign-check", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signatureImage(@Valid @RequestBody SignatureQueryInfo signatureQueryInfo,
			HttpServletRequest httpServletRequest) throws Exception {

		// String accountNo = requestParams.get("accountNo");
		String accountNo = signatureQueryInfo.getAccountNumber();
		Objects.requireNonNull(accountNo);

		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}
		String host = httpServletRequest.getRemoteHost();
		signatureQueryInfo.setIp(remoteAddr);
		signatureQueryInfo.setHostname(host);

		// log.info("account number provided " + accountNo);
		// CustomValidation customValidation = new CustomValidation();

		// if (customValidation.isBlankString(accountNo)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// "Account Number Cannot be Blank", ResponseStatus.FOURZ8.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }

		// if (!customValidation.isAccountLengthValid(accountNo)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ12.getText(), ResponseStatus.FOURZ12.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }

		TccUtility tccUtility = new TccUtility();

		String requestOFS = "ENQUIRY.SELECT,,,IMAGE.VIEW.SIGN,IMAGE.REFERENCE:EQ=" + accountNo;
		String responseData = tccUtility.sendRequest(requestOFS);

		System.out.println(responseData);

		String[] allData = responseData.split(",");

		String imageRelativePath = allData[2].replace("\"", "");

		if (!imageRelativePath.toLowerCase().contains("no images to display")) {
			imageRelativePath = imageRelativePath.substring(1, imageRelativePath.length());
			String imagePath = ROOT_PATH + imageRelativePath;

			System.out.println(allData);

			/**
			 * Saving image into file
			 */
			// try {
			// 	saveImage(imagePath);
			// } catch (Exception e) {
			// 	System.out.println("Image Not Found");
			// }

			/**
			 * Sending image as API return
			 */
			try {
				URL imageURL = new URL(imagePath);
				BufferedImage signImage = ImageIO.read(imageURL);
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				ImageIO.write(signImage, "jpg", bao);

				// String convertedImage =
				// Base64.getEncoder().withoutPadding().encodeToString(bao.toByteArray());

				byte[] contents = bao.toByteArray();
				HttpHeaders headers = new HttpHeaders();
				// headers.setContentType(MediaType.IMAGE_JPEG);
				headers.setCacheControl(CacheControl.noCache().getHeaderValue());

				byte[] encodedImage = Base64Utils.encode(contents);

				Map<String, byte[]> jsonMap = new HashMap<>();
				jsonMap.put("image", encodedImage);

				// ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers,
				// HttpStatus.OK);
				// return response;
				/**
				 * Saving query info into database
				 */
				signQueryInfoService.save(signatureQueryInfo);
				return ResponseEntity.status(HttpStatus.OK).headers(headers).body(jsonMap);

			} catch (IOException e) {
				log.error(e.toString());
				throw new RuntimeException(e);
			}
		} else {
			AccountInfoNotFound accountInfoNotFound = new AccountInfoNotFound(ResponseStatus.FOURZ25.getText(),
					ResponseStatus.FOURZ25.getValue(), false);
			return ResponseEntity.status(HttpStatus.OK).body(accountInfoNotFound);
		}

	}

	/**
	 * Method for saving image to local
	 */

	// public static void saveImage(String imageUrl) throws IOException {
	// 	URL url = new URL(imageUrl);
	// 	InputStream in = url.openStream();
	// 	ByteArrayOutputStream out = new ByteArrayOutputStream();
	// 	byte[] buf = new byte[1024];
	// 	int n = 0;
	// 	while (-1 != (n = in.read(buf))) {
	// 		out.write(buf, 0, n);
	// 	}
	// 	out.close();
	// 	in.close();
	// 	byte[] response = out.toByteArray();

	// 	FileOutputStream fos = new FileOutputStream("D://borrowed_image.jpg");
	// 	fos.write(response);
	// 	fos.close();
	// }

	@RequestMapping(value = "/acct-check", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> accountInfoNew(@Valid @RequestBody AccountQueryInfo accountQueryInfo,
			HttpServletRequest httpServletRequest) throws Exception {

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

		// log.info("account number provided " + accountNo);
		// CustomValidation customValidation = new CustomValidation();

		// if (customValidation.isBlankString(accountNo)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ8.getText(), ResponseStatus.FOURZ8.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }

		// if (!customValidation.isAccountLengthValid(accountNo)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ12.getText(), ResponseStatus.FOURZ12.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }

		TccUtility tccUtility = new TccUtility();

		String requestOFS = "ENQUIRY.SELECT,,,E.JBL.API.BBR,ACCOUNT.NUMBER:EQ=" + accountNo;

		String responseData = tccUtility.sendRequest(requestOFS);

		accountQueryInfo.setOfsResponse(responseData);

		if (responseData.equals("400") || responseData.equals("401") || responseData.equals("402")
				|| responseData.equals("403")) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ3.getText(),
					ResponseStatus.FIVEZ3.getValue());

			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {
			try {
				String[] allData = responseData.split(",");
				String main_data = allData[2].replace("\"", "");
				String[] mainList = main_data.split("\\*");

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
						accountInfo.setCusMobNum(secondPart[6]);
						accountInfo.setCusNidNum(secondPart[7]);
						accountInfo.setCoCode(secondPart[8].replace("BD001", ""));
						accountInfo.setCoName(secondPart[9].replace("\"", ""));
						accountInfo.setAccBalance(secondPart[10]);

						try {
							accountInfo.setPostingRestrictionCode(Integer.parseInt(secondPart[11]));
						} catch (Exception e) {
							accountInfo.setPostingRestrictionCode(0);
						}

						accountInfo.setPostingResTrictionType(secondPart[12].replace("\"", ""));

						accountInfo.setPostingRestrictionDescription(secondPart[13].replace("\"", ""));
						accountInfo.setValid(true);
						accountInfo.setMessage(ResponseStatus.TWOZ0.getText());
						accountInfo.setResponseCode(ResponseStatus.TWOZ0.getValue());
						
						/**
						 * Saving the information into the database
						 */
						accountQueryInfoService.save(accountQueryInfo);
						System.out.println(accountQueryInfo);

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

}
