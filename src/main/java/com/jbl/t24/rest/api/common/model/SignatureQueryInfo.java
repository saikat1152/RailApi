package com.jbl.t24.rest.api.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sign_query_info")
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
public class SignatureQueryInfo extends CommonQueryInfo {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sign_query_id", unique = true, nullable = false)
	int signQueryId;

    //@Pattern(regexp = "^[0-9]{12}$|^[0-9]{13}$|^[0-9]{16}$", message = "Credit Account Length Not Valid")
	@Column(name = "account_number", length = 63)
    @NotEmpty
    String accountNumber;

}
