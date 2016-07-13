package me.mervinz.springmvc.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * Interface provide interceptor with generic support
 *
 * @param <T> annotation generic
 * @author Mervin
 */
@SuppressWarnings("unused")
public interface GenericAnnotationInterceptor<T extends Annotation> extends HandlerInterceptor {

    /**
     * Get annotation class
     *
     * @return type of annotation
     */
    Class<T> getAnnotationClass();

    /**
     * Replacement for original pre handler
     *
     * @param request    current HTTP request (original param)
     * @param response   current HTTP response (original param)
     * @param handler    chosen handler to execute, for type and/or instance evaluation (original param)
     * @param annotation current annotation
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     * @throws Exception in case of errors
     */
    boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 T annotation) throws Exception;

    /**
     * Replacement for original post handler
     *
     * @param request      current HTTP request (original param)
     * @param response     current HTTP response (origin param)
     * @param handler      handler (or {@link HandlerMethod}) that started async
     *                     execution, for type and/or instance examination (original param)
     * @param modelAndView the {@code ModelAndView} that the handler returned
     *                     (can also be {@code null}) (original param)
     * @param annotation   current annotation
     * @throws Exception in case of errors
     */
    void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                               Object handler, ModelAndView modelAndView, T annotation) throws Exception;

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
    boolean extendPreHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

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
    void extendPostHandler(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception;
}
