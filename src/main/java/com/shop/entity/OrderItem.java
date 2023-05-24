package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id") //주문상품
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;  //order_item_id 에 item_id를 다대일 매핑  (하나의 상품은 여러개의 주문상품에 들어갈수있음)

    @ManyToOne
    @JoinColumn(name= "order_id")
    private Order order; //order_item_id 에 order_id를 다대일 매핑 (한번의 주문에 여러개의 상품을 주문할수있음)

    private int orderPrice;

    private int count; //수량

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
