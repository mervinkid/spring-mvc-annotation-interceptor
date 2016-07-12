package me.mervinz.springmvc.interceptor.adapter;

import me.mervinz.springmvc.interceptor.AnnotationHandler;
import me.mervinz.springmvc.interceptor.MultipleAnnotationSupport;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @author Mervin
 */
@SuppressWarnings("unused")
public final class AnnotationPipelineInterceptorAdapter
        extends MultipleAnnotationSupportAdapter implements MultipleAnnotationSupport {

    /**
     * Override original post handler.
     * Run code from extend handler at first if it exist.
     */
    @Override
    public final void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 ModelAndView modelAndView) throws Exception {

        if (this.extendOriginalHandler != null) {
            this.extendOriginalHandler.extendPostHandler(request, response, handler, modelAndView);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        for (int i = this.handlers.size() - 1; i >= 0; i--) {

            AnnotationHandler pipelineHandler = this.handlers.get(i);

            if (pipelineHandler == null) {
                continue;
            }

            Class specifiedAnnotationClass = pipelineHandler.getHandlerAnnotationClass();

            Annotation specificAnnotation = this.getAnnotationFormHandlerMethod(handlerMethod, specifiedAnnotationClass);

            if (specificAnnotation != null) {
                pipelineHandler.postAnnotationHandler(request, response, handler, modelAndView, specificAnnotation);
            }
        }

        super.postHandle(request, response, handler, modelAndView);

    }

    /**
     * Override original pre handler.
     * Run code from extend handler at first if it exist.
     */
    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (this.extendOriginalHandler != null) {
            this.extendOriginalHandler.extendPreHandler(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        for (AnnotationHandler pipelineHandler : this.handlers) {

            Class specifiedAnnotationClass = pipelineHandler.getHandlerAnnotationClass();

            Annotation specificAnnotation = this.getAnnotationFormHandlerMethod(handlerMethod, specifiedAnnotationClass);

            if (specificAnnotation != null) {
                if (!pipelineHandler.preAnnotationHandler(request, response, handler, specificAnnotation)) {
                    return false;
                }
            }

        }

        return super.preHandle(request, response, handler);
    }

    /**
     * Get specified annotation from handler method
     *
     * @param handlerMethod   instance of handler method
     * @param annotationClass specified annotation class
     * @return instance of specified annotation
     */
    private Annotation getAnnotationFormHandlerMethod(HandlerMethod handlerMethod, Class annotationClass) {

        Annotation methodAnnotation = handlerMethod.getMethodAnnotation(annotationClass);

        Annotation classAnnotation = handlerMethod.getClass().getAnnotation(annotationClass);

        return methodAnnotation != null ? methodAnnotation : classAnnotation;
    }

}
