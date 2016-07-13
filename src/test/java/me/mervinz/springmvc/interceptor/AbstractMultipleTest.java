package me.mervinz.springmvc.interceptor;

import me.mervinz.springmvc.interceptor.base.AnnotationA;
import me.mervinz.springmvc.interceptor.base.AnnotationB;
import me.mervinz.springmvc.interceptor.handler.AnnotationHandlerAdapter;
import me.mervinz.springmvc.interceptor.handler.GenericAnnotationHandlerAdapter;
import org.junit.Before;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * Base for multiple annotation test
 *
 * @author Mervin
 */
public abstract class AbstractMultipleTest {

    protected AnnotationHandler handlerA, handlerB;

    // prepare flags
    protected boolean flags[] = {
            false,  // 0: handlerA - preAnnotationHandler
            false,  // 1: handlerA - postAnnotationHandler
            false,  // 2: handlerB - preAnnotationHandler
            false   // 3: handlerB - postAnnotationHandler
    };

    /**
     * Init for unit test
     *
     * @throws Exception
     */
    @Before
    public void init() throws Exception {
        // prepare handlers

        // handlerA: instance of AnnotationHandlerAdapter
        this.handlerA = new AnnotationHandlerAdapter(AnnotationA.class) {

            @Override
            public boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                                Object handler, Annotation annotation) throws Exception {
                flags[0] = true;
                return super.preAnnotationHandler(request, response, handler, annotation);
            }

            @Override
            public void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                              Object handler, ModelAndView modelAndView, Annotation annotation) throws Exception {
                flags[1] = true;
                super.postAnnotationHandler(request, response, handler, modelAndView, annotation);
            }
        };

        // handlerB: instance of GenericAnnotationHandlerAdapter
        this.handlerB = new GenericAnnotationHandlerAdapter<AnnotationB>() {
            @Override
            public boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                                Object handler, Annotation annotation) throws Exception {
                flags[2] = true;
                return super.preAnnotationHandler(request, response, handler, annotation);
            }

            @Override
            public void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                              Object handler, ModelAndView modelAndView, Annotation annotation) throws Exception {
                flags[3] = true;
                super.postAnnotationHandler(request, response, handler, modelAndView, annotation);
            }
        };
    }
}
