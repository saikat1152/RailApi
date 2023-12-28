package com.jbl.t24.rest.api.common.model;

import java.sql.Timestamp;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class CommonQueryInfo {
    @CreationTimestamp
    protected Timestamp queryDate;
    protected String ip;
    protected String hostname;
    protected String status;
    protected String responseData;

}
