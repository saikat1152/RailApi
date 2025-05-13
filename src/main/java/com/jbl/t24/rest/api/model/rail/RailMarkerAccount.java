package com.jbl.t24.rest.api.model.rail;

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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rail_marker_info")
@Setter
@Getter
@ToString
@Audited
public class RailMarkerAccount extends RailCommon {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rail_marker_id", unique = true, nullable = false)
	private int railMarkerId;

    @NotNull(message = "Marker Account is Missing")
	@Column(name = "account_number", unique = false, nullable = false, length = 63)
	protected String accountToMark;

	@NotNull(message = "Marker Type is Missing")
    @Column(name = "marker_type", unique = false, nullable = false, length = 10)
    protected String markerType;

	@CreationTimestamp
    @Column(name = "marker_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ", updatable = true, nullable = true)
    protected Timestamp markerDate;

}
