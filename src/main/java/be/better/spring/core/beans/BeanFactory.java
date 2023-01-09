package be.better.spring.core.beans;

import be.better.spring.core.web.servlet.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
    private static Map<Class<?>, Object> classToBean =
            new ConcurrentHashMap<>();

    /**
     * 获取Bean
     *
     * @param cls
     * @return
     */
    public static Object getBean(Class<?> cls) {
        return classToBean.get(cls);
    }

    public static void initBean(List<Class<?>> classList) throws InstantiationException, IllegalAccessException {
        List<Class<?>> toCreate = new ArrayList<>(classList);
        while (toCreate.size() != 0) {
            int remainSize = toCreate.size();
            for (int i = 0; i < toCreate.size(); i++) {
                if (finishCreate(toCreate.get(i))) {
                    toCreate.remove(i);
                }
            }
            if (toCreate.size() == remainSize) {
                throw new RuntimeException("cycle dependency!");
            }
        }
    }

    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        if (!cls.isAnnotationPresent(Service.class)
                && !cls.isAnnotationPresent(Controller.class)) {
            return true;
        }
        Object bean = cls.newInstance();
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoWired.class)) {
                Class<?> fieldType = field.getType();
                Object reliantBean = BeanFactory.getBean(fieldType);
                if (reliantBean == null) {
                    // 依赖的Bean不存在
                    return false;
                }
                field.setAccessible(true);
                // 把依赖的Bean注入进去
                field.set(bean, reliantBean);
            }
        }
        classToBean.put(cls, bean);
        return true;
    }
}
