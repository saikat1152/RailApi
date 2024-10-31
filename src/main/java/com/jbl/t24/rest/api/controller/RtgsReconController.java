package com.jbl.t24.rest.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbl.t24.rest.api.model.reconcileDtos.GroupReconcileDto;
import com.jbl.t24.rest.api.model.reconcileDtos.IndividualReconcileDto;
import com.jbl.t24.rest.api.model.rtgs.RtgsInfoOutward;
import com.jbl.t24.rest.api.model.rtgs.RtgsReconIndividual;
import com.jbl.t24.rest.api.service.rtgs.ReconTransactionService;
import com.jbl.t24.rest.api.utils.DtoToModelMapper;

import io.micrometer.core.ipc.http.HttpSender.Response;

import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;;

@RestController
@CrossOrigin
@RequestMapping("/rtgs")
@Validated
public class RtgsReconController {

    @Autowired
    ReconTransactionService service;

    @PostMapping(value = "/recon", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ResponseEntity<?>> performReconcilation(@Valid @RequestBody GroupReconcileDto groupReconcileDto,
            HttpServletRequest httpServletRequest) throws Exception {

        RtgsReconIndividual individual = DtoToModelMapper.fromIndiDtoToIndivModel(
                groupReconcileDto.individualReconcileDtos().get(0),
                groupReconcileDto.coCode());

        List<IndividualReconcileDto> individualAccountDtos = groupReconcileDto.individualReconcileDtos();
        List<RtgsReconIndividual> individualEntities = individualAccountDtos.stream()
                .map(e -> DtoToModelMapper.fromIndiDtoToIndivModel(e, groupReconcileDto.coCode()))
                .toList();

        List<ResponseEntity<?>> responses = new ArrayList<>();

        for (RtgsReconIndividual rtgsReconIndividual : individualEntities) {
            responses.add(service.handleReconTransaction(rtgsReconIndividual, httpServletRequest));
        }

        // ResponseEntity<?> response = service.handleReconTransaction(individual, httpServletRequest);

        // return ResponseEntity.status(HttpStatus.OK).body(response);
        return responses;

    }

}
