package com.jbl.t24.rest.api.model.reconcileDtos;

import java.util.*;

public record GroupReconResponse(
    String groupReconUniqueId,
    List<Map<String, String>> indivResponses,
    String groupStatus
) {
    
}
