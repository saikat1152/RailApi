package com.jbl.t24.rest.api.controller;

import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jbl.t24.rest.api.common.model.AccountInfo;
import com.jbl.t24.rest.api.common.model.AccountInfoNotFound;
import com.jbl.t24.rest.api.common.model.FtResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.custom.exception.BlankOfsResponseException;
import com.jbl.t24.rest.api.custom.validation.CustomValidation;
import com.jbl.t24.rest.api.enums.AccountNature;
import com.jbl.t24.rest.api.enums.CBSResponseStr;
import com.jbl.t24.rest.api.enums.RemitterStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.BeftnOutwardInfo;
import com.jbl.t24.rest.api.model.RequestOfs;
import com.jbl.t24.rest.api.service.BeftnInfoService;

import com.jbl.t24.rest.api.tccUtility.TccUtility;

@RestController
@CrossOrigin
@RequestMapping("/beftn_out")
@Validated
public class BeftnOutwardTransaction {

	@Autowired
	private BeftnInfoService beftnInfoService;

	@RequestMapping(value = "/outward", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> beftnTrasferOutward(@Valid @RequestBody BeftnOutwardInfo beftnInfo,
			HttpServletRequest httpServletRequest) throws Exception {

		beftnInfo.setCurrency("BDT");
		String debitAccNo = beftnInfo.getDebitAccNo();
		// String currency = beftnInfo.getCurrency();
		String creditAccNo = beftnInfo.getCreditAccNo();
		Double debitAmount = beftnInfo.getDebitAmount();
		String creditNarrative = beftnInfo.getCreditNarrative();
		String debitNarrative = beftnInfo.getDebitNarrative();
		Integer version = beftnInfo.getVersion();
		String companyCode = beftnInfo.getCompanyCode();

		// System.out.println("debitAccNo " + debitAccNo
		// // +" debitCurrency " + currency
		// + " creditAccNo " + creditAccNo
		// + " debitAmount " + debitAmount
		// + " creditNarative " + creditNarrative
		// + " debitNarative " + debitNarrative
		// + " version " + version
		// + " companyCode " + companyCode);

		/**
		 * Mentioning the non null properties of BEFTN Outward
		 */

		Objects.requireNonNull(debitAccNo);
		Objects.requireNonNull(creditAccNo);
		Objects.requireNonNull(debitAmount);
		Objects.requireNonNull(creditNarrative);

		/**
		 * @debitNarrative is a must when version is 2
		 */
		if (beftnInfo.getVersion() == 2) {
			Objects.requireNonNull(debitNarrative);

		}

		Objects.requireNonNull(version);
		Objects.requireNonNull(companyCode);

		CustomValidation customValidation = new CustomValidation();

		// if (customValidation.isBlankString(debitAccNo)
		// || customValidation.isBlankString(creditAccNo)
		// || customValidation.isBlankString(creditNarrative)
		// || customValidation.isBlankString(companyCode)
		// || customValidation.isBlankDouble(debitAmount)
		// || customValidation.isBlankInteger(version)
		// || (beftnInfo.getVersion() == 2 &&
		// customValidation.isBlankString(debitNarrative))) {

		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ18.getText(), ResponseStatus.FOURZ18.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);

		// }

		// if (!customValidation.isRequiredLength(creditNarrative, 16)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ9.getText(), ResponseStatus.FOURZ9.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }

		// if (beftnInfo.getVersion() == 2) {
		// if (!customValidation.isRequiredLength(debitNarrative, 16)) {
		// System.out.println("into debit nar len");
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ9.getText(), ResponseStatus.FOURZ9.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }

		// }

		// Debit Account and Credit Account Length Check

		// if (!customValidation.isAccountLengthValid(debitAccNo)
		// || !customValidation.isAccountLengthValid(creditAccNo)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ12.getText(), ResponseStatus.FOURZ12.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }

		// Version Validation Checking
		// 1 for Credit, 2 for Debit, 3 for Return
		// if (!customValidation.isVersionValidOutward(version)) {
		// JwtErrorResponse jwtErrorResponse = new
		// JwtErrorResponse(HttpStatus.BAD_REQUEST,
		// ResponseStatus.FOURZ20.getText(), ResponseStatus.FOURZ20.getValue());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtErrorResponse);
		// }

