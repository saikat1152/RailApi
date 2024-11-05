package com.jbl.t24.rest.api.model.rtgs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "rtgs_fc_recon_indiv")
@Data
// @MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Audited
@SuperBuilder
@Builder
public class RtgsReconIndividual extends RtgsCommon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rtgs_recon_id", unique = true, nullable = false)
    private Long reconRtgsId;

    // @Column(name = "CO_CODE", length = 10)
    // @NotNull(message = "Branch Code Missing")
    // @Pattern(regexp = "^[0-9]{4}$", message = "Branch Code Pattern Not Valid",
    // flags = Flag.CASE_INSENSITIVE)
    // protected String coCode;



    @Builder.Default
    @Column(name = "company_code", length = 10)
    protected String companyCode = "BD001";

    @Column(name = "credit_amount_str", length = 23)
    // @JsonProperty("creditAmount")
    String creditAmountStr;

    @Column(name = "debit_currency", length = 23)
    String debitCurrency;
    @Column(name = "credit_currency", length = 23)
    String creditCurrency;

    @Column(name = "indiv_recon_id", unique = true, nullable = false, length = 23)
    String inidvidualReconUniqueId;

    @ManyToOne
    @JoinColumn(name = "rtgs_grp_recon_id", nullable = false)
    @JsonBackReference
    private RtgsGroupReconcillation reconGroup;

}
