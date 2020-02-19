package component_module;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class to work with types, annotations and fields.
 * @author Hamza Ouni
 */
public class ReflectionUtils {

    private ReflectionUtils() {

    }

    /**
     * @param clazz the class for which to return all fields
     * @return all fields declared by the passed class and its superclasses
     */
    public static Collection<Field> getAllFields(Class<?> clazz) {

        // NOTE: Use a linked hash map to keep the same order as the one used to declare the fields (useful in unit testing).
        Map<String, Field> fields = new LinkedHashMap<String, Field>();
        Class<?> targetClass = clazz;
        while (targetClass != null) {
            Field[] targetClassFields;
            try {
                targetClassFields = targetClass.getDeclaredFields();
            } catch (NoClassDefFoundError e) {
                throw new NoClassDefFoundError("Failed to get fields for class [" + targetClass.getName()
                        + "] because the class [" + e.getMessage() + "] couldn't be found in the ClassLoader.");
            }

            for (Field field : targetClassFields) {
                if (!field.isSynthetic() && !fields.containsKey(field.getName())) {
                    fields.put(field.getName(), field);
                }
            }
            targetClass = targetClass.getSuperclass();
        }
        return fields.values();
    }

    /**
     * @param clazz the class for which to return the field
     * @param fieldName the name of the field to get
     * @return the field specified from either the passed class or its superclasses
     * @exception NoSuchFieldException if the field doesn't exist in the class or superclasses
     */
    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {

        Field resultField = null;
        Class<?> targetClass = clazz;
        while (targetClass != null) {
            try {
                resultField = targetClass.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // Look in superclass
                targetClass = targetClass.getSuperclass();
            }
        }

        if (resultField == null) {
            throw new NoSuchFieldException("No field named [" + fieldName + "] in class [" + clazz.getName() + "] or superclasses");
        }

        return resultField;
    }

    /**
     * Extract the main class from the passed Type.
     *
     * @param type the generic Type
     * @return the main Class of the generic Type
     */
    public static Class getTypeClass(Type type) {

        Class typeClassClass = null;

        if (type instanceof Class) {
            typeClassClass = (Class) type;
        } else if (type instanceof ParameterizedType) {
            typeClassClass = (Class) ((ParameterizedType) type).getRawType();
        } else if (type instanceof GenericArrayType) {
            Class<?> arrrayParameter = getTypeClass(((GenericArrayType) type).getGenericComponentType());
            if (arrrayParameter != null) {
                typeClassClass = Array.newInstance(arrrayParameter, 0).getClass();
            }
        }

        return typeClassClass;
    }

    /**
     * Sets a value to a field using reflection even if the field is private.
     *
     * @param objectInstance the object containing the field
     * @param fieldName the name of the field in the object
     * @param fieldValue the value to set for the provided field
     */
    public static void setFieldValue(Object objectInstance, String fieldName, Object fieldValue) {

        // Find the class containing the field to set
        Class<?> targetClass = objectInstance.getClass();
        while (targetClass != null) {
            for (Field field : targetClass.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(fieldName)) {
                    try {
                        boolean isAccessible = field.isAccessible();
                        try {
                            field.setAccessible(true);
                            field.set(objectInstance, fieldValue);
                        } finally {
                            field.setAccessible(isAccessible);
                        }
                    } catch (Exception e) {
                        // It probably means the Java security manager has been configured to prevent accessing private fields.
                        throw new RuntimeException("Failed to set field [" + fieldName + "] in instance of ["
                                + objectInstance.getClass().getName() + "]. The Java Security Manager has "
                                + "probably been configured to prevent settting private field values. XWiki requires "
                                + "this ability to work.", e);
                    }
                    return;
                }
            }
            targetClass = targetClass.getSuperclass();
        }
    }

    /**
     * Extract the last generic type from the passed Type.
     * For example private List<A, B> field would return the 'B' class.
     *
     * @param type the type from which to extract the generic type
     * @return the type of the last generic type or null if the field doesn't have a generic type
     */
    public static Type getLastTypeGenericArgument(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type[] types = pType.getActualTypeArguments();
            if (types.length > 0) {
                return types[types.length - 1];
            }
        }

        return null;
    }

    /**
     * Get the first found annotation with the provided class directly assigned to the provided AnnotatedElement.
     *
     *
     * @param <T> the type of the annotation
     * @param annotationClass the annotation class
     * @param element the class on which annotation are assigned
     * @return the found annotation or null if there is none
     */
    public static <T extends Annotation> T getDirectAnnotation(Class<T> annotationClass, AnnotatedElement element) {
        // Handle interfaces directly declared in the passed component class
        for (Annotation annotation : element.getDeclaredAnnotations()) {
            if (annotation.annotationType() == annotationClass) {
                return (T) annotation;
            }
        }

        return null;
    }
}
