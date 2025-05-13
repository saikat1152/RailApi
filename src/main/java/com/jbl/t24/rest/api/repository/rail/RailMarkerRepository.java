package com.jbl.t24.rest.api.repository.rail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.rail.RailMarkerAccount;

@Repository
public interface RailMarkerRepository extends JpaRepository<RailMarkerAccount, Integer> {

    RailMarkerAccount findByAccountToMark(String accountToMark);

    RailMarkerAccount findByRailMarkerId(int railMarkerId);

}
