package com.mall.user.bootstrap;

import com.alibaba.fastjson.JSON;
import com.mall.user.IMemberService;
import com.mall.user.dto.QueryMemberRequest;
import com.mall.user.dto.QueryMemberResponse;
import com.mall.user.dto.UpdateMemberRequest;
import com.mall.user.dto.UpdateMemberResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: jia.xue
 * @create: 2020-04-14 20:32
 * @Description
 **/
public class MemberServiceTest extends UserProviderApplicationTests {

    @Autowired
    private IMemberService memberService;

    @Test
    public void test01(){
        QueryMemberRequest request = new QueryMemberRequest();
        request.setUserId(66l);
        QueryMemberResponse queryMemberResponse = memberService.queryMemberById(request);
        System.out.println(JSON.toJSONString(queryMemberResponse));
        System.err.println("-----------------");
    }

    @Test
    public void test02(){
        UpdateMemberRequest request = new UpdateMemberRequest();
        request.setId(66l);
        request.setToken("xxxx");
        request.setSex("男");
        UpdateMemberResponse updateMemberResponse = memberService.updateMember(request);
        System.out.println(JSON.toJSONString(updateMemberResponse));
    }
}