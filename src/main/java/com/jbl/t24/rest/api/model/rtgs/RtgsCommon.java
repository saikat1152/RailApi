package com.jbl.t24.rest.api.model.rtgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jbl.t24.rest.api.enums.utils.DoubleEqualityCheck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Audited
// @Builder
@SuperBuilder
public class RtgsCommon extends CommonFtInfo{
    
    @Column(name = "CATEGORY_CODE", nullable = true, length = 10)
	protected int categoryCode;
    
    @Column(name = "is_fc", nullable = true)
	protected Boolean isFc;

	// @NotNull(message = "Debit Amount is Missing")
	// @Column(name = "debit_amount_str", length = 23)
	@Column(name = "debit_amount_str", length = 23)
	@JsonProperty("debitAmount")
	protected String debitAmountStr;

    public boolean isEqual(RtgsCommon ftInfo){
		if(this.debitAccNo.equals(ftInfo.debitAccNo)
		&& this.creditAccNo.equals(ftInfo.creditAccNo)
		&& this.debitAmountStr.equals(ftInfo.debitAmountStr)
		// &&DoubleEqualityCheck.isEqual(this.debitAmount, ftInfo.debitAmount)
		){
			System.out.println("debit amount " + ftInfo.debitAmountStr + " ll: "+ this.debitAmountStr);
			return true;
		}
		System.out.println("debit amount " + ftInfo.debitAmountStr + " ll: "+ this.debitAmountStr);
		return false;
	}
}
