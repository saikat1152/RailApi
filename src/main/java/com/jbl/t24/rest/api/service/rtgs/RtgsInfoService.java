package com.jbl.t24.rest.api.service.rtgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.model.rtgs.CommonFtInfo;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoInward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineInward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.model.rtgs.SettlementInInfo;
import com.jbl.t24.rest.api.model.rtgs.SettlementOutInfo;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoInwardRepository;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoOutwardRepository;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoPacsNineInwardRepository;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoPacsNineOutwardRepository;
import com.jbl.t24.rest.api.repository.rtgs.SettlementInInfoRepositoryEFT;
import com.jbl.t24.rest.api.repository.rtgs.SettlementOutInfoRepositoryEFT;

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

	@Autowired
	private SettlementInInfoRepositoryEFT settlementInInfoRepositoryEFT;

	@Autowired
	private SettlementOutInfoRepositoryEFT settlementOutInfoRepositoryEFT;

	public static final String RTGS_PACS08_REVERSE_TRANSACTION="1";

	public CommonFtInfo findByUniqueId(CommonFtInfo ftInfo, String uniqueId) {
		if (ftInfo instanceof RtgsInfoInward) {
			return infoInwardRepository.findByUniqueInwardRtgsId(uniqueId);

		} else if (ftInfo instanceof RtgsInfoOutward) {
			return infoOutwardRepository.findByUniqueOutwardRtgsId(uniqueId);

		} else if (ftInfo instanceof RtgsInfoPacsNineInward) {
			return infoPacsNineInwardRepository.findByUniqueInwardRtgsId(uniqueId);

		} else if (ftInfo instanceof RtgsInfoPacsNineOutward) {
			return infoPacsNineOutwardRepository.findByUniqueOutwardRtgsId(uniqueId);
		} else if (ftInfo instanceof SettlementInInfo) {
			return settlementInInfoRepositoryEFT.findByUniqueSettlementtId(uniqueId);
		}
		// (ftInfo instanceof SettlementOutInfo)
		else {
			return settlementOutInfoRepositoryEFT.findByUniqueSettlementtId(uniqueId);
		}

	}

	public void save(CommonFtInfo entity) {

		if (entity instanceof RtgsInfoInward) {
			infoInwardRepository.save((RtgsInfoInward) entity);

		} else if (entity instanceof RtgsInfoOutward) {
			infoOutwardRepository.save((RtgsInfoOutward) entity);

		} else if (entity instanceof RtgsInfoPacsNineInward) {
			infoPacsNineInwardRepository.save((RtgsInfoPacsNineInward) entity);

		} else if (entity instanceof RtgsInfoPacsNineOutward) {
			infoPacsNineOutwardRepository.save((RtgsInfoPacsNineOutward) entity);
		} else if (entity instanceof SettlementInInfo) {
			settlementInInfoRepositoryEFT.save((SettlementInInfo) entity);
		} else {
			settlementOutInfoRepositoryEFT.save((SettlementOutInfo) entity);
		}
	}

	public CommonFtInfo findByCbsFtNo(String flag, String cbsFtNo) {
		if (flag.equals(RTGS_PACS08_REVERSE_TRANSACTION)) {
			return infoOutwardRepository.findByCbsFtno(cbsFtNo);
		} else {
			return infoPacsNineOutwardRepository.findByCbsFtno(cbsFtNo);
		}

	}
}
