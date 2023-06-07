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
public class Order extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //Order(Many)ToOne(member_id)  한명의 멤버는 여러개의 주문을 할수있음
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태
                                                              //보통 반대쪽 매핑의 필드이름을 mappedBy의 값으로 주면됨
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) //연관관계의 주인의 필드인 order 를 설정 (OrderItem 에 존재), 영속성상태변화 자식엔티티에 모두전이:ALL
    private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러개의 주문상품을 가짐(OrderItem 쪽이 다 이기때문에 list 로 매핑)
                                                            //주문할 때 주문엔티티를 저장하며 주문상품 엔티티도 함께 저장
                                                            //orphanRemoval=true: 고아객체(엔티티 연관관계 끊어진객체) 제거를 위한 속성
                                                            //FetchType.LAZY: 지연로딩(실제 조회시에 쿼리문 작동)

    public void addOrderItem(OrderItem orderItem){ //주문상품 정보담기
        orderItems.add(orderItem);
        orderItem.setOrder(this);  //Order와 OrderItem은 양방향 참조, orderItem에도 order 세팅 필요
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);  //상품주문한 회원 정보 set
        for(OrderItem orderItem : orderItemList){   //상품 페이지 = 1개주문 , 장바구니 = 여러개주문, 주문상품 여러개받을수있도록 리스트형태로 파라미터값을받아 주문객체에 orderItem 객체를 추가
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem: orderItems){
            orderItem.cancel();
        }
    }

}
