package com.jbl.t24.rest.api.model.rtgs;

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
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

// @Entity
// @Table
@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Builder
public class RtgsGroupReconcillation  

{

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rtgs_grp_recon_id", unique = true, nullable = false)
	private int groupRtgsReconId;

    @NotBlank(message = "Group Unique ID is Blank")
	@Column(name = "grp_recon_unique_id", unique = true, nullable = false, length = 23)
    protected String reconGroupUniqueID;

    @Column(name = "individual_ids")
    List<String> individualReconIds;

}
