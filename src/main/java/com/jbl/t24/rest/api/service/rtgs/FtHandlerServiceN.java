package com.jbl.t24.rest.api.service.rtgs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse.FtTxResponseBuilder;
import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.constant.JwtErrorsCBS;
import com.jbl.t24.rest.api.constant.RtgsUniqueIdTransactionCheck;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.RTGSCategory;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.enums.utils.ErrorMessageGenerator;
import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;
import com.jbl.t24.rest.api.model.rtgs.CommonFtInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementNineInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementNineOutInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementOutInfo;
import com.jbl.t24.rest.api.model.rtgs.RtgsCommon;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoInward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineInward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.tccUtility.TccUtility;
import java.sql.Timestamp;
import java.util.Map;

@Service
public class FtHandlerServiceN {
	Logger logger = LogManager.getLogger(FtHandlerServiceN.class);

	@Autowired
	RtgsInfoService service;

	@Autowired
	ResponseMessageProcessor processor = new ResponseMessageProcessor();

	public static final int RTGS_OUWARD_PACS8_MIN_VALUE = 100000;

	public ResponseEntity<?> handleFtTransaction(String requestOFS, RtgsCommon ftInfo, String uniqueId)
			throws Exception {

		logger.info("Ft Handle Service Started");
		RtgsCommon ftSave;
		RtgsCommon ftExist;

		if (ftInfo instanceof RtgsInfoInward) {
			ftSave = new RtgsInfoInward();
			ftExist = new RtgsInfoInward();

		} else if (ftInfo instanceof RtgsInfoOutward) {
			ftSave = new RtgsInfoOutward();
			ftExist = new RtgsInfoOutward();
			// RtgsInfoOutward temp = (RtgsInfoOutward) ftInfo;
			ftSave = (RtgsInfoOutward) ftInfo;

			// String category = ftInfo.getTxCategory();
			// int category = ftInfo.getCategoryCode();

			// if (category.equals(RTGSCategory.RTGSPACS8OUTBDT.getValue())

			if (ftInfo.getTxCategory().equals(RTGSCategory.RTGSPACS8OUTBDT.getText())
					&& ftInfo.getDebitAmount() < RTGS_OUWARD_PACS8_MIN_VALUE) {
				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
						ResponseStatus.FOURZ26.getText(), ResponseStatus.FOURZ26.getValue());
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
			}

		} else if (ftInfo instanceof RtgsInfoPacsNineInward) {
			ftSave = new RtgsInfoPacsNineInward();
			ftExist = new RtgsInfoPacsNineInward();
		} else if (ftInfo instanceof RtgsInfoPacsNineOutward) {
			ftSave = new RtgsInfoPacsNineOutward();
			ftExist = new RtgsInfoPacsNineOutward();
		} else if (ftInfo instanceof RTGSSettlementInInfo) {
			ftSave = new RTGSSettlementInInfo();
			ftExist = new RTGSSettlementInInfo();
		} else if (ftInfo instanceof RTGSSettlementOutInfo) {
			ftSave = new RTGSSettlementOutInfo();
			ftExist = new RTGSSettlementOutInfo();
		} else if (ftInfo instanceof RTGSSettlementNineInInfo) {
			ftSave = new RTGSSettlementNineInInfo();
			ftExist = new RTGSSettlementNineInInfo();
		} else if (ftInfo instanceof RTGSSettlementNineOutInfo) {
			ftSave = new RTGSSettlementNineOutInfo();
			ftExist = new RTGSSettlementNineOutInfo();
		}

		TccUtility tccUtility = new TccUtility();

		ftSave = ftInfo;
		ftExist = service.findByUniqueId(ftInfo, uniqueId);

		int statusExist = ftExist == null ? 0 : ftExist.getStatus();

		if (ftExist != null) {

			if (statusExist == FtStatus.SUCCESS.getValue() || statusExist == FtStatus.REVERSED.getValue()) {

				String ftResponseStr = ftExist.getFtResponseStr();

				FtTxResponse ftResponse = Mapper.readValue(ftResponseStr);

				String message = ftExist.getStatus() == 2 ? ResponseStatus.TWOZ2.getText()
						: ResponseStatus.TWOZ6.getText();

				ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());
				ftResponse.setMessage(message);
				return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
			}

			else {

				if (!ftInfo.isEqual(ftExist)) {
					logger.info("Resubmitted data mismatched block entered");

					JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
							ResponseStatus.FIVEZ28.getText(), ResponseStatus.FIVEZ28.getValue());

					// ftExist.setStatus(FtStatus.FAILED.getValue());
					// ftExist.setIssueDate(ftInfo.getIssueDate());
					// ftExist.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));

					// service.save(ftExist);

					return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
				}

				logger.info("FT Alreeady Exists but Failed or Pending");

