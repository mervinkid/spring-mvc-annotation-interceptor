package me.mervinz.springmvc.interceptor.handler;

import me.mervinz.springmvc.interceptor.AnnotationHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Generic based handler implementation for interceptor with multiple annotation support.
 *
 * @param <T> annotation generic
 * @author Mervin
 */
@SuppressWarnings({"unused", "unchecked"})
public abstract class GenericAnnotationHandlerAdapter<T extends Annotation> implements AnnotationHandler {

    private Class<T> annotationClass;

    Log logger = LogFactory.getLog(this.getClass());

    /**
     * Constructor
     * Get specified annotation type from class parameter.
     */
    public GenericAnnotationHandlerAdapter() {
        Class cursor = this.getClass();
        while (true) {
            Type sType = cursor.getGenericSuperclass();
            if (sType instanceof ParameterizedType) {
                Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
                Type mainGeneric = generics[0];
                if (!mainGeneric.getTypeName().equals("T")) {
                    this.annotationClass = (Class<T>) generics[0];
                    logger.debug(String.format("Handler for annotation [%s] inited.", this.annotationClass.getName()));
                    break;
                }
            }
            if (cursor.getSuperclass() == GenericAnnotationHandlerAdapter.class) {
                logger.error("Handler init fail. No valid class parameter found.");
                break;
            }
            cursor = cursor.getSuperclass();
        }
    }

    /**
     * Get annotation class
     *
     * @return annotation class
     */
    @Override
    public Class getHandlerAnnotationClass() {
        return this.annotationClass;
    }

    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Annotation annotation) throws Exception {
        return true;
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView, Annotation annotation) throws Exception {

    }

    /**
     * Override the equals method.
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericAnnotationHandlerAdapter<?> that = (GenericAnnotationHandlerAdapter<?>) o;
        return Objects.equals(annotationClass, that.annotationClass);
    }

    /**
     * Override ths hashCode method.
     *
     * @return the hash code for current handler
     */
    @Override
    public int hashCode() {
        return Objects.hash(annotationClass);
    }
}
