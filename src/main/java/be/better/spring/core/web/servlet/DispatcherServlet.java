package be.better.spring.core.web.servlet;


import be.better.spring.core.web.handler.HandlerManager;
import be.better.spring.core.web.handler.MappingHandler;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class DispatcherServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * servlet
     *
     * @param req 从ServletRequest读取数据
     * @param res 把结果输出到servletResponse中
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(ServletRequest req, ServletResponse res) {
        // 遍历 判断是哪个MappingHandler处理这个请求
        for (MappingHandler mappingHandler :
                HandlerManager.mappingHandlerList) {
            System.out.println(mappingHandler.toString());
            try {
                if (mappingHandler.handle(req, res)) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
