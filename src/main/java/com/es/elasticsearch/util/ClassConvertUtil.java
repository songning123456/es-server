package com.es.elasticsearch.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sonning
 */
public class ClassConvertUtil {
    private static final Map<String, Method> map = new HashMap<>();

    /**
     * 将dto和entity之间的属性互相转换,dto中属性一般为String等基本类型
     * 但是entity中可能有复合主键等复杂类型,需要注意同名问题
     *
     * @param src
     * @param target
     * @return
     */
    public static Object populate(Object src, Object target) {

        Method[] srcMethods = src.getClass().getMethods();
        Method[] targetMethods = target.getClass().getMethods();

        if (!map.isEmpty() && map.containsKey(src.getClass().getSimpleName())) {
            for (Field field : target.getClass().getDeclaredFields()) {
                try {
                    String getKey = "get-" + field.getName();
                    String setKey = "set-" + field.getName();
                    Method getMethod = map.get(getKey);
                    Method setMethod = map.get(setKey);
                    if (getMethod != null && setMethod != null) {
                        Object srcResult = getMethod.invoke(src);
                        setMethod.invoke(target, srcResult);
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Method srcMethod : srcMethods) {
                String srcMethodName = srcMethod.getName();
                if (srcMethodName.startsWith("get")) {
                    try {
                        Object result = srcMethod.invoke(src);
                        String fieldName = srcMethodName.substring(3, 4).toLowerCase() + srcMethodName.substring(4);
                        map.put("get-" + fieldName, srcMethod);

                        for (Method targetMethod : targetMethods) {
                            String targetMethodName = targetMethod.getName();

                            if (targetMethodName.startsWith("set") && targetMethodName.substring(3, targetMethodName.length())
                                    .equals(srcMethodName.substring(3, srcMethodName.length()))) {
                                targetMethod.invoke(target, result);
                                map.put("set-" + fieldName, targetMethod);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // 存储类类型，做判断是否可以走缓存
            map.put(src.getClass().getSimpleName(), null);
        }
        return target;
    }

    /**
     * dto集合和entity集合间的互相属性映射
     *
     * @param src
     * @param target
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S, T> List<T> populateList(List<S> src, List<T> target, Class<?> targetClass) {
        for (int i = 0; i < src.size(); i++) {
            try {
                Object object = targetClass.newInstance();
                target.add((T) object);
                populate(src.get(i), object);
            } catch (Exception e) {
                // 某个方法反射异常
                continue;
            }
        }
        return target;
    }

    /**
     * @param src
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> void castEntityList(List<Object[]> src, List<T> target, Class<T> clazz) throws Exception {
        if (src.isEmpty()) {
            throw new Exception("数组列表转换实体类不能为空!");
        }
        if (!target.isEmpty()) {
            target.clear();
        }
        Object[] co = src.get(0);
        Class[] c2 = new Class[co.length];

        //确定构造方法
        for (int i = 0; i < co.length; i++) {
            c2[i] = co[i].getClass();
        }
        for (Object[] o : src) {
            Constructor<T> constructor = clazz.getConstructor(c2);
            target.add(constructor.newInstance(o));
        }
    }

    /**
     * @param src
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T caseEntity(Object src, Class<T> clazz) throws Exception {
        Object[] co = (Object[]) src;
        Class[] c2 = new Class[co.length];
        for (int i = 0; i < co.length; i++) {
            c2[i] = co[i].getClass();
        }
        Constructor<T> constructor = clazz.getConstructor(c2);
        T result = constructor.newInstance(co);
        return result;
    }
}
