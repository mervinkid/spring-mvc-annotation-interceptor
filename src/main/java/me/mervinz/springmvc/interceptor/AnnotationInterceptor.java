package me.mervinz.springmvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

public interface AnnotationInterceptor<T extends Annotation> extends HandlerInterceptor {

    boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 T annotation) throws Exception;

    boolean postAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                  Object handler, ModelAndView modelAndView, T annotation) throws Exception;
}
