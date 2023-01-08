package com.jbl.t24.rest.api.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import lombok.ToString;

@Entity
@Table(name = "beftn_in_info")
@Audited
@ToString
public class BeftnInwardInfo {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "beftn_in_id", unique = true, nullable = false)
	private int beftnInId;

	@NotBlank(message = "Narrative is Blank")
	@Pattern(regexp = "^(EFT)[0-9]{13}$", message = "Narrative Length Not Valid")
	@Column(name = "narrative", unique = true, nullable = false, length = 23)
	private String narrative;

	@NotNull(message = "Version for Debit or Credit is Not Mentioned")
	@Min(value=1, message = "Version is not valid")
	@Max(value=2, message = "Version is not valid")
	@Column(name = "version", nullable = false, length = 1)
	private Integer version;

//	@NotBlank(message = "Debit Account is Missing")
	@Pattern(regexp = "(BDT)[0-9]{13}$", message = "Debit Account Length Not Valid")
	@Column(name = "debit_account", length = 63)
	private String debitAccNo;

//	@NotBlank(message = "Credit Account is Missing")
	@Pattern(regexp = "^[0-9]{12}$|^[0-9]{13}$|^[0-9]{16}$", message = "Credit Account Length Not Valid")
	@Column(name = "credit_account", length = 23)
	private String creditAccNo;

	@NotNull(message = "Debit Amount is Missing")
	@Column(name = "debit_amount", length = 23)
	private Double debitAmount;
	
	@CreationTimestamp
	@Column(name = "issue_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, nullable = false)
	private Timestamp issueDate;

	@Column(name = "ofs_request", length = 1048)
	private String ofsRequest;

	@Column(name = "ofs_response", length = 2048)
	private String ofsResponse;

	@Column(name = "status", length = 2)
	private int status;

	@Column(name = "ip", length = 63)
	private String ip;

	@Column(name = "currency", length = 5)
	private String currency;

	@Column(name = "hostname", length = 127)
	private String hostName;

//    @Column(name = "hostname", length = 127)
//	private String hostName;

	public BeftnInwardInfo() {
		super();
	}

	public BeftnInwardInfo(int beftnInId, String narrative, Integer version, String debitAccNo, String creditAccNo,
			Double debitAmount, Timestamp issueDate, String ofsRequest, String ofsResponse, int status, String ip,
			String currency, String hostName) {
		super();
		this.beftnInId = beftnInId;
		this.narrative = narrative;
		this.version = version;
		this.debitAccNo = debitAccNo;
		this.creditAccNo = creditAccNo;
		this.debitAmount = debitAmount;
		this.issueDate = issueDate;
		this.ofsRequest = ofsRequest;
		this.ofsResponse = ofsResponse;
		this.status = status;
		this.ip = ip;
		this.currency = currency;
		this.hostName = hostName;
	}

	public int getBeftnInId() {
		return beftnInId;
	}

//	public void setBeftnInId(int beftnInId) {
//		this.beftnInId = beftnInId;
//	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDebitAccNo() {
		return debitAccNo;
	}

	public void setDebitAccNo(String debitAccNo) {
		this.debitAccNo = debitAccNo;
	}

	public String getCreditAccNo() {
		return creditAccNo;
	}

	public void setCreditAccNo(String creditAccNo) {
		this.creditAccNo = creditAccNo;
	}

	public Double getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}

	public Timestamp getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Timestamp issueDate) {
		this.issueDate = issueDate;
	}

	public String getOfsRequest() {
		return ofsRequest;
	}

	public void setOfsRequest(String ofsRequest) {
		this.ofsRequest = ofsRequest;
	}

	public String getOfsResponse() {
		return ofsResponse;
	}

	public void setOfsResponse(String ofsResponse) {
		this.ofsResponse = ofsResponse;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

//	public int getBeftnId() {
//		return beftnInId;
//	}
//
////	public void setBeftnId(int beftnId) {
////		this.beftnInId = beftnId;
////	}
//	
//	
//
//	public String getCreditNarrative() {
//		return narrative;
//	}
//
//	public void setCreditNarrative(String creditNarrative) {
//		this.narrative = creditNarrative;
//	}
//
//	public String getDebitAccNo() {
//		return debitAccNo;
//	}
//
//	public void setDebitAccNo(String debitAccNo) {
//		this.debitAccNo = debitAccNo;
//	}
//
//	public String getCreditAccNo() {
//		return creditAccNo;
//	}
//
//	public void setCreditAccNo(String creditAccNo) {
//		this.creditAccNo = creditAccNo;
//	}
//
//	public Double getDebitAmount() {
//		return debitAmount;
//	}
//
//	public void setDebitAmount(Double debitAmount) {
//		this.debitAmount = debitAmount;
//	}
//
//	public Timestamp getIssueDate() {
//		return issueDate;
//	}
//
//	public void setIssueDate(Timestamp issueDate) {
//		this.issueDate = issueDate;
//	}
//
//	public String getOfsRequest() {
//		return ofsRequest;
//	}
//
//	public void setOfsRequest(String ofsRequest) {
//		this.ofsRequest = ofsRequest;
//	}
//
//	public String getOfsResponse() {
//		return ofsResponse;
//	}
//
//	public void setOfsResponse(String ofsResponse) {
//		this.ofsResponse = ofsResponse;
//	}
//
//	public int getStatus() {
//		return status;
//	}
//
//	public void setStatus(int status) {
//		this.status = status;
//	}
//
//	public String getIp() {
//		return ip;
//	}
//
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
//
//	public String getCurrency() {
//		return currency;
//	}
//
//	public void setCurrency(String currency) {
//		this.currency = currency;
//	}
//
//	public String getHostName() {
//		return hostName;
//	}
//
//	public void setHostName(String hostName) {
//		this.hostName = hostName;
//	}

}
