package com.flash.framework.dubbo.context;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author zhurg
 * @date 2021/6/27 - 上午9:07
 */
public class RpcRequestContext {

    public static final Integer DEFAULT_TENANT_ID = 1;

    public static final String DEFAULT_OPERATOR = "system";

    private static final ThreadLocal<Integer> TENANT_ID = new ThreadLocal<>();

    private static final ThreadLocal<String> OPERATOR = new ThreadLocal<>();

    public static void set(Integer tenantId, String operator) {
        tenantId = Objects.isNull(tenantId) ? DEFAULT_TENANT_ID : tenantId;
        operator = StringUtils.isBlank(operator) ? DEFAULT_OPERATOR : operator;
        TENANT_ID.set(tenantId);
        OPERATOR.set(operator);
    }

    public static Integer tenantId() {
        return TENANT_ID.get();
    }

    public static String operator() {
        return OPERATOR.get();
    }

    public static void clear() {
        TENANT_ID.remove();
        OPERATOR.remove();
    }
}