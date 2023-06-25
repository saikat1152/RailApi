package com.jbl.t24.rest.api.service;

import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.common.model.ResponseMsgProcessorWrapper;
import com.jbl.t24.rest.api.enums.CBSResponseStr;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;

@Service
public class ResponseMessageProcessor {

	/*
	 * ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();
	 * 
	 * public ResponseMsgProcessorWrapper handleResponseOfs(String responseData) {
	 * 
	 * 
	 * if (responseData.contains(CBSResponseStr.invalidCompany.getText())) {
	 * wrapper.setMessage(ResponseStatus.FOURZ27.getText());
	 * wrapper.setResponseCode(ResponseStatus.FOURZ27.getValue());
	 * wrapper.setFtStatus(EftStatus.FAILED.getValue()); return wrapper; }
	 * 
	 * 
	 * String[] spiltData = responseData.split(","); String[] firstPart =
	 * spiltData[0].split("/"); String statusFlag = firstPart[2]; //
	 * if(spiltData.length()>2){
	 * 
	 * // } String additionalInfo_2nd_part = spiltData.length > 2 ? spiltData[2] :
	 * "NONE"; String additionalInfo = spiltData[1].split("=")[1] + " : " +
	 * additionalInfo_2nd_part;
	 * 
	 * if (statusFlag.equals("1")) { String ftRef = firstPart[0];
	 * 
	 * wrapper.setFtRef(ftRef); wrapper.setMessage(ResponseStatus.TWOZ0.getText());
	 * wrapper.setResponseCode(ResponseStatus.TWOZ0.getValue());
	 * wrapper.setFtStatus(FtStatus.SUCCESS.getValue());
	 * 
	 * } else {
	 * 
	 * if (responseData.contains(CBSResponseStr.alreadySuccessDuplicate.getText()))
	 * {
	 * 
	 * String duplicateSuccess[] = responseData.split(","); String[] secondPart =
	 * duplicateSuccess[1].split("-"); String ftRef = secondPart[2]; // String
	 * category = secondPart[3];
	 * 
	 * // NOTE: Not with Builder // ftResponse.setFtRef(ftRef); //
	 * ftResponse.setMessage(ResponseStatus.TWOZ2.getText()); //
	 * ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());
	 * 
	 * wrapper.setFtRef(ftRef); wrapper.setMessage(ResponseStatus.TWOZ0.getText());
	 * wrapper.setResponseCode(ResponseStatus.TWOZ0.getValue());
	 * wrapper.setFtStatus(FtStatus.SUCCESS.getValue());
	 * 
	 * } else if (responseData.contains(CBSResponseStr.failedDuplicate.getText())) {
	 * 
	 * wrapper.setMessage(ResponseStatus.TWOZ3.getText());
	 * wrapper.setResponseCode(ResponseStatus.TWOZ3.getValue());
	 * wrapper.setFtStatus(FtStatus.FAILED.getValue());
	 * 
	 * // NOTE: Not with Builder //
	 * ftResponse.setMessage(ResponseStatus.TWOZ3.getText()); //
	 * ftResponse.setResponseCode(ResponseStatus.TWOZ3.getValue());
	 * 
	 * } else if
	 * (responseData.contains(CBSResponseStr.debitAccountMissing.getText()) ||
	 * responseData.contains(CBSResponseStr.customDebitAccountMissing.getText())) {
	 * 
	 * wrapper.setMessage(ResponseStatus.FOURZ10.getText());
	 * wrapper.setResponseCode(ResponseStatus.FOURZ10.getValue());
	 * wrapper.setFtStatus(FtStatus.FAILED.getValue());
	 * 
	 * // NOTE: Not With Builder //
	 * ftResponse.setMessage(ResponseStatus.FOURZ10.getText()); //
	 * ftResponse.setResponseCode(ResponseStatus.FOURZ10.getValue());
	 * 
	 * } else if
	 * (responseData.contains(CBSResponseStr.creditAccountMissing.getText()) ||
	 * responseData.contains(CBSResponseStr.customCreditAccountMissing.getText())) {
	 * 
	 * wrapper.setMessage(ResponseStatus.FOURZ7.getText());
	 * wrapper.setResponseCode(ResponseStatus.FOURZ7.getValue());
	 * wrapper.setFtStatus(FtStatus.FAILED.getValue()); // NOTE:Not With Builder //
	 * ftResponse.setMessage(ResponseStatus.FOURZ7.getText()); //
	 * ftResponse.setResponseCode(ResponseStatus.FOURZ7.getValue());
	 * 
	 * } else if
	 * (responseData.contains(CBSResponseStr.debitAccountLessBalance.getText())) {
	 * 
	 * wrapper.setMessage(ResponseStatus.FOURZ14.getText());
	 * wrapper.setResponseCode(ResponseStatus.FOURZ14.getValue());
	 * wrapper.setFtStatus(FtStatus.FAILED.getValue()); // NOTE: Not With Builder //
	 * ftResponse.setMessage(ResponseStatus.FOURZ14.getText()); //
	 * ftResponse.setResponseCode(ResponseStatus.FOURZ14.getValue());
	 * 
	 * } else if
	 * (responseData.contains(CBSResponseStr.unauthorizedOverdraft.getText())) {
	 * 
	 *//**
		 * For Unauthorized Overdraft
		 */
	/*
	 * wrapper.setMessage(ResponseStatus.FOURZ21.getText());
	 * wrapper.setResponseCode(ResponseStatus.FOURZ21.getValue());
	 * wrapper.setFtStatus(FtStatus.FAILED.getValue());
	 * 
	 * // NOTE: Not With Builder //
	 * ftResponse.setMessage(ResponseStatus.FOURZ21.getText()); //
	 * ftResponse.setResponseCode(ResponseStatus.FOURZ21.getValue());
	 * 
	 * } else if
	 * (responseData.contains(CBSResponseStr.postingRestriction.getText())) {
	 * 
	 *//**
		 * For Posting Restriction
		 */
	/*
	 * 
	 * wrapper.setMessage(ResponseStatus.FOURZ22.getText());
	 * wrapper.setResponseCode(ResponseStatus.FOURZ22.getValue());
	 * wrapper.setFtStatus(FtStatus.FAILED.getValue());
	 * 
	 * // NOTE: Not With Builder //
	 * ftResponse.setMessage(ResponseStatus.FOURZ22.getText()); //
	 * ftResponse.setResponseCode(ResponseStatus.FOURZ22.getValue());
	 * 
	 * } else if (responseData.contains(CBSResponseStr.invalidMinus.getText())) {
	 *//**
		 * Is Amount Negative Checking
		 */
	/*
	 * 
	 * wrapper.setMessage(ResponseStatus.FOURZ23.getText());
	 * wrapper.setResponseCode(ResponseStatus.FOURZ23.getValue());
	 * wrapper.setFtStatus(FtStatus.FAILED.getValue());
	 * 
	 * // NOTE: Not With Builder //
	 * ftResponse.setMessage(ResponseStatus.FOURZ23.getText()); //
	 * ftResponse.setResponseCode(ResponseStatus.FOURZ23.getValue());
	 * 
	 * } else if (responseData.contains(CBSResponseStr.valueAmountZero.getText())) {
	 *//**
		 * Is Amount Zero Checking
		 *//*
			 * 
			 * wrapper.setMessage(ResponseStatus.FOURZ24.getText());
			 * wrapper.setResponseCode(ResponseStatus.FOURZ24.getValue());
			 * wrapper.setFtStatus(FtStatus.FAILED.getValue()); // NOTE: Not With Builder //
			 * ftResponse.setMessage(ResponseStatus.FOURZ24.getText()); //
			 * ftResponse.setResponseCode(ResponseStatus.FOURZ24.getValue());
			 * 
			 * } else { wrapper.setMessage(ResponseStatus.TWOZ1.getText());
			 * wrapper.setResponseCode(ResponseStatus.TWOZ1.getValue());
			 * wrapper.setFtStatus(FtStatus.FAILED.getValue()); // NOTE: Not With Builder //
			 * ftResponse.setMessage(ResponseStatus.TWOZ1.getText()); //
			 * ftResponse.setResponseCode(ResponseStatus.TWOZ1.getValue()); }
			 * 
			 * // NOTE: Not With Builder // ftResponse.setAdditionalInfo(additionalInfo);
			 * 
			 * // NOTE: With Builder // ftResponse.addAdditionalInfo(additionalInfo) }
			 * wrapper.setAdditionalInfo(additionalInfo); // return
			 * ResponseEntity.status(HttpStatus.OK).body(ftResponse); return wrapper;
			 * 
			 * }
			 */
	
