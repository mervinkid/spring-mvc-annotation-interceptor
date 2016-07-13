package me.mervinz.springmvc.interceptor;

import me.mervinz.springmvc.interceptor.adapter.GenericAnnotationInterceptorAdapter;
import me.mervinz.springmvc.interceptor.base.AnnotationA;
import me.mervinz.springmvc.interceptor.base.Controller;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Test for GenericAnnotationInterceptor
 *
 * @author Mervin
 */
public final class GenericAnnotationInterceptorTest {

    private GenericAnnotationInterceptor interceptor;

    private boolean[] flag = {false, false, false, false};

    /**
     * Init for test
     */
    @Before
    public void init() {
        // prepare interceptor
        this.interceptor = new GenericAnnotationInterceptorAdapter<AnnotationA>() {
            @Override
            public boolean extendPreHandler(HttpServletRequest request, HttpServletResponse response,
                                            Object handler) throws Exception {
                flag[0] = true;
                return super.extendPreHandler(request, response, handler);
            }

            @Override
            public boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                                Object handler, AnnotationA annotation) throws Exception {
                flag[1] = true;
                return super.preAnnotationHandler(request, response, handler, annotation);
            }

            @Override
            public void extendPostHandler(HttpServletRequest request, HttpServletResponse response,
                                          Object handler, ModelAndView modelAndView) throws Exception {
                flag[2] = true;
                super.extendPostHandler(request, response, handler, modelAndView);
            }

            @Override
            public void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                              Object handler, ModelAndView modelAndView, AnnotationA annotation) throws Exception {
                flag[3] = true;
                super.postAnnotationHandler(request, response, handler, modelAndView, annotation);
            }

        };
    }

    /**
     * Run unit test
     *
     * @throws Exception
     */
    @Test
    public void doTest() throws Exception {

        // validate interceptor
        Assert.assertEquals(interceptor.getAnnotationClass(), AnnotationA.class);

        // prepare and validate test methods
        Method methodA = Controller.class.getMethod("testMethodA");
        Method methodB = Controller.class.getMethod("testMethodB");
        Method methodBoth = Controller.class.getMethod("testMethodBoth");
        Assert.assertNotNull(methodA);
        Assert.assertNotNull(methodB);
        Assert.assertNotNull(methodBoth);

        // prepare handler methods
        Object o = new Object();
        HandlerMethod handlerMethodA = new HandlerMethod(o, methodA);
        HandlerMethod handlerMethodB = new HandlerMethod(o, methodB);
        HandlerMethod handlerMethodBoth = new HandlerMethod(o, methodBoth);

        // reset flag
        flag[0] = flag[1] = flag[2] = flag[3] = false;

        // 1. validate method A
        interceptor.preHandle(null, null, handlerMethodA);
        Assert.assertTrue(flag[0]);
        Assert.assertTrue(flag[1]);
        interceptor.postHandle(null, null, handlerMethodA, null);
        Assert.assertTrue(flag[2]);
        Assert.assertTrue(flag[3]);

        // reset flag
        flag[0] = flag[1] = flag[2] = flag[3] = false;

        // 2. validate method B
        interceptor.preHandle(null, null, handlerMethodB);
        Assert.assertTrue(flag[0]);
        Assert.assertFalse(flag[1]);
        interceptor.postHandle(null, null, handlerMethodB, null);
        Assert.assertTrue(flag[2]);
        Assert.assertFalse(flag[3]);

        // Reset flag
        flag[0] = flag[1] = flag[2] = flag[3] = false;

        // 3. validate methodBoth
        interceptor.preHandle(null, null, handlerMethodBoth);
        Assert.assertTrue(flag[0]);
        Assert.assertTrue(flag[1]);
        interceptor.postHandle(null, null, handlerMethodBoth, null);
        Assert.assertTrue(flag[2]);
        Assert.assertTrue(flag[3]);
    }

}
