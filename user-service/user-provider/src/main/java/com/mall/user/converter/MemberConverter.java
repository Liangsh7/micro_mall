package com.mall.user.converter;

import com.mall.user.dal.entitys.Member;
import com.mall.user.dto.QueryMemberResponse;
import com.mall.user.dto.UpdateMemberRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

/**
 *  ciggar
 * create-date: 2019/7/31-上午12:05
 */
@Mapper(componentModel = "spring")
public interface MemberConverter {

    @Mappings({})
    QueryMemberResponse member2Res(Member member);

    @Mappings({})
    Member updateReq2Member(UpdateMemberRequest request);
}
