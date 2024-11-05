package com.jbl.t24.rest.api.repository.rtgs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.rtgs.RtgsGroupReconcillation;



@Repository
public interface RtgsReconRepositoryGroup extends JpaRepository<RtgsGroupReconcillation, Long>,
		JpaSpecificationExecutor<RtgsGroupReconcillation> {

			RtgsGroupReconcillation findByGroupRtgsReconId(String groupRtgsReconId);

}
