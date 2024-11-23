package com.jbl.t24.rest.api.model.rtgs;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbl.t24.rest.api.custom.validation.ValidTxCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Audited
@SuperBuilder
public abstract class CommonFtInfo {

	/*
	 * @Transient protected final String ACCOUNT_NUMBER_PATTERN =
	 * "(^BDT[0-9]{13}$)|((^[0-9]{12}$)|(^[0-9]{13}$)|(^[0-9]{16}$))";
	 */

//    @Transient
	@Column(name = "CO_CODE", length = 10)
	@NotNull(message = "Branch Code Missing")
	@Pattern(regexp = "^[0-9]{4}$", message = "Branch Code Pattern Not Valid", flags = Flag.CASE_INSENSITIVE)
	protected String coCode;


	@Column(name = "company_code", length = 10)
	protected String companyCode = "BD001";

	@Column(name = "transaction_category", length = 20)
	@ValidTxCategory
	protected String txCategory;
	// protected String txCategory = "RTGS";

	// @Pattern(regexp = ACCOUNT_NUMBER_PATTERN, message = "Debit Account Pattern
	// Not Valid")
	@NotNull(message = "Debit Account is Missing")
	@Column(name = "debit_account", length = 63)
	protected String debitAccNo;

	@Column(name = "currency", length = 5)
	@NotNull(message = "Currency is Missing")
	protected String currency;


	@NotNull(message = "Debit Amount is Missing")
	@Column(name = "debit_amount", length = 23)
	@JsonProperty("amount")
	protected double debitAmount;

	@CreationTimestamp
	@Column(name = "issue_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = true, nullable = false)
	@JsonFormat(pattern ="yy-MM-dd HH:mm:ss")
	protected Timestamp issueDate;

	// @Pattern(regexp = ACCOUNT_NUMBER_PATTERN, message = "Credit Account Pattern
	// Not Valid")
	@Column(name = "credit_account", length = 63)
	protected String creditAccNo;

	@Column(name = "debit_details", unique = false, nullable = true, length = 256)
	protected String debitDetails;

	@Column(name = "credit_details", unique = false, nullable = true, length = 256)
	protected String creditDetails;

	@Column(name = "commission_code", unique = false, nullable = true, length = 20)
	protected String commissionCode;

	@Column(name = "COMMISSION_TYPE")
	protected String commissionType;

	@Column(name = "INSTRUMENT_NO")
	protected String instrumentNo;

	@Column(name = "status", length = 2)
	protected int status;

	/*
	 * @Column(name = "category", nullable = false, length = 2) private int
	 * category;
	 */

	@Column(name = "ofs_request", length = 1048)
	protected String ofsRequest;

	@Column(name = "ofs_response", length = 2048)
	protected String ofsResponse;

	@Column(name = "ip", length = 63)
	protected String ip;

	@Column(name = "hostname", length = 127)
	protected String hostname;

	@Column(name = "cbs_ft_number", length = 127)
	protected String cbsFtno;// [idx 0]

	@Column(name = "ft_response_str", length = 512)
	protected String ftResponseStr;

	// @CreationTimestamp
	@Column(name = "reverse_date", columnDefinition = "TIMESTAMP", updatable = true, nullable = true)
	@JsonFormat(pattern ="yy-MM-dd HH:mm:ss", timezone = "GMT+06")
	protected Timestamp reverseDate;

	@Column(name = "transaction_type")
	protected String txType;

	// @Column(name = "reverse_enabled")
	// protected boolean reverseEnabled = true;

	@Column(name = "reverse_enabled", nullable = true)
	protected Boolean reverseEnabled;



}
