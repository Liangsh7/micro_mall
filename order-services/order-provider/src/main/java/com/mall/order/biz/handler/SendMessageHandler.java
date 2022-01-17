package com.mall.order.biz.handler;

//import com.gpmall.commons.mq.producer.RabbitMessageProducer;
import com.mall.commons.mq.producer.RocketMqProducer;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 利用mq发送延迟取消订单消息
 * @Author： ciggar
 * @Date: 2019-09-17 23:14
 **/
@Component
@Slf4j
public class SendMessageHandler extends AbstractTransHandler {
//	@Autowired
//	private RabbitMessageProducer rabbitMessageProducer;

	@Autowired
	private RocketMqProducer mqProducer;

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	public boolean handle(TransHandlerContext context) {
		CreateOrderContext createOrderContext = (CreateOrderContext) context;
		//将订单发送到rabbitmq
		try {
			mqProducer.sendOrderMessage(createOrderContext.getOrderId());
		}catch (Exception e){
			log.error("发送订单id:{}到延迟队列失败",createOrderContext.getOrderId());
			return false;
		}
		return true;
	}
}