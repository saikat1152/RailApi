package com.jbl.t24.rest.api.service.rtgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoPacsNineOutwardRepository;

@Service
@Transactional(readOnly = true)
public class RtgsInfoPacsNineOutwardService {

	@Autowired
	RtgsInfoPacsNineOutwardRepository rtgsInfoPacsNineOutwardRepository;

	@Transactional(readOnly = false)
	public RtgsInfoPacsNineOutward save(RtgsInfoPacsNineOutward entity) {
		return rtgsInfoPacsNineOutwardRepository.save(entity);
	}

	public RtgsInfoPacsNineOutward findByUniqueOutwardRtgsId(String uniqueOutwardRtgsId) {
		return rtgsInfoPacsNineOutwardRepository.findByUniqueOutwardRtgsId(uniqueOutwardRtgsId);
	}

	public RtgsInfoPacsNineOutward findByCbsFtno(String cbsFtno) {
		return rtgsInfoPacsNineOutwardRepository.findByCbsFtno(cbsFtno);
	}

}
