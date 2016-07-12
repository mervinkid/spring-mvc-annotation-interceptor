package me.mervinz.springmvc.interceptor.adapter;

import me.mervinz.springmvc.interceptor.AnnotationHandler;
import me.mervinz.springmvc.interceptor.MultipleAnnotationSupport;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Mervin
 */
@SuppressWarnings("unused")
public final class MultipleAnnotationInterceptorAdapter
        extends MultipleAnnotationSupportAdapter implements MultipleAnnotationSupport {

    private Map<Class, AnnotationHandler> handlerMap = new HashMap<>();

    /**
     * Override original post handler.
     * Run code from extend handler at first if it exist.
     */
    @Override
    public final void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // This collection used for activated annotation management
        Set<Annotation> activatedAnnotationSet = new HashSet<>();

        // Add class annotations to collection
        Arrays.asList(handlerMethod.getClass().getAnnotations())
                .forEach(activatedAnnotationSet::add);

        // Add method annotations to collection
        Arrays.asList(handlerMethod.getMethod().getAnnotations())
                .forEach(activatedAnnotationSet::add);

        // Loop through activated annotations
        for (Annotation annotation : activatedAnnotationSet) {
            AnnotationHandler annotationHandler = this.handlerMap.get(annotation.getClass());
            if (annotationHandler != null) {
                annotationHandler.postAnnotationHandler(request, response, handler, modelAndView, annotation);
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

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // This collection used for activated annotation management
        Set<Annotation> activatedAnnotationSet = new HashSet<>();

        // Add class annotations to collection
        Arrays.asList(handlerMethod.getClass().getAnnotations())
                .forEach(activatedAnnotationSet::add);

        // Add method annotations to collection
        Arrays.asList(handlerMethod.getMethod().getAnnotations())
                .forEach(activatedAnnotationSet::add);

        // Loop through activated annotations
        for (Annotation annotation : activatedAnnotationSet) {
            AnnotationHandler annotationHandler = this.handlerMap.get(annotation.getClass());
            if (annotationHandler != null) {
                if (!annotationHandler.preAnnotationHandler(request, response, handler, annotation)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Add handler
     *
     * @param handler handler instance
     */
    @Override
    public void addHandler(AnnotationHandler handler) {
        super.addHandler(handler);
        this.updateHandlerMap();
    }

    /**
     * Add handlers
     *
     * @param handlers list of handler instances
     */
    @Override
    public void addHandlers(List<AnnotationHandler> handlers) {
        super.addHandlers(handlers);
        this.updateHandlerMap();
    }

    /**
     * Synchronize between handlers and handlerMap
     */
    private void updateHandlerMap() {
        this.handlers.stream()
                .filter(p -> this.handlerMap.get(p.getHandlerAnnotationClass()) == null)
                .forEach(p -> this.handlerMap.put(p.getHandlerAnnotationClass(), p));
    }
}
