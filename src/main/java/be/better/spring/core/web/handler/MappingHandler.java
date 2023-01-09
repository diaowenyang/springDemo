package be.better.spring.core.web.handler;

import be.better.spring.core.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MappingHandler {
    /**
     * @RequestMapping 配置的uri
     */
    private String uri;
    /**
     * 对应的Controller类
     */
    private Class<?> controller;
    /**
     * controller 对应的处理方法
     */
    private Method method;
    /**
     * method 对应的参数
     */
    private String[] args;

    MappingHandler(String uri, Method method, Class<?> cls, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = cls;
        this.args = args;
    }

    @Override
    public String toString() {
        return "MappingHandler{" +
                "uri='" + uri + '\'' +
                ", controller=" + controller +
                ", method=" + method +
                ", args=" + Arrays.toString(args) +
                '}';
    }

    public boolean handle(ServletRequest req, ServletResponse res)
            throws IllegalAccessException, InstantiationException,
            InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest) req).getRequestURI();
        System.out.println("handle--in");
        System.out.println("requestUri:" + requestUri);
        if (!uri.equals(requestUri)) {
            return false;
        }

        Object[] parameters = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            parameters[i] = req.getParameter(args[i]);
        }

        // 实例化这个Controller
        Object ctl = BeanFactory.getBean(controller);
        // 执行method
        Object response = method.invoke(ctl, parameters);
        res.getWriter().println(response);
        return true;
    }

}
