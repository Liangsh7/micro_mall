package com.cskaoyan.gateway.form.shopping;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * create-date: 2019/7/29-下午10:49
 */
@Data
@ApiModel
public class PageResponse {

    @ApiModelProperty(name = "data", value = "数据信息")
    private Object data;

    @ApiModelProperty(name = "total", value = "数据条数", example = "10")
    private Long total;
}
