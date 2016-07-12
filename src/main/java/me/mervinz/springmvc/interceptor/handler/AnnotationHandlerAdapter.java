package me.mervinz.springmvc.interceptor.handler;

import me.mervinz.springmvc.interceptor.AnnotationHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author Mervin
 */
@SuppressWarnings("unused")
public abstract class AnnotationHandlerAdapter implements AnnotationHandler {

    private Class<Annotation> annotationClass;

    /**
     * Constructor with annotation type
     *
     * @param annotationClass specified annotation type
     */
    public AnnotationHandlerAdapter(Class<Annotation> annotationClass) {
        if (annotationClass != null) {
            this.annotationClass = annotationClass;
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
    public boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler,
                                        Annotation annotation) throws Exception {
        return true;
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler,
                                      ModelAndView modelAndView, Annotation annotation) throws Exception {

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
        AnnotationHandlerAdapter that = (AnnotationHandlerAdapter) o;
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
