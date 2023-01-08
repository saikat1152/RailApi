package com.jbl.t24.rest.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.jbl.t24.rest.api.common.model.FtResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.custom.exception.BlankOfsResponseException;
import com.jbl.t24.rest.api.custom.validation.CustomValidation;
import com.jbl.t24.rest.api.enums.CBSResponseStr;
import com.jbl.t24.rest.api.enums.RemitterStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.BeftnInwardInfo;
import com.jbl.t24.rest.api.model.BeftnOutwardInfo;
import com.jbl.t24.rest.api.repository.BeftnInwardRepository;
import com.jbl.t24.rest.api.repository.BeftnOutwardRepository;
import com.jbl.t24.rest.api.tccUtility.TccUtility;
// import com.jbl.t24.rest.api.tccUtility.TccUtilityNew;

@Service
@Transactional(readOnly = true)
public class BeftnInfoService {

	@Autowired
	private RestTemplate restTemplate;

	// @Autowired
	// private BeftnInfoService beftnInfoService;

	@Autowired
	private BeftnInwardRepository beftnInwardRepository;

	@Autowired
	private BeftnOutwardRepository beftnOutwardRepository;

	@Transactional(readOnly = false)
	public BeftnInwardInfo save(BeftnInwardInfo entity) {
		return beftnInwardRepository.save(entity);
	}

	public BeftnInwardInfo findByNarrative(String narrative) {
		return beftnInwardRepository.findByNarrative(narrative);
	}

