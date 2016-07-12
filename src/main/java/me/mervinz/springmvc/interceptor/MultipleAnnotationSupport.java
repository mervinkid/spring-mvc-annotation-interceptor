package me.mervinz.springmvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * @author Mervin
 */
@SuppressWarnings("unused")
public interface MultipleAnnotationSupport extends HandlerInterceptor {

    /**
     * Setup extension logic for original handler.
     * These code will run before pipeline handler.
     *
     * @param extendOriginalHandler instance of ExtendOriginalHandler
     */
    void setExtendOriginalHandler(ExtendOriginalHandler extendOriginalHandler);

    /**
     * Add handler
     *
     * @param handler handler instance
     */
    void addHandler(AnnotationHandler handler);

    /**
     * Add handlers
     *
     * @param handlers list of handler instances
     */
    void addHandlers(List<AnnotationHandler> handlers);

}
