package com.jbl.t24.rest.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
@Builder
@Audited
public class RtgsInfoOutward extends CommonFtInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rtgs_out_id", unique = true, nullable = false)
	private int OutwardRtgsInfoId;

	@NotBlank(message = "Unique ID is Blank")
	//@Pattern(regexp = "^(RTGS)[0-9]{16}$", message = "RTGS Unique ID Length Not Valid")
	@Column(name = "out_unique_id", unique = true, nullable = false, length = 23)
	private String uniqueOutwardRtgsId;

	@Column(name = "transaction_type", length = 10)
	private String txType = "ACOR";
}
