package com.mall.user.services;

import com.mall.commons.mq.producer.RocketMqProducer;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.user.IUserRegisterService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dal.entitys.Member;
import com.mall.user.dal.entitys.UserVerify;
import com.mall.user.dal.persistence.MemberMapper;
import com.mall.user.dal.persistence.UserVerifyMapper;
import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;
import com.mall.user.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  ciggar
 * create-date: 2019/7/23-12:50
 */
@Slf4j
@Component
@Service
public class UserRegisterServiceImpl implements IUserRegisterService {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    RedissonClient redissonClient;

    @Value("${email.text}")
    private String emailStr;

//    @Autowired
//    KafKaRegisterSuccProducer kafKaRegisterSuccProducer;
    @Autowired
    RocketMqProducer rocketMqProducer;

    @Autowired
    UserVerifyMapper userVerifyMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    private ExecutorService executorService;

    @PostConstruct
    public void init(){
        executorService = Executors.newFixedThreadPool(2);
    }


    @Override
    public UserRegisterResponse register(UserRegisterRequest request) {
        log.info("Begin UserLoginServiceImpl.register: request:" + request);
        UserRegisterResponse response = new UserRegisterResponse();
        try {
            validUserRegisterRequest(request);
            Member member = new Member();
            member.setUsername(request.getUserName());
            member.setPassword(DigestUtils.md5DigestAsHex(request.getUserPwd().getBytes()));

            member.setState(1);
            member.setCreated(new Date());
            member.setUpdated(new Date());
            member.setIsVerified("N");//Y为激活
            member.setEmail(request.getEmail());
            if (memberMapper.insert(member) != 1) {
                response.setCode(SysRetCodeConstants.USER_REGISTER_FAILED.getCode());
                response.setMsg(SysRetCodeConstants.USER_REGISTER_FAILED.getMessage());
                return response;
            }
            //插入用户验证表
            UserVerify userVerify = new UserVerify();
            userVerify.setUsername(member.getUsername());
            String key = member.getUsername()+member.getPassword()+UUID.randomUUID().toString();
            userVerify.setUuid(DigestUtils.md5DigestAsHex(key.getBytes()));
            userVerify.setIsExpire("N");//注册信息是否过期
            userVerify.setIsVerify("N");//是否验证成功
            userVerify.setRegisterDate(new Date());
            if(userVerifyMapper.insert(userVerify)!=1){
                response.setCode(SysRetCodeConstants.USER_REGISTER_VERIFY_FAILED.getCode());
                response.setMsg(SysRetCodeConstants.USER_REGISTER_VERIFY_FAILED.getMessage());
                return response;
            }
            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
            response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());


            //发送消息到KafKa 目前由于发送邮件激活
            Map map = new HashMap();
            map.put("username",userVerify.getUsername());
            map.put("key",userVerify.getUuid());
            map.put("email",member.getEmail());

            //发送消息替换为RocketMQ
//            rocketMqProducer.sendRegisterSuccMessage(map);

            executorService.submit(() -> {
                sendMail(map);
            });

        } catch (Exception e) {
            log.error("UserLoginServiceImpl.register Occur Exception :" + e);
            ExceptionProcessorUtils.wrapperHandlerException(response, e);
        }
        return response;
    }

    //校验参数以及校验用户名是否存在
    private void validUserRegisterRequest(UserRegisterRequest request) {
        request.requestCheck();
        Example example = new Example(Member.class);
        example.createCriteria().andEqualTo("state", 1).andEqualTo("username", request.getUserName());

        List<Member> users = memberMapper.selectByExample(example);
        if (users != null && users.size() > 0) {
            throw new ValidateException(SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getCode(), SysRetCodeConstants.USERNAME_ALREADY_EXISTS.getMessage());
        }
    }

    public void sendMail(Map emmailMap){
        try {
            String mail = (String) emmailMap.get("email");
            String username = (String) emmailMap.get("username");
            String uid = (String) emmailMap.get("key");

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(mail);
            simpleMailMessage.setFrom("ciggarnot@163.com");
            simpleMailMessage.setSubject("CSMALL 商城激活");
            String text = String.format(emailStr, username, uid);
            simpleMailMessage.setText(text);
            javaMailSender.send(simpleMailMessage);
            log.info("邮件发送成功，邮件内容：{}，收件人地址：{}",text,mail);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
