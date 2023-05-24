package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne  //Order(Many)ToOne(member_id)  한명의 멤버는 여러개의 주문을 할수있음
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //연관관계의 주인의 필드인 order 를 설정 (OrderItem 에 존재), 영속성상태변화 자식엔티티에 모두전이:ALL
    private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러개의 주문상품을 가짐(OrderItem 쪽이 다 이기때문에 list 로 매핑)

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
