package com.mall.user.intercepter;

import com.alibaba.fastjson.JSON;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.user.IUserLoginService;
import com.mall.user.annotation.Anoymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.CheckAuthRequest;
import com.mall.user.dto.CheckAuthResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 用来实现token拦截认证
 */
public class TokenIntercepter extends HandlerInterceptorAdapter {

    @Reference(timeout = 3000,check = false)
    IUserLoginService iUserLoginService;

    public static String ACCESS_TOKEN="access_token";

    public static String USER_INFO_KEY="userInfo";

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception {

        //解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin")); // 设置允许所有跨域访问（支持带Cookie）
        response.setHeader("Access-Control-Allow-Credentials", "true"); // 配合前端
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept,Authorization,token");
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Object bean=handlerMethod.getBean();
        if(isAnoymous(handlerMethod)){
            return true;
        }
        String token= CookieUtil.getCookieValue(request,ACCESS_TOKEN);
        if(StringUtils.isEmpty(token)){
            ResponseData responseData=new ResponseUtil().setErrorMsg("token已失效");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(JSON.toJSON(responseData).toString());
            return false;
        }
        CheckAuthRequest checkAuthRequest=new CheckAuthRequest();
        checkAuthRequest.setToken(token);
        CheckAuthResponse checkAuthResponse=iUserLoginService.validToken(checkAuthRequest);
        if(checkAuthResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            request.setAttribute(USER_INFO_KEY,checkAuthResponse.getUserinfo()); //保存token解析后的信息后续要用
            return super.preHandle(request, response, handler);
        }
        ResponseData responseData=new ResponseUtil().setErrorMsg(checkAuthResponse.getMsg());
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(JSON.toJSON(responseData).toString());
        return false;
    }

    private boolean isAnoymous(HandlerMethod handlerMethod){
        Object bean=handlerMethod.getBean();
        Class clazz=bean.getClass();
        if(clazz.getAnnotation(Anoymous.class)!=null){
            return true;
        }
        Method method=handlerMethod.getMethod();
        return method.getAnnotation(Anoymous.class)!=null;
    }
}
