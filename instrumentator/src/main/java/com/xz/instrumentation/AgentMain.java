package com.xz.instrumentation;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

public class AgentMain {
    public static void premain(String agentArgs, Instrumentation inst) {
        // 解析参数
        Map<String, String> argsMap = parseAgentArgs(agentArgs);
        String packageName = argsMap.getOrDefault("packageName", "core"); // 默认值为 "core"
        String methodName = argsMap.getOrDefault("methodName", "*"); // 默认值为 "*"

        // 设置拦截条件
        new AgentBuilder.Default()
                .type(ElementMatchers.nameStartsWith(packageName))
                .transform((builder, typeDescription, classLoader, javaModule, classBeingRedefined) ->
                        builder.method(methodName.equals("*") ? ElementMatchers.any() : ElementMatchers.named(methodName))
                                .intercept(Advice.to(MethodTracerAdvice.class))
                ).installOn(inst);
    }

    private static Map<String, String> parseAgentArgs(String agentArgs) {
        Map<String, String> argsMap = new HashMap<>();
        if (agentArgs != null && !agentArgs.isEmpty()) {
            String[] args = agentArgs.split(",");
            for (String arg : args) {
                String[] keyValue = arg.split("=");
                if (keyValue.length == 2) {
                    argsMap.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
        return argsMap;
    }
}
