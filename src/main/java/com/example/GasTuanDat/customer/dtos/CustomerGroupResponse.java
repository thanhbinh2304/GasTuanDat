package com.example.GasTuanDat.customer.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGroupResponse {
    private UUID customerGroupId;
    private String groupName;
}
