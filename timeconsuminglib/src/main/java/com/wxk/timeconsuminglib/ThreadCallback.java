package com.wxk.timeconsuminglib;

/**
 * Date: 2019/11/20
 * Author: wangxiankai
 * Description:
 */
public interface ThreadCallback<T> {

    /**
     * 在子线程运行
     */
    T runOnThread();

    /**
     * 回到主线程
     */
    void runOnUi(T t);
}
