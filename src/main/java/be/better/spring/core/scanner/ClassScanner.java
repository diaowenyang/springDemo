package be.better.spring.core.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器
 */
public class ClassScanner {

    public static List<Class<?>> classes = new ArrayList<>();

    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        String path = packageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            // 打包成jar运行
            if (resource.getProtocol().contains("jar")) {
                JarURLConnection jar = (JarURLConnection) resource.openConnection();
                String jarPath = jar.getJarFile().getName();
                classList.addAll(getClassesFromJar(jarPath, path));
            } else {
                // 不打包，直接运行main函数
                String filePath = URLDecoder.decode(resource.getPath(), "UTF-8");
                // 是否循环迭代
                boolean recursive = true;
                classList.addAll(findAndClassesInPackageByFile(packageName, filePath, recursive));
            }
        }
        return classList;
    }

    private static Collection<Class<?>> getClassesFromJar(String jarPath, String path) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();
            // 只要传入路径开头的文件，以及它以.class结尾
            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                // 把路径转成包名，并且把class文件的 .class 后缀去掉
                String classFullName = entryName.replace("/", ".")
                        .substring(0, entryName.length() - 6);
                // 把类 加载到类加载器中
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }

    /**
     * 以文件的形式获取包下的所有Class
     *
     * @param packageName
     * @param filePath
     * @param recursive
     * @return
     */
    private static Collection<Class<?>> findAndClassesInPackageByFile(String packageName, String filePath, boolean recursive) {
        File dir = new File(filePath);
        // 如果不存在或者不是目录，就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        // 如果存在，就获取包下的所有文件，包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findAndClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
            } else {
                // 如果是java文件，去掉后面的 .class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader()
                            .loadClass(packageName + "." + className));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }
}
