package com.edu.cjp.web.handler;

import com.edu.cjp.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 澎仔 on 2019/6/28.
 */
public class MappingHandler {
    private String uri;
    private Method method;
    private Class<?> controller;
    private String[] args;

    //对应一个有@RequestMapping注释的方法
    MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }

    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest) req).getRequestURI();
        //如果请求的uri和当前的MappingHandler的uri不相等则返回false
        if (requestUri == null || !uri.equals(requestUri))
            return false;
        //根据含有@RequestParam的参数进行获取对应的值
        Object[] parameters = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            //
            parameters[i] = req.getParameter(args[i]);
        }
        //获取controller的实例
        Object ctl = BeanFactory.getBean(controller);
        //使用反射执行方法
        Object response = method.invoke(ctl,parameters);
        //回显方法返回值
        res.getWriter().print(response.toString());
        return true;
    }
}
