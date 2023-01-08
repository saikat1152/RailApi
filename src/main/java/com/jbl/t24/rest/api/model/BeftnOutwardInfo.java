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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "beftn_out_info")
@Audited
public class BeftnOutwardInfo {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "beftn_out_id", unique = true, nullable = false)
	private int beftnOutId;

	@NotBlank(message = "Credit Narrative is Blank")
	@Pattern(regexp = "^(EFT)[0-9]{13}$", message = "Credit Narrative Length Not Valid")
	@Column(name = "credit_narrative", unique = true, nullable = false, length = 23)
	private String creditNarrative;
	
	@NotBlank(message = "Debit Narrative is Blank")
	@Pattern(regexp = "^(EFT)[0-9]{13}$", message = "Debit Narrative Length Not Valid")
	@Column(name = "debit_narrative", unique = true, nullable = true, length = 23)
	private String debitNarrative;

	@NotNull(message = "Version for Debit or Credit is Not Mentioned")
	@Min(value=1, message = "Version is not valid")
	@Max(value=3, message = "Version is not valid")
	@Column(name = "version", nullable = false, length = 1)
	private Integer version;
	
	@Column(name = "company_code", nullable = false, length = 9)
	private String companyCode;

	@Pattern(regexp = "^[0-9]{12}$|^[0-9]{13}$|^[0-9]{16}$", message = "Debit Account Length Not Valid")
	@Column(name = "debit_account", length = 63)
	private String debitAccNo;

	@Pattern(regexp = "(BDT)[0-9]{13}$", message = "Credit Account Length Not Valid")
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

	public BeftnOutwardInfo() {
		super();
	}
	

	public BeftnOutwardInfo(int beftnOutId, String creditNarrative, String debitNarrative, Integer version,
			String companyCode, String debitAccNo, String creditAccNo, Double debitAmount, Timestamp issueDate,
			String ofsRequest, String ofsResponse, int status, String ip, String currency, String hostName) {
		super();
		this.beftnOutId = beftnOutId;
		this.creditNarrative = creditNarrative;
		this.debitNarrative = debitNarrative;
		this.version = version;
		this.companyCode = companyCode;
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


	public int getBeftnOutId() {
		return beftnOutId;
	}

//	public void setBeftnOutId(int beftnOutId) {
//		this.beftnOutId = beftnOutId;
//	}

	public String getCreditNarrative() {
		return creditNarrative;
	}

	public void setCreditNarrative(String creditNarrative) {
		this.creditNarrative = creditNarrative;
	}

	public String getDebitNarrative() {
		return debitNarrative;
	}

	public void setDebitNarrative(String debitNarrative) {
		this.debitNarrative = debitNarrative;
	}
	
	

	public String getCompanyCode() {
		return companyCode;
	}





	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
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


	@Override
	public String toString() {
		return "BeftnOutwardInfo [beftnOutId=" + beftnOutId + ", creditNarrative=" + creditNarrative
				+ ", debitNarrative=" + debitNarrative + ", version=" + version + ", companyCode=" + companyCode
				+ ", debitAccNo=" + debitAccNo + ", creditAccNo=" + creditAccNo + ", debitAmount=" + debitAmount
				+ ", issueDate=" + issueDate + ", ofsRequest=" + ofsRequest + ", ofsResponse=" + ofsResponse
				+ ", status=" + status + ", ip=" + ip + ", currency=" + currency + ", hostName=" + hostName + "]";
	}
	
	

}
