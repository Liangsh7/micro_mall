package com.mall.pay.dto.alipay;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;
import lombok.ToString;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
@ToString
public class AlipaymentResponse extends AbstractResponse {

    /**构建html表单*/
    private String qrCode;

}