package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.rtgs.RtgsReconIndividual;

@Repository
public interface RtgsReconRepository extends JpaRepository<RtgsReconIndividual, Long> {

	RtgsReconIndividual findByInidvidualReconUniqueId(String inidvidualReconUniqueId);

}