		/**
		 * Setting the versionwise OFS Message @requestOFS
		 */
		String requestOFS = "";
		// FUNDS.TRANSFER,BEFTN
		if (version == 1) {
			requestOFS = "FUNDS.TRANSFER,BFOW.CR/I/PROCESS//0," + companyCode
					+ ",,DEBIT.CURRENCY=BDT,"
					+ "DEBIT.ACCT.NO=" + debitAccNo + ","
					+ "CREDIT.ACCT.NO=" + creditAccNo + ","
					+ "DEBIT.AMOUNT=" + debitAmount + ","
					+ "FT.CR.DETAILS=" + creditNarrative;
		}
		// Debit narative will be unique
		/**
		 * Semicolon should be trimmed off from debit and credit narrrative
		 */
		/**
		 * 1. requestBranch ---> new filed to be added to the
		 * 2. maker and checker filed should be added --- bankId
		 * 3. 
		 */
		else if (version == 2) {
			requestOFS = "FUNDS.TRANSFER,BFOW.DR/I/PROCESS//0," + companyCode
					+ ",,DEBIT.CURRENCY=BDT,"
					+ "DEBIT.ACCT.NO=" + debitAccNo + ","
					+ "CREDIT.ACCT.NO=" + creditAccNo + ","
					+ "DEBIT.AMOUNT=" + debitAmount + ","
					+ "FT.DR.DETAILS=" + debitNarrative + ","
					+ "FT.CR.DETAILS=" + creditNarrative;
		} else if (version == 3) {
			requestOFS = "FUNDS.TRANSFER,BFOW.RET/I/PROCESS//0," + companyCode
					+ ",,DEBIT.CURRENCY=BDT,"
					+ "DEBIT.ACCT.NO=" + debitAccNo + ","
					+ "CREDIT.ACCT.NO=" + creditAccNo + ","
					+ "DEBIT.AMOUNT=" + debitAmount + ","
					+ "FT.CR.DETAILS=" + creditNarrative;
		} else {
			System.out.println("OFS Message Error Dise. Generate hoy nai");
		}

		// 102 - FT.CR.DETAILS
		// 101 - FT.DR.DETAILS
		// + "DEBIT.VALUE.DATE=20200201";

		BeftnOutwardInfo beftnInfoSave = null;
		BeftnOutwardInfo beftnInfoExist = null;

		/**
		 * Checking this beftn inward if already exist or not in database by narrative
		 * Id, if exist and success just return error
		 */

