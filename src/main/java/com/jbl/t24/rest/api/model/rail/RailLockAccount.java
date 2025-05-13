package com.jbl.t24.rest.api.model.rail;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rail_lock_info")
@Setter
@Getter
@ToString
@Audited
public class RailLockAccount extends RailCommon {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rail_lock_id", unique = true, nullable = false)
	private int railLockId;

	@NotBlank(message = "Unique ID is Blank")
	// @Pattern(regexp = "^(RTGS)[0-9]{16}$", message = "RTGS Unique ID Length Not
	// Valid")
	@Column(name = "at_unique_id", unique = true, nullable = false, length = 30)
	private String atUniqueId;

    @NotNull(message = "Lock Account is Missing")
	@Column(name = "account_to_lock", length = 63)
	protected String accountToLock;

    @NotNull(message = "Lock Amount is Missing")
	@Column(name = "lock_amount_str", length = 23)
	@JsonProperty("lockAmount")
	protected String lockAmount;

    @CreationTimestamp
	@Column(name = "lock_start_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = true, nullable = false)
	// @JsonFormat(pattern ="yy-MM-dd HH:mm:ss", timezone = "GMT+06")
	@JsonFormat(pattern ="yyyyMMdd")
	protected Date lockStartDate;

    @Column(name = "lock_end_date", columnDefinition = "TIMESTAMP", updatable = true, nullable = true)
	// @JsonFormat(pattern ="yy-MM-dd HH:mm:ss", timezone = "GMT+06")
	@JsonFormat(pattern ="yyyyMMdd")
	protected Date lockEndDate;

    @Column(name = "lock_details", unique = false, nullable = true, length = 30)
	protected String lockDetails;

    @Column(name = "request_time", columnDefinition = "TIMESTAMP", updatable = true, nullable = true)
	// @JsonFormat(pattern ="yy-MM-dd HH:mm:ss", timezone = "GMT+06")
	protected Timestamp requestTime;

    @Column(name = "response_time", columnDefinition = "TIMESTAMP", updatable = true, nullable = true)
	// @JsonFormat(pattern ="yy-MM-dd HH:mm:ss", timezone = "GMT+06")
	protected Timestamp responseTime;

}
