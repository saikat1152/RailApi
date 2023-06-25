package com.jbl.t24.rest.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbl.t24.rest.api.model.EftInfoOutward;
import com.jbl.t24.rest.api.model.RtgsInfoOutward;
import com.jbl.t24.rest.api.repository.RtgsInfoOutwardRepository;


@Service
@Transactional(readOnly = true)
public class RtgsInfoOutwardService {

	  @Autowired
	  RtgsInfoOutwardRepository rtgsInfoOutwardRepository;

	  @Transactional(readOnly = false)
	  public RtgsInfoOutward save(RtgsInfoOutward entity) {
	    return rtgsInfoOutwardRepository.save(entity);
	  }

	  public RtgsInfoOutward findByUniqueOutwardRtgsId(String uniqueOutwardRtgsId){
	    return rtgsInfoOutwardRepository.findByUniqueOutwardRtgsId(uniqueOutwardRtgsId);
	  }

	  public RtgsInfoOutward findByCbsFtno(String cbsFtno){
		    return rtgsInfoOutwardRepository.findByCbsFtno(cbsFtno);
		  }

	}