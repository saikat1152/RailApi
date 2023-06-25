package com.jbl.t24.rest.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.model.CommonFtInfo;
import com.jbl.t24.rest.api.model.RtgsInfoInward;
import com.jbl.t24.rest.api.model.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.RtgsInfoPacsNineInward;
import com.jbl.t24.rest.api.model.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.repository.RtgsInfoInwardRepository;
import com.jbl.t24.rest.api.repository.RtgsInfoOutwardRepository;
import com.jbl.t24.rest.api.repository.RtgsInfoPacsNineInwardRepository;
import com.jbl.t24.rest.api.repository.RtgsInfoPacsNineOutwardRepository;

@Service
public class RtgsInfoService {

	@Autowired
	private RtgsInfoInwardRepository infoInwardRepository;
	@Autowired
	private RtgsInfoOutwardRepository infoOutwardRepository;
	@Autowired
	private RtgsInfoPacsNineInwardRepository infoPacsNineInwardRepository;
	@Autowired
	private RtgsInfoPacsNineOutwardRepository infoPacsNineOutwardRepository;

	public CommonFtInfo findByUniqueId(CommonFtInfo ftInfo, String uniqueId) {
		if (ftInfo instanceof RtgsInfoInward) {
			return infoInwardRepository.findByUniqueInwardRtgsId(uniqueId);

		} else if (ftInfo instanceof RtgsInfoOutward) {
			return infoOutwardRepository.findByUniqueOutwardRtgsId(uniqueId);

		} else if (ftInfo instanceof RtgsInfoPacsNineInward) {
			return infoPacsNineInwardRepository.findByUniqueInwardRtgsId(uniqueId);

		} else {
			return infoPacsNineOutwardRepository.findByUniqueOutwardRtgsId(uniqueId);
		}

	}

	public void save(CommonFtInfo entity) {

		if (entity instanceof RtgsInfoInward) {
			infoInwardRepository.save((RtgsInfoInward) entity);

		} else if (entity instanceof RtgsInfoOutward) {
			infoOutwardRepository.save((RtgsInfoOutward) entity);

		} else if (entity instanceof RtgsInfoPacsNineInward) {
			infoPacsNineInwardRepository.save((RtgsInfoPacsNineInward) entity);

		} else {
			infoPacsNineOutwardRepository.save((RtgsInfoPacsNineOutward) entity);
		}
	}

	public CommonFtInfo findByCbsFtNo(String flag, String cbsFtNo) {
		if (flag.equals("1")) {
			return infoOutwardRepository.findByCbsFtno(cbsFtNo);
		} else {
			return infoPacsNineOutwardRepository.findByCbsFtno(cbsFtNo);
		}

	}
}
