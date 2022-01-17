package com.mall.order.biz.handler;

import com.mall.commons.tool.exception.BizException;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.user.IMemberService;
import com.mall.user.dto.QueryMemberRequest;
import com.mall.user.dto.QueryMemberResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  ciggar
 * create-date: 2019/8/1-下午4:47
 *
 * TODO:  如何解决商品的超卖问题？ 我这里没有进行扩展，有兴趣的同学可以进行扩展
 */
@Slf4j
@Component
public class ValidateHandler extends AbstractTransHandler {

    @Autowired
    OrderMapper orderMapper;

    @Reference(check = false)
    IMemberService memberService;

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean handle(TransHandlerContext context) {
        log.info("begin ValidateHandler :context:"+context);
        CreateOrderContext createOrderContext=(CreateOrderContext)context;
        QueryMemberRequest queryMemberRequest =new QueryMemberRequest();
        queryMemberRequest.setUserId(createOrderContext.getUserId());
        QueryMemberResponse response=memberService.queryMemberById(queryMemberRequest);
        if(OrderRetCode.SUCCESS.getCode().equals(response.getCode())){
            createOrderContext.setBuyerNickName(response.getUsername());
        }else{
            throw new BizException(response.getCode(),response.getMsg());
        }
        return true;
    }
}
