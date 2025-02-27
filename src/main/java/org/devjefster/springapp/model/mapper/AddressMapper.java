package org.devjefster.springapp.model.mapper;

import org.devjefster.springapp.controller.dto.CreateAddressDTO;
import org.devjefster.springapp.model.entities.Address;

public class AddressMapper {

    public static Address mapAddress(CreateAddressDTO dto) {
        return new Address(dto.street(), dto.city(), dto.zip(), dto.state(), dto.country());

    }
}
