package com.jbl.t24.rest.api.tccUtility;

import java.io.IOException;
import java.net.InetAddress;

// import org.hibernate.annotations.common.util.impl.Log_.logger;

import com.jbl.t24.rest.api.enums.CBSResponseStr;
// import com.temenos.tocf.*;
import com.temenos.tocf.tcc.*;

public class TccUtility {

	private static boolean bValidate = false;

	// public static String channel = ""; // Channel Name from channel.xml
	public String channel = "";

	private static String sCharSet = null;

	private static TCCFactory tcf = null;

	public TccUtility() throws IOException {

		tcf = TCCFactory.getInstance();
		channel = "TEST_TRX";
	}

	public TccUtility(String ch) throws IOException {
		tcf = TCCFactory.getInstance();
		channel = ch;
	}

	/*
	 * 400 = ERROR IN SENDING REQUEST 
	 * 401 = CANNOT PING THE TC SERVER 
	 * 402 = REQUEST NOT VALID 
	 * 403 = INTERNAL ERROR 
	 * 404 = BLANK OFS ERROR 
	 * 405 = INVALID COMPANY SPECIFIED DURING SIGN ON PROCESS
	 * 408 = SECURITY VIOLATION DURING SIGN ON PROCESS
	 */

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String sendRequest(String sRequest) {

		System.out.println("------------CHANNEL NAME: " + channel+"-----------");
		String sResponse = "400";

		try {

			/**
			 * Create a Connection based on the channel name.
			 */

			TCConnection tcConnection = tcf.createTCConnection(channel);
			tcConnection.setMaximumRetryCount(1);
			tcConnection.setRetryInterval(2);

			/*
			 * Ping the TCS
			 */
			boolean bPing = tcConnection.ping();

			if (!bPing) {
				sResponse = "401";
			} else {

				TCRequest tcSendRequest = null;

				// OFS
				if (sCharSet != null) {
					String charsetOFS = new String(sRequest.getBytes(sCharSet));
					tcSendRequest = tcf.createOfsRequest(charsetOFS, bValidate);
				} else {
					tcSendRequest = tcf.createOfsRequest(sRequest, bValidate);
				}

				if (!tcSendRequest.isValid()) {
					sResponse = "402";
				} else {

					TCResponse tcResponse = null;

					tcResponse = tcSendRequest.send(tcConnection);
					String strOFSError = null;

					long nError = tcResponse.getErrorCode();
					if (nError != 0) {
						strOFSError = tcResponse.getErrorMessage();
					}

					sResponse = tcResponse.getOFSString();

					if (sResponse.length() == 1) {
						sResponse = "404";
					}

					if (sResponse.contains(CBSResponseStr.invalidCompanyCodeAssigned.getText())) {
						sResponse = "405";
					}
					if (sResponse.contains(CBSResponseStr.invalidOrNoSignOnNameSupplied.getText())) {
						sResponse = "406";
					}
					if (sResponse.contains(CBSResponseStr.uniqueIdNotFound.getText())) {
						sResponse = "407";
					}
					if (sResponse.contains(CBSResponseStr.securityViolationDuringSignon.getText())) {
						sResponse = "408";
					}

				}
			}
			/**
			 * Close the connection.
			 */
			tcConnection.close();

		} catch (TCClientException tcEx) {
			tcEx.printStackTrace();
			sResponse = "403";
		} catch (Exception ex) {
			sResponse = "403";
		}
		return sResponse;
	}

	private void sendPingRequest(String ipAddress) throws IOException {
		InetAddress geek = InetAddress.getByName(ipAddress);
		System.out.println("Sending Ping Request to " + ipAddress);
		if (geek.isReachable(5000))
			System.out.println("Host " + ipAddress + " is reachable");
		else
			System.out.println("Sorry ! We can't reach to this host");
	}

}
