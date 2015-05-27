package javaGroovy;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GroovyLoader {
    private static Map<String, Object> cache = new HashMap<String, Object>();

    public static <T> T createGroovyInstance(Class<T> groovyInterface) {
        String path = getPath(groovyInterface);
        return createGroovyInstance(path);
    }

    public static <T> T createGroovyInstance(String path) {
        if (cache.containsKey(path)) {
            return (T)cache.get(path);
        }
        T instance = createNew(path);
        cache.put(path, instance);
        return instance;
    }

    private static <T> T createNew(String path) {
        try {
            GroovyClassLoader gcl = new GroovyClassLoader();
            Class clazz = gcl.parseClass(new File(path));
            Object instance = clazz.newInstance();
            return (T) instance;
        } catch(Exception e) {
            throw new RuntimeException("Failed to create groovy " +
                    "instance for path="+path, e);
        }
    }

    private static <T> String getPath(Class<T> groovyInterface) {
        String groovyInterfaceName = groovyInterface.getName();
        String[] groovyInterfaceNameParts = groovyInterfaceName.split("\\.");
        String groovyInterfaceSimpleName = groovyInterfaceNameParts[groovyInterfaceNameParts.length-1];
        String groovyClassNameSimpleName = groovyInterfaceSimpleName.substring(1);
        String path = "src/";
        for (int i = 0; i < groovyInterfaceNameParts.length - 1; i++) {
            path += groovyInterfaceNameParts[i];
            if (i < groovyInterfaceNameParts.length - 2) {
                path += "/";
            }
        }
        path = path + "/" + groovyClassNameSimpleName + ".groovy";
        return path;
    }

}






