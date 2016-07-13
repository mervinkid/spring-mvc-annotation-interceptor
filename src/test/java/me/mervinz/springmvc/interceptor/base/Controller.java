package me.mervinz.springmvc.interceptor.base;

public final class Controller {

    @AnnotationA
    public void testMethodA() {
    }

    @AnnotationB
    public void testMethodB() {
    }

    @AnnotationA
    @AnnotationB
    public void testMethodBoth() {
    }
}
