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

@Entity
@Table(name = "rtgs_settlement_nine_in_info")

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Audited
public class RTGSSettlementNineInInfo extends RtgsCommon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "settlement_id", unique = true, nullable = false)
	private Long settlementId;

	@NotBlank(message = "Unique Settlement Number is Blank")
	@Column(name = "settlement_unique_id", unique = true, nullable = false, length = 23)
	private String uniqueSettlementtId;

	@Column(name = "reverse_enabled")
	protected boolean reverseEnabled = false;

}
