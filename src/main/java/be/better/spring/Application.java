package be.better.spring;

import be.better.spring.core.beans.BeanFactory;
import be.better.spring.core.scanner.ClassScanner;
import be.better.spring.core.server.TomcatServer;
import be.better.spring.core.web.handler.HandlerManager;

import java.util.List;

/**
 * 项目入口类
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("Welcome be better springDemo");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
            System.out.println(Application.class.getPackage().getName());
            // 加载所有class，输入参数就是启动类的包名
            List<Class<?>> classList = ClassScanner
                    .scanClasses(Application.class.getPackage().getName());
            BeanFactory.initBean(classList);
            HandlerManager.resolveMappingHandler(classList);
            classList.forEach(it -> System.out.println(it.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
