package com.mall.coupon;

import com.mall.coupon.dto.CreateActiRequest;
import com.mall.coupon.dto.CreateActiResponse;

/**
 * Created by oahnus on 2019/8/16
 * 0:10.
 */
public interface IActivityCoreService {
    // 创建营销活动
    CreateActiResponse create(CreateActiRequest request);
    // 修改活动
    void update();
    // 删除活动
    void delete();
}
