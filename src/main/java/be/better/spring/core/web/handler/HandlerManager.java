package be.better.spring.core.web.handler;

import be.better.spring.core.web.servlet.mvc.Controller;
import be.better.spring.core.web.servlet.mvc.RequestMapping;
import be.better.spring.core.web.servlet.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理 MappingHandler
 */
public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * 遍历所有类，生成HandlerManager
     *
     * @param classList
     */
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for (Class<?> cls : classList) {
            // 判断该类是否被 @Controller 修饰
            if (cls.isAnnotationPresent(Controller.class)) {
                parseHandleFromController(cls);
            }
        }
    }

    private static void parseHandleFromController(Class<?> cls) {
        // 获取该类的所有方法
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            // 判断这些方法有没有被 @RequestMapping 注解修饰
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            // 从 @RequestMapping 获取uri
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            List<String> paramNameList = new ArrayList<>();
            // 获取Method的所有参数
            for (Parameter parameter : method.getParameters()) {
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    paramNameList.add(parameter.getDeclaredAnnotation((RequestParam.class)).value());
                }
            }
            //把参数名容器转成数组的形式
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);
            // 构造 MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);
            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}