	public String getTccOfsResponse(String requestOfs) {
		String responseData = "";
		try {
			// TccUtilityNew tccUtility = new TccUtilityNew("ISOLIST");
			TccUtility tccUtility = new TccUtility("ISOLIST");
			try {
				responseData = tccUtility.sendRequest(requestOfs);
			} catch (BlankOfsResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String responseData =
		// restTemplate.postForObject("http://TCC-CLIENT/tcc/ofs/",requestOfs,
		// String.class);
		return responseData;
	}

	public String getTccOfsResponse(String requestOfs, String channelName) {
		String responseData = "";
		try {
			// TccUtilityNew tccUtility = new TccUtilityNew(channelName);
			TccUtility tccUtility = new TccUtility(channelName);
			try {
				responseData = tccUtility.sendRequest(requestOfs);
			} catch (BlankOfsResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseData;
	}

	@Transactional(readOnly = false)
	public BeftnOutwardInfo save(BeftnOutwardInfo entity) {
		return beftnOutwardRepository.save(entity);
	}

	public BeftnOutwardInfo findByCreditNarrative(String creditNarrative) {
		return beftnOutwardRepository.findByCreditNarrative(creditNarrative);
	}

	public BeftnOutwardInfo findByDebitNarrative(String debitNarrative) {
		return beftnOutwardRepository.findByDebitNarrative(debitNarrative);
	}

	public Boolean existsByCreditNarrative(String creditNarrative) {
		return beftnOutwardRepository.existsByCreditNarrative(creditNarrative);
	}

	@Async
	public CompletableFuture<List<Object>> getBulkOfsResponse(List<BeftnInwardInfo> beftnInfolist,
			HttpServletRequest httpServletRequest) throws Exception {

		// public List<Object> getBulkOfsResponse(List<BeftnInwardInfo> beftnInfolist,
		// HttpServletRequest httpServletRequest) throws Exception {
		List<Object> responseBodyList = new ArrayList<Object>();
		List<BeftnInwardInfo> evenList = new ArrayList<>();
		List<BeftnInwardInfo> oddList = new ArrayList<>();
		int i = 0;
		for (BeftnInwardInfo info : beftnInfolist) {
			if (i % 2 == 0)
				evenList.add(info);
			else
				oddList.add(info);
			i++;
		}

		CompletableFuture<List<Object>> evenResponse = processBeftnInfoList(evenList, httpServletRequest, "ISOLIST");
		CompletableFuture<List<Object>> oddResponse = processBeftnInfoList(oddList, httpServletRequest, "ISOLIST2");
		// List<Object> evenResponse = new ArrayList<Object>();
		// List<Object> oddResponse = new ArrayList<Object>();

		// evenResponse = processBeftnInfoList(evenList, httpServletRequest, "ISOLIST");
		// oddResponse = processBeftnInfoList(oddList, httpServletRequest, "ISOLIST2");

		responseBodyList.add(evenResponse.get());
		responseBodyList.add(oddResponse.get());
		// return CompletableFuture.allOf(evenResponse, oddResponse);
		// responseBodyList.addAll(evenResponse);
		// responseBodyList.addAll(oddResponse);
		// return responseBodyList;
		// CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(responseBodyList));
		return CompletableFuture.completedFuture(responseBodyList);
	}

	@Async
	private CompletableFuture<List<Object>> processBeftnInfoList(List<BeftnInwardInfo> beftnInfoList,
			HttpServletRequest httpServletRequest,
			String channelName) throws Exception {
		// private List<Object> processBeftnInfoList(List<BeftnInwardInfo>
		// beftnInfoList,
		// HttpServletRequest httpServletRequest,
		// String channelName) throws Exception {

		List<Object> responseBodyList = new ArrayList<Object>();
		for (BeftnInwardInfo beftnInfo : beftnInfoList) {
			String debitAccNo = beftnInfo.getDebitAccNo();
			// String currency = beftnInfo.getCurrency();
			String creditAccNo = beftnInfo.getCreditAccNo();
			Double debitAmount = beftnInfo.getDebitAmount();
			String narrative = beftnInfo.getNarrative();
			Integer version = beftnInfo.getVersion();

			Objects.requireNonNull(debitAccNo);
			// Objects.requireNonNull(currency);
			Objects.requireNonNull(creditAccNo);
			Objects.requireNonNull(debitAmount);
			Objects.requireNonNull(narrative);
			Objects.requireNonNull(version);

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
				requestOFS = "FUNDS.TRANSFER,BEFTN.DR/I/PROCESS//0,BD0010102"
						+ ",,DEBIT.CURRENCY=BDT,"
						+ "DEBIT.ACCT.NO=" + debitAccNo + ","
						+ "CREDIT.ACCT.NO=" + creditAccNo + ","
						+ "DEBIT.AMOUNT=" + debitAmount + ","
						+ "FT.DR.DETAILS=" + narrative;
			}

			else {
				System.out.println("OFS Message Error Dise. Generate hoy nai");
			}

			BeftnInwardInfo beftnInfoSave = null;

			// check this beftn inward if already exist or not in database by narrative
			// Id, if exist and success just return error
			BeftnInwardInfo beftnInfoExist = this.findByNarrative(narrative);

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
						// return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
						responseBodyList.add(ftResponse);
						// break;
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
						// return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
						responseBodyList.add(ftResponse);
						// break;
					} else {
						JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
								ResponseStatus.TWOZ3.getText(), ResponseStatus.TWOZ3.getValue());
						// return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
						responseBodyList.add(jwtErrorResponse);
						// break;
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
				beftnInfoSave = this.save(beftnInfo);
				// log.info("Saved Data");
				// System.out.println("remitterInfoSave: " + remitterInfoSave);

			}
			TccUtility tccUtility = new TccUtility();
			// TccUtilityNew tccUtility = new TccUtilityNew("ISOLIST2");

			// String responseData = tccUtility.sendRequest(requestOFS);
			String responseData = this.getTccOfsResponse(requestOFS);
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
				this.save(beftnInfoSave);
				// return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
				responseBodyList.add(jwtErrorResponse);
				// break;
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
						beftnInfoSave.setStatus(RemitterStatus.SUCCESS.getValue()); // Later needs to be changed at
																					// Remitter Status
						this.save(beftnInfoSave);
					} else {

						if (responseData.contains(CBSResponseStr.alreadySuccessDuplicate.getText())) {

							String duplicateSuccess[] = responseData.split(",");
							String[] secondPart = duplicateSuccess[1].split("-");
							String ftRef = secondPart[2];
							String category = secondPart[3];

							ftResponse.setFtRef(ftRef);

							ftResponse.setMessage(ResponseStatus.TWOZ2.getText());
							ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());
							beftnInfoSave.setStatus(RemitterStatus.SUCCESS.getValue());
							this.save(beftnInfoSave);
						} else if (responseData.contains(CBSResponseStr.failedDuplicate.getText())) {
							ftResponse.setMessage(ResponseStatus.TWOZ3.getText());
							ftResponse.setResponseCode(ResponseStatus.TWOZ3.getValue());
							beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
							this.save(beftnInfoSave);
						} else if (responseData.contains(CBSResponseStr.debitAccountMissing.getText())
								|| responseData.contains(CBSResponseStr.customDebitAccountMissing.getText())) {
							ftResponse.setMessage(ResponseStatus.FOURZ10.getText());
							ftResponse.setResponseCode(ResponseStatus.FOURZ10.getValue());
							beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
							this.save(beftnInfoSave);
						} else if (responseData.contains(CBSResponseStr.creditAccountMissing.getText())
								|| responseData.contains(CBSResponseStr.customCreditAccountMissing.getText())) {
							ftResponse.setMessage(ResponseStatus.FOURZ7.getText());
							ftResponse.setResponseCode(ResponseStatus.FOURZ7.getValue());
							beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
							this.save(beftnInfoSave);
						}

						else if (responseData.contains(CBSResponseStr.debitAccountLessBalance.getText())) {
							ftResponse.setMessage(ResponseStatus.FOURZ14.getText());
							ftResponse.setResponseCode(ResponseStatus.FOURZ14.getValue());
							beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
							this.save(beftnInfoSave);
						} else {
							beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
							this.save(beftnInfoSave);
							ftResponse.setMessage(ResponseStatus.TWOZ1.getText());
							ftResponse.setResponseCode(ResponseStatus.TWOZ1.getValue());
						}
						ftResponse.setAdditionalInfo(responseData);
					}
					// return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
					responseBodyList.add(ftResponse);
					// break;

				} catch (Exception e) {
					beftnInfoSave.setStatus(RemitterStatus.FAILED.getValue());
					this.save(beftnInfoSave);
					ftResponse.setMessage(ResponseStatus.TWOZ1.getText());
					ftResponse.setResponseCode(ResponseStatus.TWOZ1.getValue());
					ftResponse.setAdditionalInfo(responseData);
					// return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
					responseBodyList.add(ftResponse);
					// break;
				}
			}

		}
		// return responseBodyList;
		return CompletableFuture.completedFuture(responseBodyList);
	}

}
