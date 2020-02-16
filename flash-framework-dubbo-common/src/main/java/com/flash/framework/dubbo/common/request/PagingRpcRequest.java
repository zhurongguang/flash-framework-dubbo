package com.flash.framework.dubbo.common.request;

import lombok.ToString;

/**
 * dubbo 分页请求
 *
 * @author zhurg
 * @date 2019/9/3 - 上午9:52
 */
@ToString(callSuper = true)
public abstract class PagingRpcRequest extends RpcRequest {

    private static final long serialVersionUID = 690835759757065113L;

    /**
     * 页码
     */
    private long pageNo = 1L;

    /**
     * 记录数
     */
    private long pageSize = 20L;


    public long getPageNo() {
        return pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageNo(long pageNo) {
        if (pageNo <= 0) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
    }

    public void setPageSize(long pageSize) {
        if (pageSize <= 0) {
            this.pageSize = 10;
        } else {
            this.pageSize = pageSize;
        }
    }
}