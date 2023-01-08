package com.jbl.t24.rest.api.audit;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = "revision_info")
@RevisionEntity(AuditRevisionListener.class)
@AttributeOverrides({ @AttributeOverride(name = "timestamp", column = @Column(name = "rev_timestamp")),
		@AttributeOverride(name = "id", column = @Column(name = "revision_id")) })

public class AuditRevisionEntity extends DefaultRevisionEntity {

	private static final long serialVersionUID = 1L;

	private String logUser;

	public AuditRevisionEntity() {
		super();
	}

	public String getLogUser() {
		return logUser;
	}

	public void setLogUser(String logUser) {
		this.logUser = logUser;
	}

	@Override
	public String toString() {
		return "AuditRevisionEntity [logUser=" + logUser + "]";
	}

}
