package fact.it.userservice.service;

import fact.it.userservice.dto.*;
import fact.it.userservice.model.Order;
import fact.it.userservice.model.UserLineItem;
import fact.it.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final WebClient webClient;

    public boolean placeOrder(UserRequest userRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<UserLineItem> userLineItems = userRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToOrderLineItem)
                .toList();

        order.setUserLineItemsList(userLineItems);

        List<String> skuCodes = order.getUserLineItemsList().stream()
                .map(UserLineItem::getSkuCode)
                .toList();

        WorkoutResponse[] workoutResponseArray = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(WorkoutResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(workoutResponseArray)
                .allMatch(WorkoutResponse::isInStock);

        if(allProductsInStock){
            RecordResponse[] recordResponseArray = webClient.get()
                    .uri("http://localhost:8080/api/product",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(RecordResponse[].class)
                    .block();

            order.getUserLineItemsList().stream()
                    .map(orderItem -> {
                        RecordResponse product = Arrays.stream(recordResponseArray)
                                .filter(p -> p.getSkuCode().equals(orderItem.getSkuCode()))
                                .findFirst()
                                .orElse(null);
                        if (product != null) {
                            orderItem.setPrice(product.getPrice());
                        }
                        return orderItem;
                    })
                    .collect(Collectors.toList());

            userRepository.save(order);
            return true;
        } else {
            return false;
        }
    }

    public List<UserResponse> getAllOrders() {
        List<Order> orders = userRepository.findAll();

        return orders.stream()
                .map(order -> new UserResponse(
                        order.getOrderNumber(),
                        mapToOrderLineItemsDto(order.getUserLineItemsList())
                ))
                .collect(Collectors.toList());
    }

    private UserLineItem mapToOrderLineItem(UserLineItemDto userLineItemDto) {
        UserLineItem userLineItem = new UserLineItem();
        userLineItem.setPrice(userLineItemDto.getPrice());
        userLineItem.setQuantity(userLineItemDto.getQuantity());
        userLineItem.setSkuCode(userLineItemDto.getSkuCode());
        return userLineItem;
    }

    private List<UserLineItemDto> mapToOrderLineItemsDto(List<UserLineItem> userLineItems) {
        return userLineItems.stream()
                .map(userLineItem -> new UserLineItemDto(
                        userLineItem.getId(),
                        userLineItem.getSkuCode(),
                        userLineItem.getPrice(),
                        userLineItem.getQuantity()
                ))
                .collect(Collectors.toList());
    }


}
