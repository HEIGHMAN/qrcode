package user.provider.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import yx.qrcode.annotation.Log;

import java.lang.reflect.Method;

/**
 * 操作日志记录处理
 *
 */
@Aspect
@Component
public class LogAspect
{
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    // 配置织入点
    @Pointcut("@annotation(yx.qrcode.annotation.Log)")
    public void logPointCut()
    {
    }

    /**
     * 前置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doBefore(JoinPoint joinPoint)
    {
        System.out.println("before: "+joinPoint);
        System.out.println(joinPoint.getArgs());
        System.out.println(joinPoint.getKind());
        System.out.println(joinPoint.getSourceLocation());
//        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e)
    {
        System.out.println("doAfter Throwing:" + joinPoint.toString());
        System.out.println(joinPoint.getArgs());
        System.out.println(joinPoint.getKind());
        System.out.println(joinPoint.getSourceLocation());
//        handleLog(joinPoint, e);
    }
    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        System.out.println("annotationLog"+method.getAnnotation(Log.class));
        if (method != null)
        {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
