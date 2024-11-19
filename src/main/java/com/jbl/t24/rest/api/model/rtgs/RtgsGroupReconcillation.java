package com.jbl.t24.rest.api.model.rtgs;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name="rtgs_fc_recon_group")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Builder
public class RtgsGroupReconcillation  

{

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rtgs_grp_recon_id", unique = true, nullable = false)
	private Long groupRtgsReconId;

    @NotBlank(message = "Group Unique ID is Blank")
	@Column(name = "grp_recon_unique_id", unique = true, nullable = false, length = 23)
    protected String reconGroupUniqueID;

    @Column(name = "recon_category", length = 10)
    String reconCatergory;

    // @Column(name = "individual_ids")
    // List<String> individualReconIds;

    @Column(name = "indiv_recons", length = 70)
    @OneToMany(mappedBy = "reconGroup", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    List<RtgsReconIndividual> individualReconList;


    @Column(name = "status", length = 2)
	protected int status;

    @Column(name ="grp_response", length=2048)
    protected String groupResponse;
}
