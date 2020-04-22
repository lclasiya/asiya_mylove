package li.changlin.oauth2.utils;

import li.changlin.user.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAop {
    private Logger logger = LoggerFactory.getLogger(LogAop.class);

    @Pointcut("@annotation(li.changlin.common.aop.AdviceAnnotation)")
    public void webLog(){}

    @Around("webLog()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        //先执行业务
        Object result = point.proceed();
        try {
            handle(point);
        } catch (Exception e) {
            logger.error("日志记录出错!", e);
        }
        return result;
    }
    private void handle(ProceedingJoinPoint point) throws Exception{
        Object[] args = point.getArgs();
        User user = (User)args[0];
        String logAsiya = "ユーザー名:" +user.getUsername()+ ";" + "パスワード:" + args[1];
        logger.info(logAsiya);
    }
}
