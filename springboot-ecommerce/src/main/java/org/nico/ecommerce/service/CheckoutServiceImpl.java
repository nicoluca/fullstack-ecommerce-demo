package org.nico.ecommerce.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.nico.ecommerce.dao.CustomerRepository;
import org.nico.ecommerce.dto.PaymentInfo;
import org.nico.ecommerce.dto.Purchase;
import org.nico.ecommerce.dto.PurchaseResponse;
import org.nico.ecommerce.entitiy.Customer;
import org.nico.ecommerce.entitiy.Order;
import org.nico.ecommerce.entitiy.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               @Value("${stripe.key.secret}") String stripeSecretKey) {
        this.customerRepository = customerRepository;
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        Order order = purchase.getOrder();

        // populate order with orderItems
        Set<OrderItem> ordersItems = purchase.getOrderItems();
        ordersItems.forEach(order::add);

        // populate order with addresses
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // populate order with orderItems
        Customer customer = purchase.getCustomer();

        // check if this is an existing customer
        String email = customer.getEmail();
        Customer customerFromDB = customerRepository.findByEmail(email);
        customer = customerFromDB != null ? customerFromDB : customer;


        customerRepository.save(customer);

        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);
        customer.add(order);

        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "Luv2Shop Order");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        // generate a random UUID number (UUID version-4)
        return UUID.randomUUID().toString();
        // To absolutely guarantee uniqueness, you can query the database for the orderTrackingNumber, and if it exists, generate a new one.
    }
}
