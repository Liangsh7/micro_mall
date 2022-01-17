package com.mall.shopping.dto;/**
 * Created by mic on 2019/7/29.
 */

import lombok.Data;

import java.io.Serializable;

/**
 *  ciggar
 * create-date: 2019/7/29-下午11:14
 */
@Data
public class RecommendDto implements Serializable{

    private Integer id;

    private String name;

    private Integer type;

    private Integer sortOrder;

    private Integer position;

    private String remark;




}
