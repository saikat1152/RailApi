package com.jbl.t24.rest.api.model.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="endpoint_access")
@AllArgsConstructor
@NoArgsConstructor
@Audited
public class EndpointAccess {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id", unique = true, nullable = false)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name="endpoint")
    private String endpoint;
    @Column(name="allowed")
    private Boolean allowed;
}