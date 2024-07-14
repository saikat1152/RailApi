package com.jbl.t24.rest.api.model.base;

import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;


@Entity
@Table(name = "companies")
@NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c")
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@Audited
public class Company implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", unique = true, nullable = false)
    private Integer companyId;

    @Column(name = "account_with", length = 63)
    private String accountWith;

    @Column(name = "company_address", length = 200)
    private String companyAddress;

    @Column(name = "company_name", length = 60)
    private String companyName;

    @Column(name = "covered_account", length = 45)
    private String coveredAccount;

    @Column(name = "country_name", length = 63)
    private String CountryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_type", nullable = false)
    @NotAudited
    private OrganizationType organizationType;

    @Column(name = "note_copy", length = 127)
    private String noteCopy;

    @Override
    public String toString(){
        return this.companyName;
    }

 }