package com.mall.user.converter;

import com.mall.user.dal.entitys.Address;
import com.mall.user.dto.AddAddressRequest;
import com.mall.user.dto.AddressDto;
import com.mall.user.dto.UpdateAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

/**
 *  ciggar
 * create-date: 2019/7/31-19:33
 */
@Mapper(componentModel = "spring")
public interface AddressConverter {

    @Mappings({})
    AddressDto address2List(Address addresses);

    /*@Mappings({})
    AddressDto address2Res(Address address);*/

    List<AddressDto> address2List(List<Address> addresses);

    @Mappings({})
    Address req2Address(AddAddressRequest request);

    @Mappings({})
    Address req2Address(UpdateAddressRequest request);
}
