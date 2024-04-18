package com.jbl.t24.rest.api.enums.utils;

import java.util.Map;
import java.util.HashMap;

public class RtgsTransactionConstants {
	public static Map<String, ConstantValues> rtgsConstants = new HashMap<>();
	static {

		rtgsConstants.put("BDT_08_OUT", ConstantValues.builder().transactionType("ACOR").code(8001).build());
		rtgsConstants.put("EPAY_08_OUT", ConstantValues.builder().transactionType("ACRC").code(8002).build());
		rtgsConstants.put("FC_08_OUT_USD", ConstantValues.builder().transactionType("ACRO").code(8003).build());
		rtgsConstants.put("FC_08_OUT_EUR", ConstantValues.builder().transactionType("ACRO").code(8004).build());
		rtgsConstants.put("FC_08_OUT_GBP", ConstantValues.builder().transactionType("ACRO").code(8005).build());
		rtgsConstants.put("FC_08_OUT_JPY", ConstantValues.builder().transactionType("ACRO").code(8006).build());
		rtgsConstants.put("FC_08_OUT_CNY", ConstantValues.builder().transactionType("ACRO").code(8007).build());

		rtgsConstants.put("BDT_08_IN", ConstantValues.builder().transactionType("ACIR").code(8501).build());
		rtgsConstants.put("FC_09_IN_USD", ConstantValues.builder().transactionType("ACRI").code(8503).build());
		rtgsConstants.put("FC_08_IN_EUR", ConstantValues.builder().transactionType("ACRI").code(8504).build());
		rtgsConstants.put("FC_08_IN_GBP", ConstantValues.builder().transactionType("ACRI").code(8505).build());
		rtgsConstants.put("FC_08_IN_JPY", ConstantValues.builder().transactionType("ACRI").code(8506).build());
		rtgsConstants.put("FC_08_IN_CNY", ConstantValues.builder().transactionType("ACRI").code(8507).build());

		rtgsConstants.put("BDT_09_OUT", ConstantValues.builder().transactionType("ACOP").code(9001).build());
		rtgsConstants.put("FC_09_OUT_USD", ConstantValues.builder().transactionType("ACOP").code(9003).build());
		rtgsConstants.put("FC_09_OUT_EUR", ConstantValues.builder().transactionType("ACOP").code(9004).build());
		rtgsConstants.put("FC_09_OUT_GBP", ConstantValues.builder().transactionType("ACOP").code(9005).build());
		rtgsConstants.put("FC_09_OUT_JPY", ConstantValues.builder().transactionType("ACOP").code(9006).build());
		rtgsConstants.put("FC_09_OUT_CNY", ConstantValues.builder().transactionType("ACOP").code(9007).build());

		rtgsConstants.put("BDT_09_IN", ConstantValues.builder().transactionType("ACIN").code(9501).build());
		rtgsConstants.put("FC_09_IN_USD", ConstantValues.builder().transactionType("ACIN").code(9503).build());
		rtgsConstants.put("FC_09_IN_EUR", ConstantValues.builder().transactionType("ACIN").code(9504).build());
		rtgsConstants.put("FC_09_IN_GBP", ConstantValues.builder().transactionType("ACIN").code(9505).build());
		rtgsConstants.put("FC_09_IN_JPY", ConstantValues.builder().transactionType("ACIN").code(9506).build());
		rtgsConstants.put("FC_09_IN_CNY", ConstantValues.builder().transactionType("ACIN").code(9507).build());

		rtgsConstants.put("BDT_08_OUT_S", ConstantValues.builder().transactionType("ACOS").code(6801).build());
		rtgsConstants.put("FC_08_OUT_USD_S", ConstantValues.builder().transactionType("ACOS").code(6803).build());
		rtgsConstants.put("FC_08_OUT_EUR_S", ConstantValues.builder().transactionType("ACOS").code(6804).build());
		rtgsConstants.put("FC_08_OUT_GBP_S", ConstantValues.builder().transactionType("ACOS").code(6805).build());
		rtgsConstants.put("FC_08_OUT_JPY_S", ConstantValues.builder().transactionType("ACOS").code(6806).build());
		rtgsConstants.put("FC_08_OUT_CNY_S", ConstantValues.builder().transactionType("ACOS").code(6807).build());

		rtgsConstants.put("BDT_08_IN_S", ConstantValues.builder().transactionType("ACIS").code(7801).build());
		rtgsConstants.put("FC_08_IN_USD_S", ConstantValues.builder().transactionType("ACIS").code(7803).build());
		rtgsConstants.put("FC_08_IN_EUR_S", ConstantValues.builder().transactionType("ACIS").code(7804).build());
		rtgsConstants.put("FC_08_IN_GBP_S", ConstantValues.builder().transactionType("ACIS").code(7805).build());
		rtgsConstants.put("FC_08_IN_JPY_S", ConstantValues.builder().transactionType("ACIS").code(7806).build());
		rtgsConstants.put("FC_08_IN_CNY_S", ConstantValues.builder().transactionType("ACIS").code(7807).build());

	}

}
