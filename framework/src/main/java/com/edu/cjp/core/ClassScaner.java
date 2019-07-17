package com.edu.cjp.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 把com.edu.cjp包下的类全部加载到JVM中
 * Created by 澎仔 on 2019/6/28.
 */
public class ClassScaner {
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        //把包名的点换成斜杠
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        //查找所有给定名称的资源。资源是可以通过类代码以与代码基无关的方式访问的某些数据（图像、声音、文本等）。
        //资源名称是以 '/' 分隔的标识资源的路径名称。
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resoure = resources.nextElement();
            if (resoure.getProtocol().contains("jar")) {
                JarURLConnection jarURLConnection = (JarURLConnection) resoure.openConnection();
                String jarFilePath = jarURLConnection.getJarFile().getName();
                //获取到当前jar文件中符合path路径要求的类
                classList.addAll(ClassScaner.getClassesFromJar(jarFilePath, path));

            } else {
                //todo
            }
        }
        return classList;
    }

    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                String classFullName = entryName.replace('/', '.')
                        .substring(0, entryName.length() - 6);
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
