package com.jbl.t24.rest.api.audit;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Audited
public abstract class Auditable {

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private int createdBy;

	@Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
	@CreatedDate
	private Date createdDate;

	@Column(name = "updated_by", updatable = true, columnDefinition = "integer default 0")
	@LastModifiedBy
	private int updatedBy;

	@Column(name = "updated_date", columnDefinition = "timestamp NULL", updatable = true)
	@LastModifiedDate
	private Timestamp updatedDate;

}
