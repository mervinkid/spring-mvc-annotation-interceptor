package me.mervinz.springmvc.interceptor.adapter;

import me.mervinz.springmvc.interceptor.AnnotationHandler;
import me.mervinz.springmvc.interceptor.ExtendOriginalHandler;
import me.mervinz.springmvc.interceptor.MultipleAnnotationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for multiple annotation support.
 * This is the base for other interceptor.
 *
 * @author Mervin
 */
@SuppressWarnings("unused")
public abstract class MultipleAnnotationSupportAdapter
        extends HandlerInterceptorAdapter implements MultipleAnnotationSupport {

    protected List<AnnotationHandler> handlers = new ArrayList<>();

    protected ExtendOriginalHandler extendOriginalHandler;

    /**
     * Default constructor
     */
    public MultipleAnnotationSupportAdapter() {
    }

    /**
     * Constructor with list of handlers
     *
     * @param handlers pipeline handlers
     */
    public MultipleAnnotationSupportAdapter(List<AnnotationHandler> handlers) {
        this.handlers.clear();
        if (handlers != null) {
            this.addHandlers(handlers);
        }
    }

    /**
     * Constructor with array of handlers
     *
     * @param handlers pipeline handlers
     */
    public MultipleAnnotationSupportAdapter(AnnotationHandler... handlers) {
        this.handlers.clear();
        if (handlers.length > 0) {
            this.addHandlers(Arrays.asList(handlers));
        }
    }

    /**
     * Setup extension logic for original handler.
     * These code will run before pipe line handler.
     *
     * @param extendOriginalHandler instance of ExtendOriginalHandler
     * @return current handler instance
     */
    public final MultipleAnnotationSupport setExtendOriginalHandler(ExtendOriginalHandler extendOriginalHandler) {
        this.extendOriginalHandler = extendOriginalHandler;
        return this;
    }

    /**
     * Add handlers
     *
     * @param handlers list of handler instances
     */
    public MultipleAnnotationSupport addHandlers(List<AnnotationHandler> handlers) {
        handlers.stream()
                .filter(p -> p.getHandlerAnnotationClass() != null)
                .forEach(p -> this.handlers.add(p));
        return this;
    }

    /**
     * Add handler
     *
     * @param handler handler instance
     */
    public MultipleAnnotationSupport addHandler(AnnotationHandler handler) {
        if (handler.getHandlerAnnotationClass() != null) {
            this.handlers.add(handler);
        }
        return this;
    }

    /**
     * Get handlers
     *
     * @return collection for annotation handler
     */
    @Override
    public final List<AnnotationHandler> getHandlers() {
        return this.handlers;
    }
}
