package com.mall.shopping.services;

import com.mall.shopping.IContentService;
import com.mall.shopping.constant.GlobalConstants;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.converter.ContentConverter;
import com.mall.shopping.dal.entitys.PanelContent;
import com.mall.shopping.dal.persistence.PanelContentMapper;
import com.mall.shopping.dto.NavListResponse;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import com.mall.shopping.IContentService;
import com.mall.shopping.constant.GlobalConstants;
import com.mall.shopping.converter.ContentConverter;
import com.mall.shopping.dal.persistence.PanelContentMapper;
import com.mall.shopping.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 *  ciggar
 * create-date: 2019/7/23-16:23
 */
@Slf4j
@Service
public class ContentServiceImpl implements IContentService {

    @Autowired
    PanelContentMapper panelContentMapper;

    @Autowired
    ContentConverter contentConverter;

    @Override
    public NavListResponse queryNavList() {
        NavListResponse response=new NavListResponse();
        try {
            Example exampleContent = new Example(PanelContent.class);
            exampleContent.setOrderByClause("sort_order");
            Example.Criteria criteriaContent = exampleContent.createCriteria();
            criteriaContent.andEqualTo("panelId", GlobalConstants.HEADER_PANEL_ID);
            List<PanelContent> pannelContents = panelContentMapper.selectByExample(exampleContent);
            //添加缓存操作 TODO
            response.setPannelContentDtos(contentConverter.panelContents2Dto(pannelContents));
            response.setCode(ShoppingRetCode.SUCCESS.getCode());
            response.setMsg(ShoppingRetCode.SUCCESS.getMessage());
        }catch (Exception e){
            log.error("ContentServiceImpl.queryNavList Occur Exception :"+e);
            ExceptionProcessorUtils.wrapperHandlerException(response,e);
        }
        return response;
    }
}
