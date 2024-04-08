package com.jbl.t24.rest.api.service.rtgs;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse.FtTxResponseBuilder;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@Service
public class RtgsHandlerService {

	@Autowired
	ResponseMessageProcessor processor = new ResponseMessageProcessor();

	@Autowired
	RtgsInfoOutwardService rtgsInfoOutwardService;

	// @Async
	public String handleFtTransaction(String requestOFS, String channelName) {

		// System.out.println(requestOFS);

		try {
			TccUtility tccUtility = new TccUtility(channelName);

			/**
			 * 
			 * 
			 */

			String ofsResponse = tccUtility.sendRequest(requestOFS);
			String[] spiltDataOfs = ofsResponse.split(",");

			System.out.println("--------------OFS RESPONSE" + " " + channelName + " " + "--------------");
			System.out.println(ofsResponse);
			// return
			// CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(ofsResponse));
			// return null;
			// return ResponseEntity.status(HttpStatus.OK).body(ofsResponse);
			return ofsResponse;
		} catch (Exception e) {
			// return ResponseEntity.status(HttpStatus.OK).body("eror");
			return "ofsResponse error";
		}

	}

	public ResponseEntity<?> handleRtgsTransaction(String requestOFS, RtgsInfoOutward rtgsInfoOutward,
			HttpServletRequest httpServletRequest) throws Exception {

		System.out.println(requestOFS);

		TccUtility tccUtility = new TccUtility();

		RtgsInfoOutward rtgsInfoExist = new RtgsInfoOutward();
		RtgsInfoOutward rtgsInfoSave = new RtgsInfoOutward();
		rtgsInfoExist = rtgsInfoOutwardService.findByUniqueOutwardRtgsId(rtgsInfoOutward.getUniqueOutwardRtgsId());
		int statusExist = rtgsInfoExist == null ? 0 : rtgsInfoExist.getStatus();
		/**
		 * If eft present in db and its status is success
		 */
		if (rtgsInfoExist != null) {
			// NOTE:
			/**
			 * Fetching Previous Ft Response
			 */
			if (statusExist == FtStatus.SUCCESS.getValue()) {

				String ftResponseStr = rtgsInfoExist.getFtResponseStr();
				ObjectMapper mapper = new ObjectMapper();
				FtTxResponse savedFtResponse = mapper.readValue(ftResponseStr, FtTxResponse.class);
				FtTxResponse ftResponse = savedFtResponse;

				rtgsInfoSave = rtgsInfoExist;
				return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
				// }
			} else {
				rtgsInfoSave = rtgsInfoExist;
				rtgsInfoSave.setCoCode(rtgsInfoOutward.getCoCode());
			}
		}

		// NOTE:
		/**
		 * When BEFTN Not exists in database OR it exist but it's status is pending or
		 * failure then we have try again to reach CBS transaction and then Saving BEFTN
		 * Data into the database again
		 */
		System.out.println(rtgsInfoOutward.toString());

		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
		String host = httpServletRequest.getRemoteHost();

		if (remoteAddr == null) {
			remoteAddr = httpServletRequest.getRemoteAddr();
		}

		rtgsInfoOutward.setIp(remoteAddr);
		rtgsInfoOutward.setHostname(host);

		/**
		 * Initially the status of BEFTN Transaction is pending and saved in the
		 * database It is because of Transaction response yet not confirmed
		 */
		rtgsInfoOutward.setStatus(FtStatus.PENDING.getValue());
		rtgsInfoOutward.setOfsRequest(requestOFS);

		rtgsInfoSave = rtgsInfoOutwardService.save(rtgsInfoOutward);

		System.out.println("requestOFS: " + requestOFS);
		String responseData = "";

		/**
		 * Checking if response from cbs is blank due to timeout or cbs issue
		 */

		responseData = tccUtility.sendRequest(requestOFS);

		/**
		 * Finally the BEFTN Txn response is fetched
		 */

		// eftnInfoSave.setOfsResponse(responseData);
		// remitterInfoSave.setResponseAt(new Timestamp(System.currentTimeMillis()));
		System.out.println("responseData " + responseData);

		if (responseData.equals("400") || responseData.equals("401") || responseData.equals("402")
				|| responseData.equals("403")) {
			JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FIVEZ3.getText(),
					ResponseStatus.FIVEZ3.getValue());
			rtgsInfoSave.setStatus(FtStatus.FAILED.getValue());
			rtgsInfoOutwardService.save(rtgsInfoSave);
			rtgsInfoOutwardService.save(rtgsInfoOutward);
			return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
		} else {

			// FtTxResponse ftResponse = new FtTxResponse();
			FtTxResponseBuilder ftResponse = FtTxResponse.builder();
			// NOTE: Not with Builder
			// ftResponse.setStatus(HttpStatus.OK);
			// ftResponse.setNarrative(creditNarrative);

			// NOTE: With Builder
			ftResponse.status(HttpStatus.OK).uniqueRTGS(rtgsInfoOutward.getUniqueOutwardRtgsId());

			/**
			 * NOTE: Wrapping the response by Response Handler
			 */

			ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
			wrapper = processor.handleResponseOfs(responseData, 0);

			ftResponse.ftRef(wrapper.getFtRef()).message(wrapper.getMessage()).responseCode(wrapper.getResponseCode())
					.additionalInfo(wrapper.getAdditionalInfo());
			rtgsInfoOutward.setStatus(wrapper.getFtStatus());

			ObjectMapper mapper = new ObjectMapper();
			String ftResponseStr = mapper.writeValueAsString(ftResponse.build());
			rtgsInfoOutward.setFtResponseStr(ftResponseStr);
			rtgsInfoOutward.setCbsFtno(ftResponse.build().getFtRef());
			rtgsInfoOutward.setOfsResponse(responseData);

			rtgsInfoSave = rtgsInfoOutward;
			// FIXME:
			rtgsInfoOutwardService.save(rtgsInfoSave);
			// eftInfoOutwardService.save(eftInfoOutward);

			return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

		}

	}

}