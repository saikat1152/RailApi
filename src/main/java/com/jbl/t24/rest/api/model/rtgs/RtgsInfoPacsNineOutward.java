package com.jbl.t24.rest.api.model.rtgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.jbl.t24.rest.api.common.model.IWithVatCommission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rtgs_out_pacs_nine_info")
@Setter
@Getter
@ToString
@Audited
public class RtgsInfoPacsNineOutward extends RtgsCommon implements IWithVatCommission{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rtgs_out_id", unique = true, nullable = false)
	private int OutwardRtgsInfoId;

	@NotBlank(message = "Unique ID is Blank")
	// @Pattern(regexp = "^(RTGS)[0-9]{16}$", message = "RTGS Unique ID Length Not
	// Valid")
	@Column(name = "out_unique_id", unique = true, nullable = false, length = 23)
	private String uniqueOutwardRtgsId;

	// @NotNull(message = "Bill Description is Missing")
	@Column(name = "bill_description", nullable = true, length = 35)
	private String billDescription;

	// @NotNull(message = "LC NUmber is Missing")
	@Column(name = "lc_number", nullable = true, length = 35)
	private String lcNumber;

	// @NotNull(message = "Party Name is Missing")
	@Column(name = "party_name", nullable = true, length = 35)
	private String partyName;

	// @NotNull(message = "Instruction Info is Missing")
	@Column(name = "instruction_info", nullable = true, length = 35)
	private String instructionInfo;

	// @NotNull(message = "Trade Finance Info is Missing")
	@Column(name = "trade_finance_info", nullable = true, length = 35)
	private String tradeFinanceInfo;

	// @NotNull(message = "Other Info is Missing")
	@Column(name = "other_info", nullable = true, length = 35)
	private String otherInfo;

	@Column(name = "COMMISSION", nullable = true, precision = 10, scale = 2)
	protected double commission;

	@Column(name = "VAT", nullable = true, precision = 10, scale = 2)
	protected double vat;

	@Column(name = "ref_unique_id", unique = true, nullable = true, length = 23)
	protected String refUniqueOutwardRtgsId;

	@Override
	public Double getCommissionI() {
		return commission;
	}

	@Override
	public void setCommissionI(Double commision) {
		this.commission = commision;
	}

	@Override
	public Double getVatI() {
	return vat;
	}

	@Override
	public void setVatI(Double vat) {
		this.vat = vat;
	}

}
