package com.jbl.t24.rest.api.service.rail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbl.t24.rest.api.model.rail.RailLockAccount;
import com.jbl.t24.rest.api.model.rail.RailMarkerAccount;
import com.jbl.t24.rest.api.model.rail.RailUnlockAccount;
import com.jbl.t24.rest.api.repository.rail.RailLockRepository;
import com.jbl.t24.rest.api.repository.rail.RailMarkerRepository;
import com.jbl.t24.rest.api.repository.rail.RailUnlockRepository;

@Service
@Transactional
public class RailInfoService {

    Logger logger = LogManager.getLogger(RailInfoService.class);

    @Autowired
    RailMarkerRepository railMarkerRepository;

    @Autowired
    RailLockRepository railLockRepository;

    @Autowired
    RailUnlockRepository railUnlockRepository;

    public RailMarkerAccount saveRailMarkerAccount(RailMarkerAccount railMarkerAccount) {
        return railMarkerRepository.save(railMarkerAccount);
    }

    public RailLockAccount saveRailLockAccount(RailLockAccount railLockAccount) {
        return railLockRepository.save(railLockAccount);
    }

    public RailUnlockAccount saveRailUnlockAccount(RailUnlockAccount railUnlockAccount) {
        return railUnlockRepository.save(railUnlockAccount);
    }
}
