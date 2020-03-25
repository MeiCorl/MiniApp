package com.meicorl.shopping_mall_miniapp.aspects;

import com.alibaba.fastjson.JSONObject;
import com.meicorl.shopping_mall_miniapp.annotations.FlowControll;
import com.meicorl.shopping_mall_miniapp.common.GlobalException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Objects;

/**
 * 根据 userId_userType@projectId:action 限频
 * @author caomei
 * @date 2019/9/16
 **/
@Aspect
@Component
@Profile({"test", "prod"})
public class FlowControllAspect {
    private static final String FLOW_CONTROLL_REDIS_KEY = "FlowControll#%s_%s@%s:%s";

    @Autowired
    RedisTemplate redisTemplate;

    /** 以自定义 @FlowControll 注解为切点 */
    @Pointcut("@annotation(com.meicorl.shopping_mall_miniapp.annotations.FlowControll)")
    public void flowControll() {}

    @Before("flowControll()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        JSONObject flowControllInfo = Objects.requireNonNull(getFlowControllInfo(joinPoint));
        JSONObject params = (JSONObject)joinPoint.getArgs()[0];
        String userId = params.getString("userId");
        String userType = params.getString("userType");
        String projectId = params.getString("projectId");
        String action = flowControllInfo.getString("action");
        int interval = flowControllInfo.getInteger("interval");
        String controllRule = String.format(FLOW_CONTROLL_REDIS_KEY, userId, userType, projectId, action);

        // 限流：限制每个用户interval秒内最多请求一次
        if(!redisTemplate.opsForValue().setIfAbsent(controllRule, "1", Duration.ofSeconds(interval)))
            throw new GlobalException("请求太频繁, 请稍后再试!");
    }

    /**
     * 获取限流规则、限流动作、限流时间间隔
     * @param joinPoint
     * @return
     * @throws Exception
     */
    private JSONObject getFlowControllInfo(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    JSONObject flowControllInfo = new JSONObject();
                    flowControllInfo.put("action", method.getAnnotation(FlowControll.class).action());
                    flowControllInfo.put("interval", method.getAnnotation(FlowControll.class).interval());
                    return flowControllInfo;
                }
            }
        }
        return null;
    }
}
 