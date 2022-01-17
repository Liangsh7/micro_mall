package com.mall.shopping.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.Set;

/**
 *  ciggar
 * create-date: 2019/7/23-17:48
 */
@Data
public class HomePageResponse extends AbstractResponse {

    private Set<PanelDto> panelContentItemDtos;
}
