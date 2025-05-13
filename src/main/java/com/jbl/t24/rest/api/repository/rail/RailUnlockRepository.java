package com.jbl.t24.rest.api.repository.rail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.rail.RailUnlockAccount;

@Repository
public interface RailUnlockRepository extends JpaRepository<RailUnlockAccount, Integer> {

    RailUnlockAccount findByRailUnlockId(int railUnlockId);

    RailUnlockAccount findByAtUniqueId(String atUniqueId);

    RailUnlockAccount findByAtUniqueIdAndCompanyCode(String atUniqueId, String companyCode);

}
