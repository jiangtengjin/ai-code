package com.xhh.aicode.aop;

import cn.hutool.core.util.ObjUtil;
import com.xhh.aicode.annotation.AuthCheck;
import com.xhh.aicode.exception.ErrorCode;
import com.xhh.aicode.exception.ThrowUtils;
import com.xhh.aicode.innerservice.InnerUserService;
import com.xhh.aicode.model.entity.User;
import com.xhh.aicode.model.enums.UserRoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {


    /**
     *  执行拦截
     * @param joinPoint     切点
     * @param authCheck     权限校验注解
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        User loginUser = InnerUserService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // 不需要权限，放行
        if (ObjUtil.isEmpty(mustRoleEnum)) {
            joinPoint.proceed();
        }
        // 以下为：必须有改权限才通过
        // 获取当前用户的权限
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        // 没有权限，拒绝
        ThrowUtils.throwIf(ObjUtil.isEmpty(userRoleEnum), ErrorCode.NO_AUTH_ERROR);
        // 要求必须是管理员或者会员权限，但是用户不是，拒绝
        ThrowUtils.throwIf(
                (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) ||
                        (UserRoleEnum.NUMBER.equals(mustRoleEnum) && !UserRoleEnum.NUMBER.equals(userRoleEnum)),
                ErrorCode.NO_AUTH_ERROR);
        return joinPoint.proceed();
    }

}
