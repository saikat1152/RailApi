package com.jbl.t24.rest.api.model.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.jbl.t24.rest.api.annotation.NameUnique;
import com.jbl.t24.rest.api.audit.Auditable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "request_types")
@NamedQuery(name = "RequestType.findAll", query = "SELECT rt FROM RequestType rt")
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@Audited
public class RequestType extends Auditable implements Serializable{

    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int reqTypeId;

    @NameUnique(groups = OnCreate.class)
	@Column(name = "req_type_name", length = 128, unique=true)
    @NotBlank
    private String reqTypeName;
}