		if (version == 1 || version == 3) {
			beftnInfoExist = beftnInfoService.findByCreditNarrative(creditNarrative);
		} else if (version == 2) {
			beftnInfoExist = beftnInfoService.findByDebitNarrative(debitNarrative);

			// System.out.println("I am chacking exisitng: "+beftnInfoExist.toString());
			// beftnInfoExist2 = beftnInfoService.findByCreditNarrative(creditNarrative);

			boolean check = beftnInfoService.existsByCreditNarrative(creditNarrative);

			if (check != false) {

				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
						ResponseStatus.TWOZ4.getText(), ResponseStatus.TWOZ4.getValue());
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
			}
		}

		if (beftnInfoExist != null) {

			int statusExist = beftnInfoExist.getStatus();

			if (statusExist == RemitterStatus.SUCCESS.getValue()) {
				FtResponse ftResponse = new FtResponse();
				ftResponse.setStatus(HttpStatus.OK);

				if (version == 1 || version == 3)
					ftResponse.setNarrative(creditNarrative);
				else if (version == 2) {
					ftResponse.setNarrative(debitNarrative);
				}

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

		}

		else {

			/**
			 * When BEFTN Not exists in database
			 * Saving BEFTN Data into the database
			 */

			String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");

			if (remoteAddr == null) {
				remoteAddr = httpServletRequest.getRemoteAddr();
			}

			String host = httpServletRequest.getRemoteHost();

			beftnInfo.setIp(remoteAddr);
			beftnInfo.setHostName(host);

			/**
			 * Initially the status of BEFTN Transaction is pending and saved in the
			 * database
			 * It is because of Transaction response yet not confirmed
			 */
			beftnInfo.setStatus(RemitterStatus.PENDING.getValue());
			beftnInfo.setOfsRequest(requestOFS);
			// remitterInfo.setRequestAt(new Timestamp(System.currentTimeMillis()));
			beftnInfoSave = beftnInfoService.save(beftnInfo);
			// System.out.println("remitterInfoSave: " + remitterInfoSave);

		}

		long start_time = System.currentTimeMillis();

		System.out.println("requestOFS: " + requestOFS);
		TccUtility tccUtility = new TccUtility();
		String responseData = "";

		/**
		 * Checking if response from cbs is blank due to timeout or cbs issue
		 */

		try {
			responseData = tccUtility.sendRequest(requestOFS);
		} catch (BlankOfsResponseException e) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ4.getText(),
					ResponseStatus.FIVEZ4.getValue());
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		}

		//
		// --------------------
		// RequestOfs requestOfs = new RequestOfs();
		// requestOfs.setReqOfs(requestOFS);
		// String responseData = beftnInfoService.getTccOfsResponse(requestOfs);
		// -------------------------------
		//

		/**
		 * Finally the BEFTN Txn response is fetched
		 */

		beftnInfoSave.setOfsResponse(responseData);
		// remitterInfoSave.setResponseAt(new Timestamp(System.currentTimeMillis()));
		System.out.println("responseData " + responseData);

		long end_time = System.currentTimeMillis();

		System.out.println("Total Execution Time: " + (end_time - start_time));

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

			if (version == 1 || version == 3)
				ftResponse.setNarrative(creditNarrative);
			else if (version == 2) {
				ftResponse.setNarrative(debitNarrative);
			}

			try {

				String[] spiltData = responseData.split(",");
				String[] firstPart = spiltData[0].split("/");
				String statusFlag = firstPart[2];
				// if(spiltData.length()>2){

				// }
				String additionalInfo_2nd_part = spiltData.length>2?spiltData[2]  : "NONE";
				String additionalInfo = spiltData[1].split("=")[1] +" : " +additionalInfo_2nd_part;

				if (statusFlag.equals("1")) {
					String ftRef = firstPart[0];
					ftResponse.setFtRef(ftRef);
					ftResponse.setMessage(ResponseStatus.TWOZ0.getText());
					ftResponse.setResponseCode(ResponseStatus.TWOZ0.getValue());

					// if (responseData.contains(CBSResponseStr.accountNaturePersonal.getText())) {
					// ftResponse.setCreditAccountCategory(AccountNature.PERSONAL.getValue());
					// ftResponse.setAdditionalInfo(AccountNature.PERSONAL.getText());
					// }
					// else {
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
						// }
						// else {
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

					} else if (responseData.contains(CBSResponseStr.unauthorizedOverdraft.getText())) {

						/**
						 * For Unauthorized Overdraft
						 */

						ftResponse.setMessage(ResponseStatus.FOURZ21.getText());
						ftResponse.setResponseCode(ResponseStatus.FOURZ21.getValue());
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());

						beftnInfoService.save(beftnInfoSave);
					} else if (responseData.contains(CBSResponseStr.postingRestriction.getText())) {

						/**
						 * For Posting Restriction
						 */
						ftResponse.setMessage(ResponseStatus.FOURZ22.getText());
						ftResponse.setResponseCode(ResponseStatus.FOURZ22.getValue());
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());

						beftnInfoService.save(beftnInfoSave);

					} else if (responseData.contains(CBSResponseStr.invalidMinus.getText())) {
						/**
						 * Is Amount Negative Checking
						 */
						ftResponse.setMessage(ResponseStatus.FOURZ23.getText());
						ftResponse.setResponseCode(ResponseStatus.FOURZ23.getValue());
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());

						beftnInfoService.save(beftnInfoSave);
					} else if (responseData.contains(CBSResponseStr.valueAmountZero.getText())) {
						/**
						 * Is Amount Zero Checking
						 */
						ftResponse.setMessage(ResponseStatus.FOURZ24.getText());
						ftResponse.setResponseCode(ResponseStatus.FOURZ24.getValue());
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());

						beftnInfoService.save(beftnInfoSave);
					} else {
						beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
						beftnInfoService.save(beftnInfoSave);

						ftResponse.setMessage(ResponseStatus.TWOZ1.getText());
						ftResponse.setResponseCode(ResponseStatus.TWOZ1.getValue());

					}

					ftResponse.setAdditionalInfo(additionalInfo);
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

}
