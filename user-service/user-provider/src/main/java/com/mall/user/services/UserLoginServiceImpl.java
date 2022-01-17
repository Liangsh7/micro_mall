package com.mall.user.services;

import com.alibaba.fastjson.JSON;
import com.mall.user.IUserLoginService;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.converter.UserConverterMapper;
import com.mall.user.dal.entitys.Member;
import com.mall.user.dal.persistence.MemberMapper;
import com.mall.user.dto.CheckAuthRequest;
import com.mall.user.dto.CheckAuthResponse;
import com.mall.user.dto.UserLoginRequest;
import com.mall.user.dto.UserLoginResponse;
import com.mall.user.utils.ExceptionProcessorUtils;
import com.mall.user.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  ciggar
 * create-date: 2019/7/22-13:21
 */
@Slf4j
@Component
@Service
public class UserLoginServiceImpl implements IUserLoginService {

    @Autowired
    MemberMapper memberMapper;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        log.info("Begin UserLoginServiceImpl.login: request:"+request);
        UserLoginResponse response=new UserLoginResponse();
        try {
            request.requestCheck();
            Example example = new Example(Member.class);
            example.createCriteria().andEqualTo("state",1).andEqualTo("username",request.getUserName());

            List<Member> member = memberMapper.selectByExample(example);
            if(member==null||member.size()==0) {
                response.setCode(SysRetCodeConstants.USERORPASSWORD_ERRROR.getCode());
                response.setMsg(SysRetCodeConstants.USERORPASSWORD_ERRROR.getMessage());
                return response;
            }
            //验证是否已经激活

            if("N".equals(member.get(0).getIsVerified())){
                response.setCode(SysRetCodeConstants.USER_ISVERFIED_ERROR.getCode());
                response.setMsg(SysRetCodeConstants.USER_ISVERFIED_ERROR.getMessage());
                return response;
            }

            if(!DigestUtils.md5DigestAsHex(request.getPassword().getBytes()).equals(member.get(0).getPassword())){
                response.setCode(SysRetCodeConstants.USERORPASSWORD_ERRROR.getCode());
                response.setMsg(SysRetCodeConstants.USERORPASSWORD_ERRROR.getMessage());
                return response;
            }
            Map<String,Object> map=new HashMap<>();
            map.put("uid",member.get(0).getId());
            map.put("file",member.get(0).getFile());
            map.put("username",member.get(0).getUsername());

            String token=JwtTokenUtils.builder().msg(JSON.toJSON(map).toString()).build().creatJwtToken();
            response=UserConverterMapper.INSTANCE.converter(member.get(0));
            response.setToken(token);
            response.setCode(SysRetCodeConstants.SUCCESS.getCode());
            response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            log.error("UserLoginServiceImpl.login Occur Exception :"+e);
            ExceptionProcessorUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public CheckAuthResponse validToken(CheckAuthRequest request) {
        log.info("Begin UserLoginServiceImpl.validToken: request:"+request);
        CheckAuthResponse response=new CheckAuthResponse();
        response.setCode(SysRetCodeConstants.SUCCESS.getCode());
        response.setMsg(SysRetCodeConstants.SUCCESS.getMessage());
        try{
            request.requestCheck();
            String decodeMsg=JwtTokenUtils.builder().token(request.getToken()).build().freeJwt();
            if(StringUtils.isNotBlank(decodeMsg)){
                log.info("validate success");
                response.setUserinfo(decodeMsg);
                return response;
            }
            response.setCode(SysRetCodeConstants.TOKEN_VALID_FAILED.getCode());
            response.setMsg(SysRetCodeConstants.TOKEN_VALID_FAILED.getMessage());


        }catch (Exception e){
            log.error("UserLoginServiceImpl.validToken Occur Exception :"+e);
            ExceptionProcessorUtils.wrapperHandlerException(response,e);
        }
        return response;
    }
}
