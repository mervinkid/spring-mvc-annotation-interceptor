package me.mervinz.springmvc.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AnnotationInterceptorAdapter<T extends Annotation>
        extends HandlerInterceptorAdapter implements AnnotationInterceptor<T> {

    private Class<T> annotationClass;

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
                    break;
                }
            }
            if (cursor.getSuperclass() == AnnotationInterceptorAdapter.class) {
                break;
            }
            cursor = cursor.getSuperclass();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (this.annotationClass == null) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        T methodAnnotation = handlerMethod.getMethodAnnotation(this.annotationClass);
        T classAnnotation = handlerMethod.getClass().getAnnotation(this.annotationClass);

        return !(methodAnnotation != null || classAnnotation != null) ||
                preAnnotationHandler(request, response, handler, methodAnnotation != null ? methodAnnotation : classAnnotation);
    }

    public abstract boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler,
                                        T annotation) throws Exception;
}
