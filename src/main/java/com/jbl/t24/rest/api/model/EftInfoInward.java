package com.jbl.t24.rest.api.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class EftInfoInward extends CommonFtInfo {
    private int InwardEftInfoId;
    private String uniqueInwardEftId;
    private String ftCategory;
    
}
