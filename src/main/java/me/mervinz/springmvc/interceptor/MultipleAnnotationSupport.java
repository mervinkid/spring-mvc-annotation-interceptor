package me.mervinz.springmvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * Interface provide interceptor with multiple annotation support
 *
 * @author Mervin
 */
@SuppressWarnings("unused")
public interface MultipleAnnotationSupport extends HandlerInterceptor {

    /**
     * Setup extension logic for original handler.
     * These code will run before pipeline handler.
     *
     * @param extendOriginalHandler instance of ExtendOriginalHandler
     * @return current handler instance
     */
    MultipleAnnotationSupport setExtendOriginalHandler(ExtendOriginalHandler extendOriginalHandler);

    /**
     * Add handler
     *
     * @param handler handler instance
     * @return current handler instance
     */
    MultipleAnnotationSupport addHandler(AnnotationHandler handler);

    /**
     * Add handlers
     *
     * @param handlers list of handler instances
     * @return current handler instance
     */
    MultipleAnnotationSupport addHandlers(List<AnnotationHandler> handlers);

    /**
     * Get handlers
     *
     * @return collection for annotation handler
     */
    List<AnnotationHandler> getHandlers();
}
