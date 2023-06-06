package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto { //주문정보

    private Long orderId;

    private String orderDate;

    private OrderStatus orderStatus;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public OrderHistDto(Order order){  //order객체를 파라미터로 받아 멤버변수값 세팅, 주문날짜 포맷 아래와같이 설정
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();

    }

    public void addOrderItemDto(OrderItemDto orderItemDto){  //orderItemDto 객체를 주문상품 리스트에 추가
        orderItemDtoList.add(orderItemDto);
    }

}
