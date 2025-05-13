package com.jbl.t24.rest.api.model.base;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.NotAudited;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="password_reset_token")
@NamedQuery(name="PasswordResetToken.findAll", query="SELECT p FROM PasswordResetToken p")
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="pass_id", unique=true, nullable=false)
	private int passId;

	@Column(name="expiry_date")
	private Timestamp expiryDate;

	@Column(length=255)
	private String token;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	@NotAudited
	private UserAccount userAccount;

}