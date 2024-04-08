package com.jbl.t24.rest.api.service.rtgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbl.t24.rest.api.model.rtgs.CommonFtInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementInInfo;
import com.jbl.t24.rest.api.model.rtgs.RTGSSettlementOutInfo;
import com.jbl.t24.rest.api.model.rtgs.RtgsCommon;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoInward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineInward;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoPacsNineOutward;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoInwardRepository;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoOutwardRepository;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoPacsNineInwardRepository;
import com.jbl.t24.rest.api.repository.rtgs.RtgsInfoPacsNineOutwardRepository;
import com.jbl.t24.rest.api.repository.rtgs.RTGSSettlementInInfoRepository;
import com.jbl.t24.rest.api.repository.rtgs.RTGSSettlementOutInfoRepository;

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
	private RTGSSettlementInInfoRepository settlementInInfoRepository;

	@Autowired
	private RTGSSettlementOutInfoRepository settlementOutInfoRepository;

	public static final String RTGS_PACS08_REVERSE_TRANSACTION = "1";

	// public CommonFtInfo findByUniqueId(CommonFtInfo ftInfo, String uniqueId) {
	// 	if (ftInfo instanceof RtgsInfoInward) {
	// 		return infoInwardRepository.findByUniqueInwardRtgsId(uniqueId);

	// 	} else if (ftInfo instanceof RtgsInfoOutward) {
	// 		return infoOutwardRepository.findByUniqueOutwardRtgsId(uniqueId);

	// 	} else if (ftInfo instanceof RtgsInfoPacsNineInward) {
	// 		return infoPacsNineInwardRepository.findByUniqueInwardRtgsId(uniqueId);

	// 	} else if (ftInfo instanceof RtgsInfoPacsNineOutward) {
	// 		return infoPacsNineOutwardRepository.findByUniqueOutwardRtgsId(uniqueId);
	// 	} else if (ftInfo instanceof RTGSSettlementInInfo) {
	// 		return settlementInInfoRepository.findByUniqueSettlementtId(uniqueId);
	// 	}
	// 	// (ftInfo instanceof SettlementOutInfo)
	// 	else {
	// 		return settlementOutInfoRepository.findByUniqueSettlementtId(uniqueId);
	// 	}

	// }

	public RtgsCommon findByUniqueId(RtgsCommon ftInfo, String uniqueId) {
		if (ftInfo instanceof RtgsInfoInward) {
			return infoInwardRepository.findByUniqueInwardRtgsId(uniqueId);

		} else if (ftInfo instanceof RtgsInfoOutward) {
			return infoOutwardRepository.findByUniqueOutwardRtgsId(uniqueId);

		} else if (ftInfo instanceof RtgsInfoPacsNineInward) {
			return infoPacsNineInwardRepository.findByUniqueInwardRtgsId(uniqueId);

		} else if (ftInfo instanceof RtgsInfoPacsNineOutward) {
			return infoPacsNineOutwardRepository.findByUniqueOutwardRtgsId(uniqueId);
		} else if (ftInfo instanceof RTGSSettlementInInfo) {
			return settlementInInfoRepository.findByUniqueSettlementtId(uniqueId);
		}
		// (ftInfo instanceof SettlementOutInfo)
		else {
			return settlementOutInfoRepository.findByUniqueSettlementtId(uniqueId);
		}

	}

	// public void save(CommonFtInfo entity) {

	// 	if (entity instanceof RtgsInfoInward) {
	// 		infoInwardRepository.save((RtgsInfoInward) entity);

	// 	} else if (entity instanceof RtgsInfoOutward) {
	// 		infoOutwardRepository.save((RtgsInfoOutward) entity);

	// 	} else if (entity instanceof RtgsInfoPacsNineInward) {
	// 		infoPacsNineInwardRepository.save((RtgsInfoPacsNineInward) entity);

	// 	} else if (entity instanceof RtgsInfoPacsNineOutward) {
	// 		infoPacsNineOutwardRepository.save((RtgsInfoPacsNineOutward) entity);
	// 	} else if (entity instanceof RTGSSettlementInInfo) {
	// 		settlementInInfoRepository.save((RTGSSettlementInInfo) entity);
	// 	} else {
	// 		settlementOutInfoRepository.save((RTGSSettlementOutInfo) entity);
	// 	}
	// }

	public void save(RtgsCommon entity) {

		if (entity instanceof RtgsInfoInward) {
			infoInwardRepository.save((RtgsInfoInward) entity);

		} else if (entity instanceof RtgsInfoOutward) {
			infoOutwardRepository.save((RtgsInfoOutward) entity);

		} else if (entity instanceof RtgsInfoPacsNineInward) {
			infoPacsNineInwardRepository.save((RtgsInfoPacsNineInward) entity);

		} else if (entity instanceof RtgsInfoPacsNineOutward) {
			infoPacsNineOutwardRepository.save((RtgsInfoPacsNineOutward) entity);
		} else if (entity instanceof RTGSSettlementInInfo) {
			settlementInInfoRepository.save((RTGSSettlementInInfo) entity);
		} else {
			settlementOutInfoRepository.save((RTGSSettlementOutInfo) entity);
		}
	}

	// public CommonFtInfo findByCbsFtNo(String flag, String cbsFtNo) {
	// 	if (flag.equals(RTGS_PACS08_REVERSE_TRANSACTION)) {
	// 		return infoOutwardRepository.findByCbsFtno(cbsFtNo);
	// 	} else {
	// 		return infoPacsNineOutwardRepository.findByCbsFtno(cbsFtNo);
	// 	}

	// }

	public RtgsCommon findByCbsFtNo(String flag, String cbsFtNo) {
		if (flag.equals(RTGS_PACS08_REVERSE_TRANSACTION)) {
			return infoOutwardRepository.findByCbsFtno(cbsFtNo);
		} else {
			return infoPacsNineOutwardRepository.findByCbsFtno(cbsFtNo);
		}

	}
}
