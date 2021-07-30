package com.ventus.core.models;

import com.ventus.core.interfaces.IProfile;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Profile implements IProfile {
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
}
