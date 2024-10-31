package com.jbl.t24.rest.api.model.rtgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rtgs_out_info")
@Setter
@Getter
@ToString
@Audited
public class RtgsInfoOutward extends RtgsCommon {
	// public class RtgsInfoOutward extends CommonFtInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rtgs_out_id", unique = true, nullable = false)
	private int OutwardRtgsInfoId;

	@NotBlank(message = "Unique ID is Blank")
	// @Pattern(regexp = "^(RTGS)[0-9]{16}$", message = "RTGS Unique ID Length Not
	// Valid")
	@Column(name = "out_unique_id", unique = true, nullable = false, length = 23)
	private String uniqueOutwardRtgsId;

	@Column(name = "COMMISSION", nullable = true, precision = 10, scale = 2)
	protected double commission;

	@Column(name = "VAT", nullable = true, precision = 10, scale = 2)
	protected double vat;

	@Column(name = "ibas_id", length = 100, nullable = true)
	protected String ibasId;
}
