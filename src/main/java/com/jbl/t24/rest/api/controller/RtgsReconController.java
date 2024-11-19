package com.jbl.t24.rest.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jbl.t24.rest.api.common.model.FtTxResponse;
import com.jbl.t24.rest.api.common.model.JwtErrorResponse;
import com.jbl.t24.rest.api.config.Mapper;
import com.jbl.t24.rest.api.enums.FtStatus;
import com.jbl.t24.rest.api.enums.ResponseStatus;
import com.jbl.t24.rest.api.model.reconcileDtos.GroupReconResponse;
import com.jbl.t24.rest.api.model.reconcileDtos.GroupReconcileDto;
import com.jbl.t24.rest.api.model.reconcileDtos.IndividualReconcileDto;
import com.jbl.t24.rest.api.model.rtgs.RtgsGroupReconcillation;
import com.jbl.t24.rest.api.model.rtgs.RtgsReconIndividual;
import com.jbl.t24.rest.api.service.rtgs.ReconTransactionService;
import com.jbl.t24.rest.api.service.rtgs.RtgsGroupReconService;
import com.jbl.t24.rest.api.utils.DtoToModelMapper;
import com.jbl.t24.rest.api.utils.TransactionStatusFetcher;

import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;;

@RestController
@CrossOrigin
@RequestMapping("/rtgs")
@Validated
public class RtgsReconController {

    @Autowired
    ReconTransactionService service;

    @Autowired
    RtgsGroupReconService groupReconService;

    @Autowired
    TransactionStatusFetcher statusFetcher;

    @PostMapping(value = "/recon", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> performReconcilation(@Valid @RequestBody GroupReconcileDto groupReconcileDto,
            HttpServletRequest httpServletRequest) throws Exception {

    
        RtgsGroupReconcillation groupReconcillationExists = groupReconService
                .GroupReconUniqueId(groupReconcileDto.groupReconUniqueId());

        RtgsGroupReconcillation groupReconcillationEntity;
        List<RtgsReconIndividual> individualEntities;

        if (groupReconcillationExists != null) {
            if (groupReconcillationExists.getStatus() == FtStatus.PENDING.getValue()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new JwtErrorResponse(HttpStatus.CONFLICT, ResponseStatus.FIVEZ27.getText(),
                                ResponseStatus.FIVEZ27.getValue()));
            } else if (groupReconcillationExists.getStatus() == FtStatus.SUCCESS.getValue()) {
                GroupReconResponse groupReconResponse = Mapper
                        .readGroupReconValue(groupReconcillationExists.getGroupResponse());
                return ResponseEntity.status(HttpStatus.OK).body(groupReconResponse);
            } else {
                groupReconcillationEntity = groupReconcillationExists;
                individualEntities = groupReconcillationEntity.getIndividualReconList();
            }

        } else {
            groupReconcillationEntity = DtoToModelMapper
                    .fromReconGroupDtoToReconModel(groupReconcileDto);
            List<IndividualReconcileDto> individualAccountDtos = groupReconcileDto.individualReconcileDtos();
            individualEntities = individualAccountDtos.stream()
                    .map(e -> DtoToModelMapper.fromIndiDtoToIndivModel(e, groupReconcillationEntity)).toList();

            groupReconcillationEntity.setIndividualReconList(individualEntities);
            groupReconService.saveGroupRecon(groupReconcillationEntity);
        }

        // RtgsGroupReconcillation groupReconcillationEntity;

        // if(!groupReconcileDto.groupReconUniqueId().isEmpty()){

        // RtgsGroupReconcillation reconGroupUniqueId =
        // groupReconService.GroupReconUniqueId(groupReconcileDto.groupReconUniqueId());
        // GroupReconResponse groupReconResponse =
        // Mapper.readGroupReconValue(reconGroupUniqueId.getGroupResponse());
        // return ResponseEntity.status(HttpStatus.OK).body(groupReconResponse);

        // }

        // List<IndividualReconcileDto> individualAccountDtos =
        // groupReconcileDto.individualReconcileDtos();

        // List<RtgsReconIndividual> individualEntities = individualAccountDtos.stream()
        // .map(e -> DtoToModelMapper.fromIndiDtoToIndivModel(e,
        // groupReconcillationEntity)).toList();

        // Saving GroupRecon as Pending
        

        List<ResponseEntity<?>> responses = new ArrayList<>();

        individualEntities.stream()
                .limit(2)
                .parallel()
                .forEach(e -> {
                    try {
                        responses.add(service.handleReconTransaction(e, httpServletRequest));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });

        for (var response : responses) {
            System.out.println(response);
        }

        FtTxResponse res = (FtTxResponse) responses.get(0).getBody();
        String localAmountBdt = res.getLocalAmountBdt();

        individualEntities.get(2).setDebitAmountStr(localAmountBdt);
        responses.add(service.handleReconTransaction(individualEntities.get(2), httpServletRequest));

        List<Integer> allTransactionStatus = new ArrayList<>();
        List<Map<String, String>> txStatusList = new ArrayList<>();

        for (RtgsReconIndividual iReconcile : individualEntities) {
            String uniqueId = iReconcile.getInidvidualReconUniqueId();
            int status = statusFetcher.getTransactionStatus(iReconcile, uniqueId);
            Map<String, String> update = new HashMap<>();
            update.put(uniqueId, status + "");
            txStatusList.add(update);
            allTransactionStatus.add(status);
            // iReconcile.setStatus(status);
        }

        if (allTransactionStatus.get(0) == 2 &&
                allTransactionStatus.get(1) == 2 &&
                allTransactionStatus.get(2) == 2) {
            groupReconcillationEntity.setStatus(2);

        } else {
            groupReconcillationEntity.setStatus(3);
        }

        GroupReconResponse groupReconResponse = new GroupReconResponse(
                groupReconcileDto.groupReconUniqueId(),
                txStatusList,
                groupReconcillationEntity.getStatus() + "");

        String groupResponse = Mapper.mapToJsonString(groupReconResponse);
        groupReconcillationEntity.setGroupResponse(groupResponse);

        groupReconService.saveGroupRecon(groupReconcillationEntity);

        return ResponseEntity.status(HttpStatus.OK).body(groupReconResponse);

    }

}
