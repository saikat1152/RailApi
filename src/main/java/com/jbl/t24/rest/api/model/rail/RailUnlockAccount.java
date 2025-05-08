package com.jbl.t24.rest.api.model.rail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@AllArgsConstructor
// @NoArgsConstructor
@Entity
@Table(name = "rail_unlock_info")
@Setter
@Getter
@ToString
@Audited
public class RailUnlockAccount extends RailCommon {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rail_unlock_id", unique = true, nullable = false)
	private int railUnlockId;

    @NotBlank(message = "Lock Ref Id is Blank")
	// @Pattern(regexp = "^(RTGS)[0-9]{16}$", message = "RTGS Unique ID Length Not
	// Valid")
	@Column(name = "lock_ref_id", unique = true, nullable = false, length = 30)
	private String lockRefId;


    @NotBlank(message = "Unique ID is Blank")
	// @Pattern(regexp = "^(RTGS)[0-9]{16}$", message = "RTGS Unique ID Length Not
	// Valid")
	@Column(name = "at_unique_id", unique = true, nullable = false, length = 30)
	private String atUniqueId;

}
