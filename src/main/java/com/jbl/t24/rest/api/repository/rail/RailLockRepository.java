package com.jbl.t24.rest.api.repository.rail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.rail.RailLockAccount;

@Repository
public interface RailLockRepository extends JpaRepository<RailLockAccount, Integer> {

    RailLockAccount findByRailLockId(int railLockId);

    RailLockAccount findByAccountToLock(String accountToLock);

}
