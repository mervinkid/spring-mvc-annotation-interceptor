package me.mervinz.springmvc.interceptor.adapter;

import me.mervinz.springmvc.interceptor.GenericAnnotationInterceptor;
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

/**
 * Abstract class for AnnotationInterceptor implement
 *
 * @param <T> annotation generic
 * @author Mervin
 */
@SuppressWarnings("unused")
public abstract class GenericAnnotationInterceptorAdapter<T extends Annotation>
        extends HandlerInterceptorAdapter implements GenericAnnotationInterceptor<T> {

    private Class<T> annotationClass;   // Annotation class

    Log logger = LogFactory.getLog(this.getClass());    // Logging

    /**
     * Constructor
     * Get specified annotation type from class parameter.
     */
    @SuppressWarnings("unchecked")
    public GenericAnnotationInterceptorAdapter() {
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
            if (cursor.getSuperclass() == GenericAnnotationInterceptorAdapter.class) {
                logger.error("Interceptor init fail. No valid class parameter found.");
                break;
            }
            cursor = cursor.getSuperclass();
        }
    }

    /**
     * Get annotation from handler
     *
     * @param handler handler to execute
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

    /**
     * Get annotation class
     *
     * @return type of annotation
     */
    public Class<T> getAnnotationClass() {
        return annotationClass;
    }

    /**
     * Override original pre handler.
     * Try to get annotation from handler method or class.
     * If the annotation exist, call the replacement handler 'preAnnotationHandler'.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     * @throws Exception in case of errors
     */
    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Call extend method
        this.extendPreHandler(request, response, handler);

        T annotation = this.getAnnotation(handler);

        return annotation == null || preAnnotationHandler(request, response, handler, annotation);
    }

    /**
     * Override original post handler.
     * Try to get annotation from handler method or class.
     * If the annotation exist, call the replacement handler 'postAnnotationHandler'.
     *
     * @param request      current HTTP request
     * @param response     current HTTP response
     * @param handler      handler (or {@link HandlerMethod}) that started async
     *                     execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     *                     (can also be {@code null})
     * @throws Exception in case of errors
     */
    @Override
    public final void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 ModelAndView modelAndView) throws Exception {

        // Call extend method
        this.extendPostHandler(request, response, handler, modelAndView);

        T annotation = this.getAnnotation(handler);

        if (annotation != null) {
            postAnnotationHandler(request, response, handler, modelAndView, annotation);
        }
    }

    /**
     * Extension for original post handler
     * The original post handler will run these code before it's own code.
     *
     * @param request      current HTTP request
     * @param response     current HTTP response
     * @param handler      handler (or {@link HandlerMethod}) that started async
     *                     execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     *                     (can also be {@code null})
     * @throws Exception in case of errors
     */
    @Override
    public void extendPostHandler(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * Extension for original pre handler
     * The original pre handler will run these code before it's own code.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     * @throws Exception in case of errors
     */
    @Override
    public boolean extendPreHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView, T annotation) throws Exception {

    }

    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler, T annotation) throws Exception {
        return true;
    }
}
