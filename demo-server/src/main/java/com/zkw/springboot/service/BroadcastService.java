package com.zkw.springboot.service;

import com.zkw.springboot.bean.User;

/**
 * @author zhangkewei
 * @date 2020/12/22 12:21
 * @desc 广播服务
 */
public interface BroadcastService{
    /**
     * 用户切换地图请求，通知其他在线用户
     * @param user
     * @param oldMapId
     */
    void changeMapToAll(User user, Integer oldMapId);

    /**
     * 通知其他在线用户更新用户数据
     * @param user
     * @param description
     */
    void updateAll(User user, String description);
}
