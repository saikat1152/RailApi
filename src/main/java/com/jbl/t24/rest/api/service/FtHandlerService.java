package com.jbl.t24.rest.api.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jbl.t24.rest.api.common.model.FtResponse;
import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.common.model.FtTxResponse.FtTxResponseBuilder;
import com.jbl.t24.rest.api.custom.exception.BlankOfsResponseException;
import com.jbl.t24.rest.api.enums.CBSResponseStr;
import com.jbl.t24.rest.api.enums.EftStatus;
import com.jbl.t24.rest.api.enums.RTGSCategory;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.EftInfoOutward;
import com.jbl.t24.rest.api.model.RtgsInfoInward;
import com.jbl.t24.rest.api.model.RtgsInfoOutward;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@Service
public class FtHandlerService {

    @Autowired
    ResponseMessageProcessor processor = new ResponseMessageProcessor();

    @Autowired
	RtgsInfoOutwardService rtgsInfoOutwardService;

    public static final int RTGS_OUWARD_PACS8_MIN_VALUE=100000;

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

    public ResponseEntity<?> handleFtTransaction(String requestOFS, EftInfoOutward eftInfoOutward,
            HttpServletRequest httpServletRequest) throws Exception {

        System.out.println(requestOFS);

        TccUtility tccUtility = new TccUtility();

        EftInfoOutward eftnInfoExist = null;
        EftInfoOutward eftnInfoSave = null;
        // eftnInfoExist = beftnInfoService.findByCreditNarrative(creditNarrative);

        if (eftnInfoExist != null) {

            int statusExist = eftnInfoExist.getStatus();

            // NOTE: Testing the builder pattern
            if (statusExist == EftStatus.SUCCESS.getValue()) {

                // NOTE:
                /**
                 * Fetching Previous Ft Response
                 */
                String ftResponseStr = eftnInfoExist.getFtResponseStr();
                ObjectMapper mapper = new ObjectMapper();
                FtTxResponse savedFtResponse = mapper.readValue(ftResponseStr, FtTxResponse.class);
                FtTxResponse ftResponse = savedFtResponse;

                eftnInfoSave = eftnInfoExist;

                return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
            }

            else {

            }
        }

        // NOTE:
        /**
         * When BEFTN Not exists in database
         * Saving BEFTN Data into the database
         */

        String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
        String host = httpServletRequest.getRemoteHost();

        if (remoteAddr == null) {
            remoteAddr = httpServletRequest.getRemoteAddr();
        }

        eftInfoOutward.setIp(remoteAddr);
        eftInfoOutward.setHostname(host);

        /**
         * Initially the status of BEFTN Transaction is pending and saved in the
         * database
         * It is because of Transaction response yet not confirmed
         */
        eftInfoOutward.setStatus(EftStatus.PENDING.getValue());
        eftInfoOutward.setOfsRequest(requestOFS);

        // FIXME: Update it for saving into db
        // eftnInfoSave = eftInfoService.save(beftnInfo);

        System.out.println("requestOFS: " + requestOFS);
        // TccUtility tccUtility = new TccUtility();
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
            eftnInfoSave.setStatus(EftStatus.FAILED.getValue());
            // FIXME:
            // eftInfoService.save(eftnInfoSave);
            return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
        } else {

            // FtTxResponse ftResponse = new FtTxResponse();
            FtTxResponseBuilder ftResponse = FtTxResponse.builder();
            // NOTE: Not with Builder
            // ftResponse.setStatus(HttpStatus.OK);
            // ftResponse.setNarrative(creditNarrative);

            // NOTE: With Builder
            ftResponse.status(HttpStatus.OK)
                    .uniqueEft(eftInfoOutward.getUniqueOutwardEftId());

            /**
             * NOTE:
             * Wrapping the response by Response Handler
             */

            ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
            wrapper = processor.handleResponseOfs(responseData);

            ftResponse.ftRef(wrapper.getFtRef())
                    .message(wrapper.getMessage())
                    .responseCode(wrapper.getResponseCode())
                    .additionalInfo(wrapper.getAdditionalInfo());
            eftInfoOutward.setStatus(wrapper.getFtStatus());
            eftnInfoSave = eftInfoOutward;
            // FIXME:
            // beftnInfoService.save(eftnInfoSave);

            return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

        }

    }

    public ResponseEntity<?> handleRtgsOutwardTransaction(String requestOFS, RtgsInfoOutward rtgsInfoOutward,
            HttpServletRequest httpServletRequest) throws Exception {

    	 System.out.println(requestOFS);

         TccUtility tccUtility = new TccUtility();

         RtgsInfoOutward rtgsInfoExist = new RtgsInfoOutward();
//         RtgsInfoOutward rtgsInfoSave = new RtgsInfoOutward();
         RtgsInfoOutward rtgsInfoSave = rtgsInfoOutward;
         rtgsInfoExist = rtgsInfoOutwardService.findByUniqueOutwardRtgsId(rtgsInfoOutward.getUniqueOutwardRtgsId());
         int statusExist = rtgsInfoExist == null ? 0 : rtgsInfoExist.getStatus();
         /**
          * If eft present in db and its status is success
          */
         if (rtgsInfoExist != null ) {
				// NOTE:
				/**
				 * Fetching Previous Ft Response from database record
				 */
				if (statusExist == EftStatus.SUCCESS.getValue()) {

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
          * When BEFTN Not exists in database OR it exist
          * but it's status is pending or failure then
          * we have try again to reach CBS transaction and then
          * Saving BEFTN Data into the database again
          */
//         System.out.println(rtgsInfoOutward.toString());
         System.out.println(rtgsInfoSave.toString());

         String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
         String host = httpServletRequest.getRemoteHost();

         if (remoteAddr == null) {
             remoteAddr = httpServletRequest.getRemoteAddr();
         }

			/*
			 * checking the categorty RTGS Ouward=1
			 * Customs E payment=3,
			 * RTGS Inward=2
			 */

			int category = rtgsInfoOutward == null ? 0 : rtgsInfoOutward.getCategory();
			if (category == RTGSCategory.RTGSOUTWARDPACS8.getValue() && rtgsInfoOutward.getDebitAmount() < RTGS_OUWARD_PACS8_MIN_VALUE) {
				JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(HttpStatus.OK, ResponseStatus.FOURZ26.getText(),
						ResponseStatus.FOURZ26.getValue());
				return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
			} else {
				rtgsInfoSave.setCategory(rtgsInfoOutward.getCategory());
			}

			/*
			 * rtgsInfoOutward.setIp(remoteAddr); rtgsInfoOutward.setHostname(host);
			 */

         rtgsInfoSave.setIp(remoteAddr);
         rtgsInfoSave.setHostname(host);

         /**
          * Initially the status of BEFTN Transaction is pending and saved in the
          * database
          * It is because of Transaction response yet not confirmed
          */

			/*
			 * rtgsInfoOutward.setStatus(EftStatus.PENDING.getValue());
			 * rtgsInfoOutward.setOfsRequest(requestOFS);
			 */

         rtgsInfoSave.setStatus(EftStatus.PENDING.getValue());
         rtgsInfoSave.setOfsRequest(requestOFS);

         //rtgsInfoSave = 
        		 rtgsInfoOutwardService.save(rtgsInfoSave);

         System.out.println("requestOFS: " + requestOFS);
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
             rtgsInfoSave.setStatus(EftStatus.FAILED.getValue());
             rtgsInfoOutwardService.save(rtgsInfoSave);
             //rtgsInfoOutwardService.save(rtgsInfoOutward);
             return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
         } else {

             // FtTxResponse ftResponse = new FtTxResponse();
             FtTxResponseBuilder ftResponse = FtTxResponse.builder();
             // NOTE: Not with Builder
             // ftResponse.setStatus(HttpStatus.OK);
             // ftResponse.setNarrative(creditNarrative);

             // NOTE: With Builder
             ftResponse.status(HttpStatus.OK)
                     .uniqueEft(rtgsInfoOutward.getUniqueOutwardRtgsId());

             /**
              * NOTE:
              * Wrapping the response by Response Handler
              */

             ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
             wrapper = processor.handleResponseOfs(responseData);

             ftResponse.ftRef(wrapper.getFtRef())
                     .message(wrapper.getMessage())
                     .responseCode(wrapper.getResponseCode())
                     .additionalInfo(wrapper.getAdditionalInfo());
//             rtgsInfoOutward.setStatus(wrapper.getFtStatus());
             rtgsInfoSave.setStatus(wrapper.getFtStatus());

             ObjectMapper mapper = new ObjectMapper();
             String ftResponseStr = mapper.writeValueAsString(ftResponse.build());
				/*
				 * rtgsInfoOutward.setFtResponseStr(ftResponseStr);
				 * rtgsInfoOutward.setCbsFtno(ftResponse.build().getFtRef());
				 * rtgsInfoOutward.setOfsResponse(responseData);
				 */

             rtgsInfoSave.setFtResponseStr(ftResponseStr);
             rtgsInfoSave.setCbsFtno(ftResponse.build().getFtRef());
             rtgsInfoSave.setOfsResponse(responseData);
             
//             rtgsInfoSave = rtgsInfoOutward;
             // FIXME:
             rtgsInfoOutwardService.save(rtgsInfoSave);
             // eftInfoOutwardService.save(eftInfoOutward);

             return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

         }

    }

    public ResponseEntity<?> handleRtgsInwardTransaction(String requestOFS, RtgsInfoInward rtgsInfoInward,
            HttpServletRequest httpServletRequest) throws Exception {

    	 System.out.println(requestOFS);

         TccUtility tccUtility = new TccUtility();

         RtgsInfoInward rtgsInfoExist = new RtgsInfoInward();
         RtgsInfoInward rtgsInfoSave = new RtgsInfoInward();
         ///In ward saikat
         //rtgsInfoExist = rtgsInfoOutwardService.findByUniqueOutwardRtgsId(rtgsInfoOutward.getUniqueOutwardRtgsId());
         int statusExist = rtgsInfoExist == null ? 0 : rtgsInfoExist.getStatus();
         /**
          * If eft present in db and its status is success
          */
         if (rtgsInfoExist != null ) {
				// NOTE:
				/**
				 * Fetching Previous Ft Response from database record
				 */
				if (statusExist == EftStatus.SUCCESS.getValue()) {

					String ftResponseStr = rtgsInfoExist.getFtResponseStr();
					ObjectMapper mapper = new ObjectMapper();
					FtTxResponse savedFtResponse = mapper.readValue(ftResponseStr, FtTxResponse.class);
					FtTxResponse ftResponse = savedFtResponse;

					rtgsInfoSave = rtgsInfoExist;
					return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
					// }
				} else {
					rtgsInfoSave = rtgsInfoExist;
					rtgsInfoSave.setCoCode(rtgsInfoInward.getCoCode());
				}
         }

         // NOTE:
         /**
          * When BEFTN Not exists in database OR it exist
          * but it's status is pending or failure then
          * we have try again to reach CBS transaction and then
          * Saving BEFTN Data into the database again
          */
         System.out.println(rtgsInfoInward.toString());

         String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
         String host = httpServletRequest.getRemoteHost();

         if (remoteAddr == null) {
             remoteAddr = httpServletRequest.getRemoteAddr();
         }

         rtgsInfoInward.setIp(remoteAddr);
         rtgsInfoInward.setHostname(host);

         /**
          * Initially the status of BEFTN Transaction is pending and saved in the
          * database
          * It is because of Transaction response yet not confirmed
          */
         rtgsInfoInward.setStatus(EftStatus.PENDING.getValue());
         rtgsInfoInward.setOfsRequest(requestOFS);

         ///In ward saikat
        // rtgsInfoSave = rtgsInfoOutwardService.save(rtgsInfoOutward);

         System.out.println("requestOFS: " + requestOFS);
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
             rtgsInfoSave.setStatus(EftStatus.FAILED.getValue());

             ///In ward saikat
             //  rtgsInfoOutwardService.save(rtgsInfoSave);
             // rtgsInfoOutwardService.save(rtgsInfoOutward);
             return ResponseEntity.status(HttpStatus.OK).body(jwtErrorResponse);
         } else {

             // FtTxResponse ftResponse = new FtTxResponse();
             FtTxResponseBuilder ftResponse = FtTxResponse.builder();
             // NOTE: Not with Builder
             // ftResponse.setStatus(HttpStatus.OK);
             // ftResponse.setNarrative(creditNarrative);

             // NOTE: With Builder
             ftResponse.status(HttpStatus.OK)
                     .uniqueEft(rtgsInfoInward.getUniqueInwardRtgsId());

             /**
              * NOTE:
              * Wrapping the response by Response Handler
              */

             ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
             wrapper = processor.handleResponseOfs(responseData);

             ftResponse.ftRef(wrapper.getFtRef())
                     .message(wrapper.getMessage())
                     .responseCode(wrapper.getResponseCode())
                     .additionalInfo(wrapper.getAdditionalInfo());
             rtgsInfoInward.setStatus(wrapper.getFtStatus());

             ObjectMapper mapper = new ObjectMapper();
             String ftResponseStr = mapper.writeValueAsString(ftResponse.build());
             rtgsInfoInward.setFtResponseStr(ftResponseStr);
             rtgsInfoInward.setCbsFtno(ftResponse.build().getFtRef());
             rtgsInfoInward.setOfsResponse(responseData);

             ///In ward saikat
            // rtgsInfoSave = rtgsInfoOutward;
             // FIXME:
             //rtgsInfoOutwardService.save(rtgsInfoSave);
             // eftInfoOutwardService.save(eftInfoOutward);

             return ResponseEntity.status(HttpStatus.OK).body(ftResponse.build());

         }

    }

}