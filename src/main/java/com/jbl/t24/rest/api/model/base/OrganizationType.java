package com.jbl.t24.rest.api.model.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization_types")
@NamedQuery(name = "OrganizationType.findAll", query = "SELECT org FROM OrganizationType org")
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@Audited
public class OrganizationType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int OrganizationTypeId;

	@Column(name = "name", length = 127)
    private String OrganizationName;

	@Override
	public String toString(){
		return this.OrganizationName;
	}

}