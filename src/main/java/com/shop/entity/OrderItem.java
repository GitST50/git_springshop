package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "order_item_id") //주문상품
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩 방식
    @JoinColumn(name = "item_id")
    private Item item;  //order_item_id 에 item_id를 다대일 매핑  (하나의 상품은 여러개의 주문상품에 들어갈수있음)

    @ManyToOne(fetch = FetchType.LAZY)  //@ManyToOne쪽이 항상 연관관계의 주인이됨 (mappedBy 속성이 없음)
    @JoinColumn(name= "order_id")
    private Order order; //order_item_id 에 order_id를 다대일 매핑 (한번의 주문에 여러개의 상품을 주문할수있음)

    private int orderPrice; //가격

    private int count; //수량


}
