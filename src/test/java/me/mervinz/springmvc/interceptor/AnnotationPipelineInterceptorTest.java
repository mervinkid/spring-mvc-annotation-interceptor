package me.mervinz.springmvc.interceptor;

import me.mervinz.springmvc.interceptor.adapter.AnnotationPipelineInterceptorAdapter;
import me.mervinz.springmvc.interceptor.base.Controller;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

/**
 * Test for annotation pipeline interceptor
 *
 * @author Mervin
 */
public final class AnnotationPipelineInterceptorTest extends AbstractMultipleTest {

    /**
     * Run test
     *
     * @throws Exception
     */
    @Test
    public void doTest() throws Exception {

        // prepare interceptor
        MultipleAnnotationSupport interceptor = new AnnotationPipelineInterceptorAdapter();
        interceptor.addHandler(handlerA)
                .addHandler(handlerB);

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

        // 1. validate method A
        interceptor.preHandle(null, null, handlerMethodA);
        Assert.assertTrue(flags[0]);
        Assert.assertFalse(flags[2]);
        interceptor.postHandle(null, null, handlerMethodA, null);
        Assert.assertTrue(flags[1]);
        Assert.assertFalse(flags[3]);

        // reset flag
        flags[0] = flags[1] = flags[2] = flags[3] = false;

        // 2. validate method B
        interceptor.preHandle(null, null, handlerMethodB);
        Assert.assertFalse(flags[0]);
        Assert.assertTrue(flags[2]);
        interceptor.postHandle(null, null, handlerMethodB, null);
        Assert.assertFalse(flags[1]);
        Assert.assertTrue(flags[3]);

        // Reset flag
        flags[0] = flags[1] = flags[2] = flags[3] = false;

        // 3. validate methodBoth
        interceptor.preHandle(null, null, handlerMethodBoth);
        Assert.assertTrue(flags[0]);
        Assert.assertTrue(flags[2]);
        interceptor.postHandle(null, null, handlerMethodBoth, null);
        Assert.assertTrue(flags[1]);
        Assert.assertTrue(flags[3]);
    }
}
