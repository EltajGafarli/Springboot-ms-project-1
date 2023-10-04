package com.example.orderservice.dto;

import com.example.orderservice.model.OrderLineItems;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderRequest {
    private List<OrderLineItems> orderLineItems;
}
