package com.example.orderservice.service;

import brave.Span;
import brave.Tracer;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;




@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItems()
                .stream()
                .map(orderLineItems -> OrderLineItems
                        .builder()
                        .price(orderLineItems.getPrice())
                        .skuCode(orderLineItems.getSkuCode())
                        .quantity(orderLineItems.getQuantity())
                        .build()
                ).toList();

        order.setOrderLineItems(orderLineItemsList);


        List<String> skuCodes = orderLineItemsList
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        Span inventoryServiceLookUp = tracer.nextSpan().name("InventoryServiceLookUp");

        try(Tracer.SpanInScope spanInScope = tracer.withSpanInScope(inventoryServiceLookUp)){

            InventoryResponse[] inventoryResponseArray = webClientBuilder
                    .build()
                    .get()
                    .uri("http://INVENTORY-SERVICE/api/inventory",
                            uriBuilder -> uriBuilder
                                    .queryParam("skuCodes", skuCodes)
                                    .build()
                    )
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            log.info(Arrays.toString(inventoryResponseArray));
            log.info(skuCodes.toString());

            boolean result = false;
            if(inventoryResponseArray == null || inventoryResponseArray.length == 0) result = false;
            else result = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

            log.info("result {}", result);

            if(result) {
                orderRepository.save(order);
            }else {
                throw new IllegalStateException("Product is not in stock, please try again");
            }
        }finally {
            inventoryServiceLookUp.finish();
        }



    }
}
