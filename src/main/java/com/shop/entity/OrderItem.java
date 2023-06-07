package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

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

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);  //상품세팅
        orderItem.setCount(count);  //수량
        orderItem.setOrderPrice(item.getPrice());  //현재시간 기준 상품가격을 주문가격으로 세팅

        item.removeStock(count);  //주문수량만큼 감소
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice*count;  //주문가격 * 주문수량 = 총 가격
    }

    public void cancel(){ //주문 취소시 count만큼 상품재고 다시 더하기
        this.getItem().addStock(count);
    }


}
