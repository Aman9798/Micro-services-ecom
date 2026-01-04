package com.example.user.Mapper;

import com.example.user.DTO.AddressRequestDTO;
import com.example.user.DTO.AddressResponseDTO;
import com.example.user.Entity.Address;

public class AddressMapper {

    public static Address convertToAddress(AddressRequestDTO addressRequest) {
        return Address.builder()
                .street(addressRequest.getStreet())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .zipCode(addressRequest.getZipCode())
                .build();
    }
    public static AddressResponseDTO convertToAddressResponseDTO(Address address) {
        return AddressResponseDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .build();
    }

}
