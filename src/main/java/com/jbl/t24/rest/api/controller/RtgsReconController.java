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

        // if status is pending, not receive anything

        RtgsGroupReconcillation groupReconcillationEntity = DtoToModelMapper
                .fromReconGroupDtoToReconModel(groupReconcileDto);

        List<IndividualReconcileDto> individualAccountDtos = groupReconcileDto.individualReconcileDtos();

        List<RtgsReconIndividual> individualEntities = individualAccountDtos.stream()
                .map(e -> DtoToModelMapper.fromIndiDtoToIndivModel(e, groupReconcillationEntity)).toList();

        groupReconcillationEntity.setIndividualReconList(individualEntities);

        // Saving GroupRecon as Pending
        groupReconService.saveGroupRecon(groupReconcillationEntity);

        List<ResponseEntity<?>> responses = new ArrayList<>();

        // for (int i = 0; i < 2; i++) {
        //     responses.add(service.handleReconTransaction(individualEntities.get(i), httpServletRequest));
        // }

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
            update.put(uniqueId, status+"");
            txStatusList.add(update);
            allTransactionStatus.add(status);
        }

        if (allTransactionStatus.get(0) == 2 &&
        allTransactionStatus.get(1) == 2 &&
        allTransactionStatus.get(2) == 2) {
            groupReconcillationEntity.setStatus(2);

        } else {
            groupReconcillationEntity.setStatus(3);
        }

        groupReconService.saveGroupRecon(groupReconcillationEntity);

        GroupReconResponse groupReconResponse = new GroupReconResponse(
            groupReconcileDto.groupReconUniqueId(), 
            txStatusList, 
            groupReconcillationEntity.getStatus()+"");

        return ResponseEntity.status(HttpStatus.OK).body(groupReconResponse);

    }

}
