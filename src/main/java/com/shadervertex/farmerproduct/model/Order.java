package com.shadervertex.farmerproduct.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.shadervertex.farmerproduct.dto.OrderDto;
import com.shadervertex.farmerproduct.enums.OrderStatus;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderDescription;
    private Date date;
    private Long amount;
    private String address;
    private String payment;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Long totalAmount;
    private Long discount;
    private UUID trackingId;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @JsonManagedReference
    private List<CartItems> cartItems;
    private Double latitude;
    private Double longitude;

    public OrderDto getOrderDto() {
        return OrderDto.builder()
                .id(id)
                .orderDescription(orderDescription)
                .date(date)
                .amount(amount)
                .address(address)
                .totalAmount(totalAmount)
                .discount(discount)
                .payment(payment)
                .orderStatus(orderStatus)
                .trackingId(trackingId)
                .userName(user != null ? user.getName() : null)
                .couponName(coupon != null ? coupon.getName() : null)
                .build();
    }
}