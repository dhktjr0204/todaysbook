package com.example.todaysbook.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentAddressAndMileageInfo {

    private String user;
    private String postcode;
    private String address;
    private String detailAddress;
    private long usedMileage;
    private long totalPrice;

}