				if (statusExist == FtStatus.PENDING.getValue()) {
					logger.info("FT Alreeady Exists but Pending-Transaction Is On Already Processing");
					JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
							ResponseStatus.FIVEZ27.getText(), ResponseStatus.FIVEZ27.getValue());
					return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
				}

				logger.info("FT Alreeady Exists but Failed");
				// TODO: Pending issue in pacs9 inward
				logger.info(ftExist.getClass().getSimpleName() + " is being saved as Pending before re-CBS Checking");
				ftExist.setStatus(FtStatus.PENDING.getValue());
				service.save(ftExist);

				String uniqueIdTransactioncheck = RtgsUniqueIdTransactionCheck.checkTransactionByUniqueId(uniqueId);

				if ((uniqueIdTransactioncheck.startsWith("40") && !uniqueIdTransactioncheck.equals("407"))
						|| uniqueIdTransactioncheck.equals("499")) {

					JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(uniqueIdTransactioncheck);

					ftExist.setStatus(FtStatus.FAILED.getValue());
					ftExist.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
					ftExist.setOfsResponse(uniqueIdTransactioncheck);

					ftExist.setIssueDate(ftInfo.getIssueDate());

					service.save(ftExist);

					logger.error("Server Error:: " + jwtErrorResponse);

					return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);

				} else if (uniqueIdTransactioncheck.contains("cbsFtNumber")) {

					Map<String, String> cbsSuccessTrData = Mapper.readValueForMap(uniqueIdTransactioncheck);
					// ftSave = ftExist;

					FtTxResponseBuilder ftResponse = FtTxResponse.builder();

					ftResponse.status(HttpStatus.OK).uniqueRTGS(uniqueId).ftRef(cbsSuccessTrData.get("cbsFtNumber"))
							.message(cbsSuccessTrData.get("message"))
							.responseCode(Integer.parseInt(cbsSuccessTrData.get("responseCode")))
							.timestamp(ftInfo.getIssueDate());

					String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());
					
					ftExist.setIssueDate(ftInfo.getIssueDate());
					ftExist.setStatus(2);
					ftExist.setFtResponseStr(ftResponseStr);
					ftExist.setCbsFtno(cbsSuccessTrData.get("cbsFtNumber"));
					ftExist.setOfsResponse(cbsSuccessTrData.get("ofsResponse"));
					ftExist.setDebitAmount(Double.parseDouble(cbsSuccessTrData.get("ammount")));

					service.save(ftExist);

					logger.info("FT Transaction Data Saved");
					logger.info("Ft Handle Service Finished");

					return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

				}

				

				ftSave = ftExist;
				ftSave.setCoCode(ftInfo.getCoCode());
				ftSave.setIssueDate(ftInfo.getIssueDate());

			}
		}

		System.out.println(ftSave.toString());
		ftSave.setStatus(FtStatus.PENDING.getValue());
		ftSave.setOfsRequest(requestOFS);
		service.save(ftSave);
		logger.info("FT saved as PENDING");

		String responseData = tccUtility.sendRequest(requestOFS);
		System.out.println(responseData);

		ftSave.setIssueDate(new Timestamp(System.currentTimeMillis()));

		if (responseData.startsWith("40")) {

			JwtErrorResponse jwtErrorResponse = JwtErrorsCBS.getCbsJwtError(responseData);
			ftSave.setStatus(FtStatus.FAILED.getValue());
			ftSave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
			ftSave.setOfsResponse(responseData);
			service.save(ftSave);

			logger.error("Server Error:: " + jwtErrorResponse);

			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {
			FtTxResponseBuilder ftResponse = FtTxResponse.builder();

			ftResponse.status(HttpStatus.OK).uniqueRTGS(uniqueId);

			ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();

			try {
				wrapper = processor.handleResponseOfs(responseData, 0);
				ftResponse.ftRef(wrapper.getFtRef()).message(wrapper.getMessage())
						.responseCode(wrapper.getResponseCode()).additionalInfo(wrapper.getAdditionalInfo())
						// .timestamp(ftSave.getIssueDate());
						.commission(wrapper.getCommission())
						.vat(wrapper.getVat())
						.timestamp(wrapper.getCbsHittingTime());
			} catch (Exception e1) {

				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK,
						ErrorMessageGenerator.getErrorMessage(e1), ResponseStatus.FIVEZ0.getValue());
				ftSave.setStatus(FtStatus.FAILED.getValue());
				ftSave.setFtResponseStr(Mapper.mapToJsonString(jwtErrorResponse));
				ftSave.setOfsResponse(responseData);

				logger.error("Server error----: " + jwtErrorResponse);
				service.save(ftSave);
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
			}

			String ftResponseStr = Mapper.mapToJsonString(ftResponse.build());

			ftSave.setStatus(wrapper.getFtStatus());
			ftSave.setFtResponseStr(ftResponseStr);
			ftSave.setCbsFtno(ftResponse.build().getFtRef());
			ftSave.setOfsResponse(responseData);

			if (ftInfo instanceof RtgsInfoOutward) {
				((RtgsInfoOutward) ftSave).setCommission(wrapper.getCommission());
				((RtgsInfoOutward) ftSave).setVat(wrapper.getVat());

			} else if (ftInfo instanceof RtgsInfoPacsNineOutward) {
				((RtgsInfoPacsNineOutward) ftSave).setCommission(wrapper.getCommission());
				((RtgsInfoPacsNineOutward) ftSave).setVat(wrapper.getVat());
			}

			try {

				service.save(ftSave);
			} catch (Exception e) {
				logger.info("-----::ERROR CHECKING----::" + ftSave);
				logger.error(e.getMessage(), e);
				logger.info("JPA Error Occured During Save: " + e.getMessage());
			}

			logger.info("FT Data Saved");
			logger.info("Ft Handle Service Finished");

			return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

		}

	}

}