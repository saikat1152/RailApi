package com.jbl.t24.rest.api.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "request_queue")
@NamedQuery(name = "RequestQueue.findAll", query = "SELECT r FROM RequestQueue r")

public class RequestQueue implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "requet_id", unique = true, nullable = false)
	private long requetId;

	@Column(name = "ofs_request", length = 255)
	private String ofsRequest;

	@Column(name = "ofs_response", length = 1500)
	private String ofsResponse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jwt_request_id")
	private JwtUser jwtUser;

	@Column(name = "entry_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp entryAt;

}