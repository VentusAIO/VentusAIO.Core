package com.ventus.core.profile;

import com.ventus.core.network.AvailabilityFilters;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Profile {
    private String logIn;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String city;
    private String street;
    private String zipcodeIndex;
    private String address1;
    private String houseNumber;
    private String phoneNumber;
    private String apartmentNumber;

    private String card;
    private String mm;
    private String year;
    private String cvc;

    private String[] size;
    private AvailabilityFilters filter;

    private String amount;
}
