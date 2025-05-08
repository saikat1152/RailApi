package com.jbl.t24.rest.api.model.rail;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "rail_fund_transfer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
// @Builder
@Audited
public class RailFundTransfer extends RailCommon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rail_ft_id", unique = true, nullable = false)
    private int railFtId;

    @NotBlank(message = "Unique EFT Number is Blank")
    // @Pattern(regexp = "^(BEFT)[0-9]{16}$", message = "EFT Unique ID Length Not
    // Valid")
    @Column(name = "unique_ft_id", unique = true, nullable = false, length = 23)
    private String uniqueftId;

    // @Pattern(regexp = ACCOUNT_NUMBER_PATTERN, message = "Debit Account Pattern Not Valid")
    @NotNull(message = "Debit Account is Missing")
	@Column(name = "debit_account", length = 63)
    protected String debitAccNo;

    
    @NotNull(message = "Debit Amount is Missing")
	@Column(name = "debit_amount", length = 30)
    protected double debitAmount;
  
     @CreationTimestamp
    @Column(name = "issue_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ", updatable = true, nullable = false)
    protected Timestamp issueDate;

    // @Pattern(regexp = ACCOUNT_NUMBER_PATTERN, message = "Credit Account Pattern Not Valid")
    @NotNull(message = "Credit Account is Missing")
	@Column(name = "credit_account", length = 63)
    protected String creditAccNo;

    @Column(name = "currency", length = 5)
    @NotNull(message = "Currency is Missing")
    protected String currency;

    @Column(name = "debit_details", unique = false, nullable = true, length = 256)
    protected String debitDetails;

    @Column(name = "credit_details", unique = false, nullable = true, length = 256)
    protected String creditDetails;

    @Column(name = "status", length = 2)
    protected int status;

    @Column(name = "ofs_request", length = 1048)
    protected String ofsRequest;

    @Column(name = "ofs_response", length = 2048)
    protected String ofsResponse;

    @Column(name = "cbs_ft_number", length = 127)
    protected String cbsFtno;//[idx 0]

    @Column(name = "ft_response_str", length = 512)
    protected String ftResponseStr;

    @Column(name = "transaction_type")
    private String txType = "ACMA";

}
