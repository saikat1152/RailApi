package com.jbl.t24.rest.api.model.rtgs;

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
@Table(name = "rtgs_in_info")
@Getter
@Setter
@ToString
@Builder
@Audited
public class RtgsInfoInward extends CommonFtInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rtgs_in_id", unique = true, nullable = false)
	private int InwardRtgsInfoId;

	@NotBlank(message = "Unique ID is Blank")
	@Pattern(regexp = "^(RTGS)[0-9]{16}$", message = "RTGS Unique ID Length Not Valid")
	@Column(name = "in_unique_id", unique = true, nullable = false, length = 23)
	private String uniqueInwardRtgsId;

	@Column(name = "category", nullable = false, length = 2)
	private int category;

	@Column(name = "transaction_type", length = 10)
	private String txType = "ACIR";

}
