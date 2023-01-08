package com.jbl.t24.rest.api.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

// import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class EftInfoOutward extends CommonFtInfo{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eft_out_id", unique = true, nullable = false)
    private int OutwardEftInfoId;

    @NotBlank(message = "Narrative is Blank")
	@Pattern(regexp = "^(BEFT)[0-9]{13}$", message = "EFT Unique ID Length Not Valid")
	@Column(name = "out_ft_Id", unique = true, nullable = false, length = 23)
    private String uniqueOutwardEftId;

    @Column(name="transaction_category")
    private String txType = "ACOD";
    
}