	ResponseMsgProcessorWrapper wrapper = new ResponseMsgProcessorWrapper();

    public ResponseMsgProcessorWrapper handleResponseOfs(String responseData, int reverseFlag) {

        String[] spiltData = responseData.split(",");
        String[] firstPart = spiltData[0].split("/");
        String statusFlag = firstPart[2];
        // if(spiltData.length()>2){

        // }
        String additionalInfo_2nd_part = spiltData.length > 2 ? spiltData[2] : "NONE";
        String additionalInfo = spiltData[1].split("=")[1] + " : " + additionalInfo_2nd_part;

        switch (statusFlag) {
            case "1":
                String ftRef = firstPart[0];
                String uniqueOperationTransactionId = spiltData[20].split("=")[1];

                wrapper.setFtRef(ftRef);
                wrapper.setMessage(reverseFlag == 0 ? ResponseStatus.TWOZ0.getText() : ResponseStatus.TWOZ5.getText());
                wrapper.setResponseCode(
                        reverseFlag == 0 ? ResponseStatus.TWOZ0.getValue() : ResponseStatus.TWOZ5.getValue());
                wrapper.setFtStatus(reverseFlag == 0 ? FtStatus.SUCCESS.getValue() : FtStatus.REVERSED.getValue());
                wrapper.setUniqueOperationTransactionId(uniqueOperationTransactionId);
                break;

            default:
                if (responseData.contains(CBSResponseStr.alreadySuccessDuplicate.getText())) {

                    String duplicateSuccess[] = responseData.split(",");
                    String[] secondPart = duplicateSuccess[1].split("-");
                    ftRef = secondPart[2];
                    // String category = secondPart[3];

                    // NOTE: Not with Builder
                    // ftResponse.setFtRef(ftRef);
                    // ftResponse.setMessage(ResponseStatus.TWOZ2.getText());
                    // ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());

                    wrapper.setFtRef(ftRef);
                    wrapper.setMessage(ResponseStatus.TWOZ2.getText());
                    wrapper.setResponseCode(ResponseStatus.TWOZ2.getValue());
                    wrapper.setFtStatus(FtStatus.SUCCESS.getValue());

                } else if (responseData.contains(CBSResponseStr.failedDuplicate.getText())) {

                    wrapper.setMessage(ResponseStatus.TWOZ3.getText());
                    wrapper.setResponseCode(ResponseStatus.TWOZ3.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());

                    // NOTE: Not with Builder
                    // ftResponse.setMessage(ResponseStatus.TWOZ3.getText());
                    // ftResponse.setResponseCode(ResponseStatus.TWOZ3.getValue());

                } else if (responseData.contains(CBSResponseStr.debitAccountMissing.getText())
                        || responseData.contains(CBSResponseStr.customDebitAccountMissing.getText())) {

                    wrapper.setMessage(ResponseStatus.FOURZ10.getText());
                    wrapper.setResponseCode(ResponseStatus.FOURZ10.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());

                    // NOTE: Not With Builder
                    // ftResponse.setMessage(ResponseStatus.FOURZ10.getText());
                    // ftResponse.setResponseCode(ResponseStatus.FOURZ10.getValue());

                } else if (responseData.contains(CBSResponseStr.creditAccountMissing.getText())
                        || responseData.contains(CBSResponseStr.customCreditAccountMissing.getText())) {

                    wrapper.setMessage(ResponseStatus.FOURZ7.getText());
                    wrapper.setResponseCode(ResponseStatus.FOURZ7.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());
                    // NOTE:Not With Builder
                    // ftResponse.setMessage(ResponseStatus.FOURZ7.getText());
                    // ftResponse.setResponseCode(ResponseStatus.FOURZ7.getValue());

                } else if (responseData.contains(CBSResponseStr.debitAccountLessBalance.getText())) {

                    wrapper.setMessage(ResponseStatus.FOURZ14.getText());
                    wrapper.setResponseCode(ResponseStatus.FOURZ14.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());
                    // NOTE: Not With Builder
                    // ftResponse.setMessage(ResponseStatus.FOURZ14.getText());
                    // ftResponse.setResponseCode(ResponseStatus.FOURZ14.getValue());

                } else if (responseData.contains(CBSResponseStr.unauthorizedOverdraft.getText())) {

                    /**
                     * For Unauthorized Overdraft
                     */
                    wrapper.setMessage(ResponseStatus.FOURZ21.getText());
                    wrapper.setResponseCode(ResponseStatus.FOURZ21.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());

                    // NOTE: Not With Builder
                    // ftResponse.setMessage(ResponseStatus.FOURZ21.getText());
                    // ftResponse.setResponseCode(ResponseStatus.FOURZ21.getValue());

                } else if (responseData.contains(CBSResponseStr.postingRestriction.getText())) {

                    /**
                     * For Posting Restriction
                     */

                    wrapper.setMessage(ResponseStatus.FOURZ22.getText());
                    wrapper.setResponseCode(ResponseStatus.FOURZ22.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());

                    // NOTE: Not With Builder
                    // ftResponse.setMessage(ResponseStatus.FOURZ22.getText());
                    // ftResponse.setResponseCode(ResponseStatus.FOURZ22.getValue());

                } else if (responseData.contains(CBSResponseStr.invalidMinus.getText())) {
                    /**
                     * Is Amount Negative Checking
                     */

                    wrapper.setMessage(ResponseStatus.FOURZ23.getText());
                    wrapper.setResponseCode(ResponseStatus.FOURZ23.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());

                    // NOTE: Not With Builder
                    // ftResponse.setMessage(ResponseStatus.FOURZ23.getText());
                    // ftResponse.setResponseCode(ResponseStatus.FOURZ23.getValue());

                } else if (responseData.contains(CBSResponseStr.valueAmountZero.getText())) {
                    /**
                     * Is Amount Zero Checking
                     */

                    wrapper.setMessage(ResponseStatus.FOURZ24.getText());
                    wrapper.setResponseCode(ResponseStatus.FOURZ24.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());
                    // NOTE: Not With Builder
                    // ftResponse.setMessage(ResponseStatus.FOURZ24.getText());
                    // ftResponse.setResponseCode(ResponseStatus.FOURZ24.getValue());

                } else if (responseData.contains(CBSResponseStr.historyRecordMissing.getText())) {

                    ftRef = firstPart[0];
                    wrapper.setFtRef(ftRef);
                    wrapper.setMessage(ResponseStatus.TWOZ6.getText());
                    wrapper.setResponseCode(ResponseStatus.TWOZ6.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());
                } else {
                    wrapper.setMessage(ResponseStatus.TWOZ1.getText());
                    wrapper.setResponseCode(ResponseStatus.TWOZ1.getValue());
                    wrapper.setFtStatus(FtStatus.FAILED.getValue());
                    // NOTE: Not With Builder
                    // ftResponse.setMessage(ResponseStatus.TWOZ1.getText());
                    // ftResponse.setResponseCode(ResponseStatus.TWOZ1.getValue());
                }
                break;
        }

        // if (statusFlag.equals("1")) {
        //     String ftRef = firstPart[0];
        //     String uniqueOperationTransactionId = spiltData[20].split("=")[1];

        //     wrapper.setFtRef(ftRef);
        //     wrapper.setMessage(reverseFlag == 0 ? ResponseStatus.TWOZ0.getText() : ResponseStatus.TWOZ5.getText());
        //     wrapper.setResponseCode(
        //             reverseFlag == 0 ? ResponseStatus.TWOZ0.getValue() : ResponseStatus.TWOZ5.getValue());
        //     wrapper.setFtStatus(reverseFlag == 0 ? EftStatus.SUCCESS.getValue() : EftStatus.REVERSED.getValue());
        //     wrapper.setUniqueOperationTransactionId(uniqueOperationTransactionId);

        // } else {

        //     if (responseData.contains(CBSResponseStr.alreadySuccessDuplicate.getText())) {

        //         String duplicateSuccess[] = responseData.split(",");
        //         String[] secondPart = duplicateSuccess[1].split("-");
        //         String ftRef = secondPart[2];
        //         // String category = secondPart[3];

        //         // NOTE: Not with Builder
        //         // ftResponse.setFtRef(ftRef);
        //         // ftResponse.setMessage(ResponseStatus.TWOZ2.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.TWOZ2.getValue());

        //         wrapper.setFtRef(ftRef);
        //         wrapper.setMessage(ResponseStatus.TWOZ2.getText());
        //         wrapper.setResponseCode(ResponseStatus.TWOZ2.getValue());
        //         wrapper.setFtStatus(EftStatus.SUCCESS.getValue());

        //     } else if (responseData.contains(CBSResponseStr.failedDuplicate.getText())) {

        //         wrapper.setMessage(ResponseStatus.TWOZ3.getText());
        //         wrapper.setResponseCode(ResponseStatus.TWOZ3.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());

        //         // NOTE: Not with Builder
        //         // ftResponse.setMessage(ResponseStatus.TWOZ3.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.TWOZ3.getValue());

        //     } else if (responseData.contains(CBSResponseStr.debitAccountMissing.getText())
        //             || responseData.contains(CBSResponseStr.customDebitAccountMissing.getText())) {

        //         wrapper.setMessage(ResponseStatus.FOURZ10.getText());
        //         wrapper.setResponseCode(ResponseStatus.FOURZ10.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());

        //         // NOTE: Not With Builder
        //         // ftResponse.setMessage(ResponseStatus.FOURZ10.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.FOURZ10.getValue());

        //     } else if (responseData.contains(CBSResponseStr.creditAccountMissing.getText())
        //             || responseData.contains(CBSResponseStr.customCreditAccountMissing.getText())) {

        //         wrapper.setMessage(ResponseStatus.FOURZ7.getText());
        //         wrapper.setResponseCode(ResponseStatus.FOURZ7.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());
        //         // NOTE:Not With Builder
        //         // ftResponse.setMessage(ResponseStatus.FOURZ7.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.FOURZ7.getValue());

        //     } else if (responseData.contains(CBSResponseStr.debitAccountLessBalance.getText())) {

        //         wrapper.setMessage(ResponseStatus.FOURZ14.getText());
        //         wrapper.setResponseCode(ResponseStatus.FOURZ14.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());
        //         // NOTE: Not With Builder
        //         // ftResponse.setMessage(ResponseStatus.FOURZ14.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.FOURZ14.getValue());

        //     } else if (responseData.contains(CBSResponseStr.unauthorizedOverdraft.getText())) {

        //         /**
        //          * For Unauthorized Overdraft
        //          */
        //         wrapper.setMessage(ResponseStatus.FOURZ21.getText());
        //         wrapper.setResponseCode(ResponseStatus.FOURZ21.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());

        //         // NOTE: Not With Builder
        //         // ftResponse.setMessage(ResponseStatus.FOURZ21.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.FOURZ21.getValue());

        //     } else if (responseData.contains(CBSResponseStr.postingRestriction.getText())) {

        //         /**
        //          * For Posting Restriction
        //          */

        //         wrapper.setMessage(ResponseStatus.FOURZ22.getText());
        //         wrapper.setResponseCode(ResponseStatus.FOURZ22.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());

        //         // NOTE: Not With Builder
        //         // ftResponse.setMessage(ResponseStatus.FOURZ22.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.FOURZ22.getValue());

        //     } else if (responseData.contains(CBSResponseStr.invalidMinus.getText())) {
        //         /**
        //          * Is Amount Negative Checking
        //          */

        //         wrapper.setMessage(ResponseStatus.FOURZ23.getText());
        //         wrapper.setResponseCode(ResponseStatus.FOURZ23.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());

        //         // NOTE: Not With Builder
        //         // ftResponse.setMessage(ResponseStatus.FOURZ23.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.FOURZ23.getValue());

        //     } else if (responseData.contains(CBSResponseStr.valueAmountZero.getText())) {
        //         /**
        //          * Is Amount Zero Checking
        //          */

        //         wrapper.setMessage(ResponseStatus.FOURZ24.getText());
        //         wrapper.setResponseCode(ResponseStatus.FOURZ24.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());
        //         // NOTE: Not With Builder
        //         // ftResponse.setMessage(ResponseStatus.FOURZ24.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.FOURZ24.getValue());

        //     } else if (responseData.contains(CBSResponseStr.historyRecordMissing.getText())) {

        //         String ftRef = firstPart[0];
        //         wrapper.setFtRef(ftRef);
        //         wrapper.setMessage(ResponseStatus.TWOZ6.getText());
        //         wrapper.setResponseCode(ResponseStatus.TWOZ6.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());
        //     } else {
        //         wrapper.setMessage(ResponseStatus.TWOZ1.getText());
        //         wrapper.setResponseCode(ResponseStatus.TWOZ1.getValue());
        //         wrapper.setFtStatus(EftStatus.FAILED.getValue());
        //         // NOTE: Not With Builder
        //         // ftResponse.setMessage(ResponseStatus.TWOZ1.getText());
        //         // ftResponse.setResponseCode(ResponseStatus.TWOZ1.getValue());
        //     }

        //     // NOTE: Not With Builder
        //     // ftResponse.setAdditionalInfo(additionalInfo);

        //     // NOTE: With Builder
        //     // ftResponse.addAdditionalInfo(additionalInfo)
        // }
        wrapper.setAdditionalInfo(additionalInfo);
        // return ResponseEntity.status(HttpStatus.OK).body(ftResponse);
        return wrapper;

    }

}
