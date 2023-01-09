package be.better.spring.core.server;

import be.better.spring.core.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer {
    private Tomcat tomcat;
    /**
     * 启动参数
     */
    private String[] args;

    public TomcatServer(String[] args) {
        this.args = args;
    }

    public void startServer() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(1234);
        tomcat.start();

        // 把Servlet注册到tomcat中
        // 实例化一个Context
        Context context = new StandardContext();
        context.setPath("");
        // 监听器
        context.addLifecycleListener(new Tomcat.FixContextListener());
        DispatcherServlet servlet = new DispatcherServlet();
        // 把servlet 添加到Tomcat中，并且设置它支持异步，这里给servlet起名为 "DispatcherServlet"
        Tomcat.addServlet(context, "DispatcherServlet", servlet)
                .setAsyncSupported(true);
        // uri 到servlet的映射， / 表示拦截所有的uri
        context.addServletMappingDecoded("/", "DispatcherServlet");
        // 把context注册到Host中
        tomcat.getHost().addChild(context);

        // 等待线程
        Thread awaitThread = new Thread("tomcat_await_thread") {
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        // 设置线程为非守护线程
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
