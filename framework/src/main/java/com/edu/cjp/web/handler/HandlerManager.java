package com.edu.cjp.web.handler;

import com.edu.cjp.web.mvc.Controller;
import com.edu.cjp.web.mvc.RequestMapping;
import com.edu.cjp.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 澎仔 on 2019/6/28.
 */
public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    public static void resolveMappingHandler(List<Class<?>> classList) {
        //把带有@controller的类全部挑选出来
        for (Class cls : classList) {
            if (cls.isAnnotationPresent(Controller.class)) {
                parseHandlerFromController(cls);
            }
        }
    }

    //对含有@RequestMapping注解的方法进行处理
    private static void parseHandlerFromController(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            //过滤掉没有@RequestMapping注释的方法
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            //存放带有@RequestParam注释的方法参数
            List<String> paramNameList = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            //把paramNameList转为数组形式
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);
            MappingHandler mappingHandler = new MappingHandler(uri,method,cls,params);
            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}
