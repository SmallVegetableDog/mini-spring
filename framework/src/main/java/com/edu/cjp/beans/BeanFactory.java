package com.edu.cjp.beans;

import com.edu.cjp.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 澎仔 on 2019/6/28.
 */
public class BeanFactory {
    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();

    public static Object getBean(Class<?> cls) {
        return classToBean.get(cls);
    }

    //初始化Bean，classList是所有类的全限定名
    public static void initBean(List<Class<?>> classList) throws Exception {
        List<Class<?>> toCreate = new ArrayList<>(classList);
        while (toCreate.size() > 0) {
            int remainSize = toCreate.size();
            Iterator<Class<?>> iterator = toCreate.iterator();
            while (iterator.hasNext()) {
                //如果初始化成功，则把该类的全限定名去掉
                if (finishCreate(iterator.next())) {
                    iterator.remove();
                }
            }
            //如果一个循环之后没有类可以被初始化，则抛出异常，因为进入了依赖循环当中
            if (remainSize == toCreate.size()) {
                throw new Exception("cycle dependency!");
            }
        }
    }

    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        //该类既没有@Bean注释和@Controller注释，则说明该类不需要初始化到BeanFactory当中
        if (!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)) {
            return true;
        }
        Object bean = cls.newInstance();
        //找出该类的所有属性，判断这些属性是否需要进行依赖注入
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoWired.class)) {
                Class<?> fieldType = field.getType();
                Object relianBean = BeanFactory.getBean(fieldType);
                //BeanFactory中含有这个属性的对象，则进行注入，否则就等待下一轮循环再实例化。
                if(relianBean==null){
                    return false;
                }
                field.setAccessible(true);
                field.set(bean,relianBean);
            }
        }
        //实例化成功，放入BeanFactory
        classToBean.put(cls,bean);
        return true;
    }
}
