package com.tlcsdm.core.factory;

/**
 * 初始化对象
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/2/5 7:20
 */
public interface InitializingFactory {
    /**
     * 初始化
     */
    void initialize() throws Exception;
}
