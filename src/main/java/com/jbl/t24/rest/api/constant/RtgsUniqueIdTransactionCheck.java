package com.jbl.t24.rest.api.constant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.tccUtility.TccUtility;

@Service
public class RtgsUniqueIdTransactionCheck {

	
	@Value("${channel.name.trx}")
	private String transactionChannel;
	@Value("${channel.name.enq}")
	private String enquiryChannel;
	public String checkTransactionByUniqueId(String uniqueTransactionId) {
		try {
			// TccUtility tccUtility = new TccUtility("ISOLIST2");
			TccUtility tccUtility = new TccUtility(enquiryChannel);
			String requestOFS = String.format(OfsSources.UNIQUE_ID_ENQ_STRING, uniqueTransactionId);
			String responseData = tccUtility.sendRequest(requestOFS);
			if (responseData.startsWith("40")) {
				return responseData;
			} else {
				Map<String, String> mainData = processResponse(responseData);
				String mainDataStr = Mapper.mapToJsonString(mainData);
				return mainDataStr;
			}
		} catch (IOException e) {
			return "499";
		}
	}

	private static Map<String, String> processResponse(String responseData) {
		String[] allData = responseData.split(",");
		String mainDataString = allData[2].substring(1, allData[2].length() - 1);
		String[] mainData = mainDataString.split("\\~\\|");
		Map<String, String> processedData = new HashMap<String, String>();
		processedData.put("message", mainData[0]);
		processedData.put("responseCode", mainData[1]);
		processedData.put("cbsFtNumber", mainData[2]);
		processedData.put("debitAccNo", mainData[3]);
		processedData.put("creditAccNo", mainData[4]);
		processedData.put("ammount", mainData[5]);
		processedData.put("issueDate", mainData[6]);
		processedData.put("commission", mainData[7].isBlank()? "0.0" : mainData[7].substring(3));
		processedData.put("vat", mainData[8].isBlank()?"0.0" : mainData[8].substring(3));
		processedData.put("localAmountBdt", mainData[9]);
		processedData.put("ofsResponse", responseData);
		return processedData;

	}

}
