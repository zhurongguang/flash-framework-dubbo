package com.flash.framework.dubbo.filter;

import com.flash.framework.dubbo.common.request.RpcRequest;
import com.flash.framework.dubbo.common.response.RpcResponse;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.validation.Validator;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhurg
 * @date 2019/4/29 - 上午9:26
 */
@Activate(
        group = {CommonConstants.CONSUMER}, order = 10001
)
public class PreValidateFilter implements Filter {

    private final Map<String, Validator> validators = Maps.newConcurrentMap();

    private final Set<String> rpcResponseMethods = Sets.newConcurrentHashSet();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Object[] args = invocation.getArguments();
        if (Objects.nonNull(args) && args.length == 1 && args[0] instanceof RpcRequest) {
            try {
                Validator validator = getValidator(invoker.getUrl());
                if (Objects.nonNull(validator)) {
                    validator.validate(invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
                }

                RpcRequest request = (RpcRequest) args[0];
                request.checkRequestParam();
            } catch (ConstraintViolationException e) {
                if (isRpcResponse(invoker, invocation)) {
                    return AsyncRpcResult.newDefaultAsyncResult(RpcResponse.fail(e.getMessage()), invocation);
                }
            } catch (IllegalArgumentException e) {
                if (isRpcResponse(invoker, invocation)) {
                    return AsyncRpcResult.newDefaultAsyncResult(RpcResponse.fail(e.getMessage()), invocation);
                }
            } catch (RpcException e) {
                throw e;
            } catch (Throwable t) {
                return AsyncRpcResult.newDefaultAsyncResult(t, invocation);
            }
        }

        return invoker.invoke(invocation);
    }

    private boolean isRpcResponse(Invoker<?> invoker, Invocation invocation) {
        String key = invoker.getUrl().toFullString();
        if (rpcResponseMethods.contains(key)) {
            return true;
        }
        Method method = MethodUtils.getAccessibleMethod(ReflectUtils.forName(invoker.getUrl().getServiceInterface()), invocation.getMethodName(), invocation.getParameterTypes());
        if (method.getReturnType().equals(RpcResponse.class)) {
            rpcResponseMethods.add(key);
            return true;
        }
        return false;
    }

    private Validator getValidator(URL url) {
        String key = url.toFullString();
        Validator validator = validators.get(key);
        if (validator == null) {
            validators.put(key, createValidator(url));
            validator = validators.get(key);
        }
        return validator;
    }

    private Validator createValidator(URL url) {
        return new FFValidator(ReflectUtils.forName(url.getServiceInterface()));
    }
}