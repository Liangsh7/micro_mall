package com.mall.shopping.dal.persistence;

import com.mall.commons.tool.tkmapper.TkMapper;
import com.mall.shopping.dal.entitys.Panel;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PanelMapper extends TkMapper<Panel> {

    List<Panel> selectPanelContentById(@Param("panelId")Integer panelId);
}