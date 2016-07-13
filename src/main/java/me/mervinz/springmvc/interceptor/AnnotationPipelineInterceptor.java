package me.mervinz.springmvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * Interface provide annotation interceptor with pipeline support.
 *
 * @author Mervin
 */
@SuppressWarnings("unused")
public interface AnnotationPipelineInterceptor extends HandlerInterceptor {

    /**
     * Add pipeline handlers
     *
     * @param handlers list of handler instances
     */
    void addPipelineHandlers(List<AnnotationHandler> handlers);

    /**
     * Add pipeline handler
     *
     * @param handler handler instance
     */
    void addPipelineHandler(AnnotationHandler handler);

    /**
     * Setup extension logic for original handler.
     * These code will run before pipeline handler.
     *
     * @param extendOriginalHandler instance of ExtendOriginalHandler
     */
    void setupExtendOriginalHandler(ExtendOriginalHandler extendOriginalHandler);
}
