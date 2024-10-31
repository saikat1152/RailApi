package com.jbl.t24.rest.api.service.rtgs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.config.HostIpHandle;
import com.jbl.t24.rest.api.constant.OfsSources;
import com.jbl.t24.rest.api.enums.utils.RtgsTransactionConstants;
import com.jbl.t24.rest.api.model.rtgs.RtgsReconIndividual;

@Service
public class ReconTransactionService {

    @Autowired
	private FtHandlerServiceN ftHandlerServicen;

    public ResponseEntity<?> handleReconTransaction(RtgsReconIndividual reconIndividual, HttpServletRequest httpServletRequest)
            throws Exception {

        String uniqueFtId = reconIndividual.getInidvidualReconUniqueId();
        String coCode = reconIndividual.getCoCode();
        String companyCode = reconIndividual.getCompanyCode() + coCode;
        reconIndividual.setCompanyCode(companyCode);
        String txCategory = reconIndividual.getTxCategory();
        String txType = reconIndividual.getTxType();
        // reconIndividual.setTxType(txType);
        // reconIndividual.setCategoryCode(RtgsTransactionConstants.rtgsConstants.get(txCategory).getCode());
        String debitAccNo = reconIndividual.getDebitAccNo();
        String debitCurrency = reconIndividual.getDebitCurrency();
        String debitAmount = reconIndividual.getDebitAmountStr();
        
        reconIndividual.setCurrency(debitCurrency);

        String currency = reconIndividual.getCurrency();
        // String debitAmount = String.format("%.2f", reconIndividual.getDebitAmount());
     
        String creditAccNo = reconIndividual.getCreditAccNo();
        String creditCurrency = reconIndividual.getCreditCurrency();
        String creditAmount = reconIndividual.getCreditAmountStr();

        String debitDetails = reconIndividual.getDebitDetails();
        String creditDetails = reconIndividual.getCreditDetails();

        String commissionCode = reconIndividual.getCommissionCode();
        String commissionType = reconIndividual.getCommissionType();

        boolean isFc = reconIndividual.getCurrency().contains("BDT") ? false : true;
        reconIndividual.setIsFc(isFc);


        String issueDate = new SimpleDateFormat("YYYYMMdd").format(new Date());
        Timestamp t = new Timestamp(new Date().getTime());
        reconIndividual.setIssueDate(t);

     
        if (reconIndividual.getReverseEnabled() != null) {
            reconIndividual.setReverseEnabled(true);
        }

        issueDate = "20220506";

        String  requestOFS = String.format(OfsSources.RECON_OFS_STRING, companyCode, txType, debitAccNo,
				debitCurrency, debitAmount, issueDate, creditAccNo, debitDetails, creditDetails, commissionCode,
				commissionType, uniqueFtId,creditCurrency, creditAmount);


        Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		reconIndividual.setHostname(hostIpData.get("host"));
		reconIndividual.setIp(hostIpData.get("remoteAddr"));
        
        ResponseEntity<?> response = ftHandlerServicen.handleFtTransaction(requestOFS, reconIndividual, uniqueFtId);

        System.out.println(response.getBody());
        
        return response;

    }

}
