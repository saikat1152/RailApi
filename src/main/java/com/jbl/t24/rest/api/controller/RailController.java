package com.jbl.t24.rest.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbl.t24.rest.api.config.HostIpHandle;
import com.jbl.t24.rest.api.constant.RailOfsSources;
import com.jbl.t24.rest.api.model.rail.RailLockAccount;
import com.jbl.t24.rest.api.model.rail.RailMarkerAccount;
import com.jbl.t24.rest.api.model.rail.RailUnlockAccount;
import com.jbl.t24.rest.api.service.rail.RailMarkerHandler;
import com.jbl.t24.rest.api.service.rail.RailServiceHandler;

@RestController
@CrossOrigin
@RequestMapping("/account")
@Validated
public class RailController {

    Logger logger = LogManager.getLogger(RailController.class);

    @Autowired
    RailServiceHandler railService;

	@Autowired
    RailMarkerHandler railMarkerService;

    @PostMapping(value = {"/lock"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> railLockAction(@Valid @RequestBody RailLockAccount railLockinfo,
			HttpServletRequest httpServletRequest) throws Exception {


		String uniqueFtId = railLockinfo.getAtUniqueId();
		String coCode = railLockinfo.getCoCode();
		String companyCode = railLockinfo.getCompanyCode() + coCode;
		railLockinfo.setCompanyCode(companyCode);
		String LockAccNumber = railLockinfo.getAccountToLock().trim();
		String LockAmount = railLockinfo.getLockAmount().trim();
		String LockDetails = railLockinfo.getLockDetails();


		String lockStartDate = railLockinfo.getLockStartDate().toString().replace("-", "");
		String lockEndDate = railLockinfo.getLockEndDate().toString().replace("-", "");;

		final String requestOFS;

	    requestOFS = String.format(RailOfsSources.LOCK_OFS_STRING,companyCode,LockAccNumber, LockDetails,lockStartDate,lockEndDate,LockAmount,uniqueFtId);
	
		// requestOFS  = RailOfsSources.UNLOCK_OFS_STRING;

		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		railLockinfo.setHostname(hostIpData.get("host"));
		railLockinfo.setIp(hostIpData.get("remoteAddr"));

		ResponseEntity<?> response = null;
	
		try {
			response = railService.handleLock(requestOFS, railLockinfo);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}

				return response; 
			}


    @PostMapping(value = {"/unlock"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> railUnLockAction(@Valid @RequestBody RailUnlockAccount railUnLockinfo,
			HttpServletRequest httpServletRequest) throws Exception {


		String uniqueFtId = railUnLockinfo.getAtUniqueId();
		String coCode = railUnLockinfo.getCoCode();
		String companyCode = railUnLockinfo.getCompanyCode() + coCode;
		railUnLockinfo.setCompanyCode(companyCode);
		String lockRefId= railUnLockinfo.getLockRefId().trim();
		
	

		final String requestOFS;

	    requestOFS = String.format(RailOfsSources.UNLOCK_OFS_STRING,companyCode,lockRefId,uniqueFtId);
	
		// requestOFS  = RailOfsSources.UNLOCK_OFS_STRING;

		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		railUnLockinfo.setHostname(hostIpData.get("host"));
		railUnLockinfo.setIp(hostIpData.get("remoteAddr"));

		ResponseEntity<?> response = null;
	
		try {
			 response = railService.handleUnLock(requestOFS, railUnLockinfo);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}

				return response; 
	}


	@PostMapping(value = {"/marker"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> railMarkerAction(@Valid @RequestBody RailMarkerAccount railMarkerinfo,
			HttpServletRequest httpServletRequest) throws Exception {


		String markAccNumber = railMarkerinfo.getAccountToMark().trim();
		String coCode = railMarkerinfo.getCoCode();
		String companyCode = railMarkerinfo.getCompanyCode() + coCode;
		railMarkerinfo.setCompanyCode(companyCode);
		String markerType= railMarkerinfo.getMarkerType().trim();
		


		final String requestOFS;

	    requestOFS = String.format(RailOfsSources.MARKER_FT_STRING,companyCode,markAccNumber,markerType,companyCode);
	
		// requestOFS  = RailOfsSources.UNLOCK_OFS_STRING;

		Map<String, String> hostIpData = HostIpHandle.hostIp(httpServletRequest);
		railMarkerinfo.setHostname(hostIpData.get("host"));
		railMarkerinfo.setIp(hostIpData.get("remoteAddr"));

		ResponseEntity<?> response = null;
	
		try {
			response = railMarkerService.handleMarker(requestOFS, railMarkerinfo);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}

				return response; 
	}


}
