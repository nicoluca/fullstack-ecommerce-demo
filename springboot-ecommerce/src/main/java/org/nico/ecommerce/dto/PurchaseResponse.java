package org.nico.ecommerce.dto;


// Was @Data plus a POJO with orderTrackingNumber field

public record PurchaseResponse(String orderTrackingNumber) {
}
