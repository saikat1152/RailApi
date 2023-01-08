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
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.EftInfoOutward;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@Service
public class FtHandlerService {

    @Autowired
    ResponseMessageProcessor processor = new ResponseMessageProcessor();

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

}