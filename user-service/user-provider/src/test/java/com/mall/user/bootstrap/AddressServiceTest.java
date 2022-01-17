package com.mall.user.bootstrap;

import com.alibaba.fastjson.JSON;
import com.mall.user.IAddressService;
import com.mall.user.dto.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: jia.xue
 * @create: 2020-04-14 17:00
 * @Description
 **/
public class AddressServiceTest extends UserProviderApplicationTests {

    @Autowired
    IAddressService addressService;


    /**
     * 获取地址详细，根据地址id
     * @param request
     * @return
     */
    @Test
    public void test01(){

        AddressDetailRequest addressDetailRequest = new AddressDetailRequest();
        addressDetailRequest.setAddressId(3l);
        AddressDetailResponse addressDetailResponse = addressService.addressDetail(addressDetailRequest);
        System.out.println(JSON.toJSONString(addressDetailResponse));
        System.err.println("===================================");

    }

    /**
     * 获取地址列表，根据用户id
     * @param request
     * @return
     */
    @Test
    public void test02(){
        AddressListRequest request = new AddressListRequest();
        request.setUserId(62l);
        AddressListResponse addressListResponse = addressService.addressList(request);
        System.out.println(JSON.toJSONString(addressListResponse));
        System.err.println("===================================");
    }

    /**
     * 添加地址
     * @param request
     * @return
     */
    @Test
    public void test03(){
        AddAddressRequest request = new AddAddressRequest();
        request.setIsDefault(0);
        request.setStreetName("黑龙江省哈尔滨市西大直街");
        request.setTel("18888888888");
        request.setUserId(64l);
        request.setUserName("wangshuguo");
        AddAddressResponse addAddressResponse = addressService.createAddress(request);
        System.out.println(JSON.toJSONString(addAddressResponse));
        System.err.println("===================================");
    }

    /**
     * 修改地址信息
     * @param request
     * @return
     */
    @Test
    public void test04(){
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setAddressId(7l);
        request.setStreetName("湖南省长沙市雨花区xxx");
        request.setUserName("tom");
        request.setUserId(62l);
        request.setTel("13100000000");
        request.setIsDefault(1);
        UpdateAddressResponse updateAddressResponse = addressService.updateAddress(request);
        System.out.println(JSON.toJSONString(updateAddressResponse));
        System.err.println("===================================");
    }

    @Test
    public void test05(){
        DeleteAddressRequest request = new DeleteAddressRequest();
        request.setAddressId(6l);
        DeleteAddressResponse deleteAddressResponse = addressService.deleteAddress(request);
        System.out.println(JSON.toJSONString(deleteAddressResponse));
        System.err.println("===================================");
    }



}