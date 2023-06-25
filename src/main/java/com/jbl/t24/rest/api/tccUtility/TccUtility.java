package com.jbl.t24.rest.api.tccUtility;

import java.io.IOException;
import java.net.InetAddress;

import com.jbl.t24.rest.api.custom.exception.BlankOfsResponseException;
import com.temenos.tocf.tcc.*;

public class TccUtility {

	private static boolean bValidate = false;

	private static String channel = ""; // Channel Name from channel.xml

	private static String sCharSet = null;

	private static TCCFactory tcf = null;

	public TccUtility() throws IOException {

		tcf = TCCFactory.getInstance();

		// System.out.println("tcf object " + tcf);

		// sendPingRequest("10.180.10.60");
		channel = "ISOLIST2";
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
	 * 405 = INVALID COMPANY SPECIFIED DURING SIGN ON PROCESS
	 */

	public String sendRequest(String sRequest) throws BlankOfsResponseException {
		String sResponse = "400";

		// System.out.println("Request OFS: " + sRequest);

		try {

			/**
			 * Create a Connection based on the channel name.
			 */
			// long lTime = System.currentTimeMillis();
			// System.out.println("The time is " + lTime);
			// System.out.println("tcf " + tcf);
			TCConnection tcConnection = tcf.createTCConnection(channel);
			// System.out.println("tcConnection: " + tcConnection);
			// System.out.println("Connection created successfully.");
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

					// String strOFSResponse = null;
					String strOFSError = null;

					long nError = tcResponse.getErrorCode();
					// System.out.println("nError: " + nError);
					if (nError != 0) {
						strOFSError = tcResponse.getErrorMessage();
						// System.out.println(strOFSError);
					}

					sResponse = tcResponse.getOFSString();

					if (sResponse.length() == 1) {
						throw new BlankOfsResponseException("Response None Due to Unresponsive CBS.");
					}
					if(sResponse.toLowerCase().contains("INVALID COMPANY SPECIFIED DURING SIGN ON PROCESS".toLowerCase()))
						sResponse="405";
					// System.out.println("len "+sResponse.trim());
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
