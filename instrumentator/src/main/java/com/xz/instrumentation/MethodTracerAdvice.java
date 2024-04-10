package com.xz.instrumentation;

import net.bytebuddy.asm.Advice;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodTracerAdvice {

    @Advice.OnMethodEnter
    static long enterMethod(@Advice.Origin Method method, @Advice.AllArguments Object[] args) {
        long startTime = System.currentTimeMillis();
        // 打印方法签名和输入参数
        System.out.println("Entering method: " + method.toString());
        System.out.println("Arguments: " + Arrays.toString(args));
        return startTime;
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    static void exitMethod(@Advice.Enter long startTime, @Advice.Origin Method method, @Advice.Return Object returnValue, @Advice.Thrown Throwable throwable) {
        long duration = System.currentTimeMillis() - startTime;
        // 打印方法签名、执行时间、返回值和抛出的异常（如果有）
        System.out.println("Exiting method: " + method.toString());
        System.out.println("Method execution time: " + duration + " milliseconds");
        System.out.println("Return value: " + returnValue);
        if (throwable != null) {
            System.out.println("Exception: " + throwable.getMessage());
        }
    }
}
