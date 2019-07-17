package com.edu.cjp.starter;

import com.edu.cjp.beans.BeanFactory;
import com.edu.cjp.core.ClassScaner;
import com.edu.cjp.web.handler.HandlerManager;
import com.edu.cjp.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.util.List;

/**
 * Created by 澎仔 on 2019/6/27.
 */
public class MiniApplication {
    public static void run(Class<?> cls, String[] args) {
        System.out.println("hello miniApplication");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
            //cls.getPackage().getName()======com.edu.cjp
            //找出符合要求的类的全限定名
            List<Class<?>> classList = ClassScaner.scanClasses(cls.getPackage().getName());
            //对所有类进行扫描，有@Bean注解的类则进行初始化并用BeanFactory进行管理起来
            BeanFactory.initBean(classList);

            //把含有@Controller注解的类中含有@RequestMapping注解的方法封装为一个MappingHandler，多个MappingHandler组成一个mappingHandlerList
            //然后在DispatcherServlet中对mappingHandler循环，如果请求的uri和mappingHandler的uri符合则进行具体的方法调用。
            HandlerManager.resolveMappingHandler(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
