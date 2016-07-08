package me.mervinz.springmvc.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AnnotationInterceptorAdapter<T extends Annotation>
        extends HandlerInterceptorAdapter implements AnnotationInterceptor<T> {

    private Class<T> annotationClass;   // Annotation class

    Log logger = LogFactory.getLog(this.getClass());    // Logging

    /**
     * Constructor
     */
    @SuppressWarnings("unchecked")
    public AnnotationInterceptorAdapter() {
        Class cursor = this.getClass();
        while (true) {
            Type sType = cursor.getGenericSuperclass();
            if (sType instanceof ParameterizedType) {
                Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
                Type mainGeneric = generics[0];
                if (!mainGeneric.getTypeName().equals("T")) {
                    this.annotationClass = (Class<T>) generics[0];
                    logger.debug(String.format("Interceptor for annotation [%s] inited.", this.annotationClass.getName()));
                    break;
                }
            }
            if (cursor.getSuperclass() == AnnotationInterceptorAdapter.class) {
                break;
            }
            cursor = cursor.getSuperclass();
        }
    }

    /**
     * Get annotation from handler
     *
     * @param handler Handler
     * @return annotation
     */
    private T getAnnotation(Object handler) {
        if (this.annotationClass == null) {
            return null;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        T methodAnnotation = handlerMethod.getMethodAnnotation(this.annotationClass);
        T classAnnotation = handlerMethod.getClass().getAnnotation(this.annotationClass);

        return methodAnnotation != null ? methodAnnotation : classAnnotation;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        T annotation = this.getAnnotation(handler);

        return annotation == null || preAnnotationHandler(request, response, handler, annotation);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        T annotation = this.getAnnotation(handler);

        if (annotation != null) {
            postAnnotationHandler(request, response, handler, modelAndView, annotation);
        }
    }

}
