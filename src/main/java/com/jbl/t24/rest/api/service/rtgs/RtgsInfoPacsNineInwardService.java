package com.jbl.t24.rest.api.service.rtgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineInward;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoPacsNineInwardRepository;

@Service
@Transactional(readOnly = true)
public class RtgsInfoPacsNineInwardService {

	@Autowired
	RtgsInfoPacsNineInwardRepository rtgsInfoPacsNineInwardRepository;

	@Transactional(readOnly = false)
	public RtgsInfoPacsNineInward save(RtgsInfoPacsNineInward entity) {
		return rtgsInfoPacsNineInwardRepository.save(entity);
	}

	public RtgsInfoPacsNineInward findByUniqueInwardRtgsId(String uniqueInwardRtgsId) {
		return rtgsInfoPacsNineInwardRepository.findByUniqueInwardRtgsId(uniqueInwardRtgsId);
	}

}
