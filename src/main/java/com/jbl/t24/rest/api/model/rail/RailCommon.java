package com.jbl.t24.rest.api.model.rail;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Audited
@SuperBuilder
public class RailCommon {

    @Column(name = "CO_CODE", length = 10)
	@NotNull(message = "Branch Code Missing")
	@Pattern(regexp = "^[0-9]{4}$", message = "Branch Code Pattern Not Valid", flags = Flag.CASE_INSENSITIVE)
	protected String coCode;

	@Column(name = "company_code", length = 10)
	protected String companyCode = "BD001";

	@Column(name = "ip", length = 63)
	protected String ip;

	@Column(name = "hostname", length = 127)
	protected String hostname;

	@Column(name = "status", length = 2)
	protected int status;

	@Column(name = "ofs_request", length = 1048)
	protected String ofsRequest;

	@Column(name = "ofs_response", length = 4000)
	protected String ofsResponse;

	@Column(name = "ft_response_str", length = 512)
	protected String ftResponseStr;

}
