package com.mall.user.converter;

import com.mall.user.dal.entitys.Member;
import com.mall.user.dto.UserLoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 *  ciggar
 * create-date: 2019/7/22-18:05
 */
@Mapper
public interface UserConverterMapper {

    UserConverterMapper INSTANCE= Mappers.getMapper(UserConverterMapper.class);

    @Mappings({})
    UserLoginResponse converter(Member member);

}
