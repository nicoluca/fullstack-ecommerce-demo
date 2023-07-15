package org.nico.ecommerce.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.nico.ecommerce.dto.PaymentInfo;
import org.nico.ecommerce.dto.Purchase;
import org.nico.ecommerce.dto.PurchaseResponse;
import org.nico.ecommerce.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase) {
        return checkoutService.placeOrder(purchase);
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> paymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {
        logger.warning("Payment info amount: " + paymentInfo.getAmount());

        PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfo);
        String paymentStr = paymentIntent.toJson();
        return ResponseEntity.ok(paymentStr);
    }

}
