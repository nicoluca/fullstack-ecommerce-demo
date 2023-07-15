package org.nico.ecommerce.dto;

import lombok.Data;
import org.nico.ecommerce.entitiy.Address;
import org.nico.ecommerce.entitiy.Customer;
import org.nico.ecommerce.entitiy.Order;
import org.nico.ecommerce.entitiy.OrderItem;

import java.util.Set;

@Data
public class Purchase {
    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
