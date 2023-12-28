package com.jbl.t24.rest.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jbl.t24.rest.api.model.RtgsInfoInward;
import com.jbl.t24.rest.api.repository.RtgsInfoInwardRepository;


@Service
@Transactional(readOnly = true)
public class RtgsInfoInwardService {

	  @Autowired
	  RtgsInfoInwardRepository rtgsInfoInwardRepository;

	  @Transactional(readOnly = false)
	  public RtgsInfoInward save(RtgsInfoInward entity) {
	    return rtgsInfoInwardRepository.save(entity);
	  }

	  public RtgsInfoInward findByUniqueInwardRtgsId(String uniqueInwardRtgsId){
	    return rtgsInfoInwardRepository.findByUniqueInwardRtgsId(uniqueInwardRtgsId);
	  }

	}