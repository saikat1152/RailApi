package com.jbl.t24.rest.api.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Audited
public abstract class CommonFtInfo {

	@Transient
    private final String ACCOUNT_NUMBER_PATTERN = "(^BDT[0-9]{13}$)|((^[0-9]{12}$)|(^[0-9]{13}$)|(^[0-9]{16}$))";

    @Transient
    @NotNull(message = "Branch Code Missing")
    @Pattern(regexp = "^[0-9]{4}$", message = "Branch Code Pattern Not Valid")
    private String coCode;

    @Column(name = "company_code", length = 10)
    private String companyCode = "BD001";

    @Column(name = "transaction_category", length = 10)
    private String txCategory = "RTGS";



    @Pattern(regexp = ACCOUNT_NUMBER_PATTERN, message = "Debit Account Pattern Not Valid")
	@Column(name = "debit_account", length = 63)
    private String debitAccNo;

    @Column(name = "currency", length = 5)
    @NotNull(message = "Currency is Missing")
    private String currency;
    
    @NotNull(message = "Debit Amount is Missing")
	@Column(name = "debit_amount", length = 23)
    private double debitAmount;

    @CreationTimestamp
    @Column(name = "issue_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, nullable = false)
    private Timestamp issueDate;

    @Pattern(regexp = ACCOUNT_NUMBER_PATTERN, message = "Credit Account Pattern Not Valid")
	@Column(name = "credit_account", length = 63)
    private String creditAccNo;

    @Column(name = "debit_details", unique = true, nullable = false, length = 256)
    private String debitDetails;

    @Column(name = "credit_details", unique = true, nullable = false, length = 256)
    private String creditDetails;
    private String commissionType;
    private String instrumentNo;

    @Column(name = "status", length = 2)
    private int status;

    @Column(name = "ofs_request", length = 1048)
    private String ofsRequest;

    @Column(name = "ofs_response", length = 2048)
    private String ofsResponse;

    @Column(name = "ip", length = 63)
    private String ip;

    @Column(name = "hostname", length = 127)
    private String hostname;

    @Column(name = "cbs_ft_number", length = 127)
    private String cbsFtno;//[idx 0]

    @Column(name = "ft_response_str", length = 512)
    private String ftResponseStr;
}
