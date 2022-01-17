package com.mall.user.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 *  ciggar
 * create-date: 2019/7/31-19:15
 */
@Data
public class AddressListResponse extends AbstractResponse {

    private List<AddressDto> addressDtos;
}
