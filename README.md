# annotation-interceptor

![Java](https://img.shields.io/badge/java-1.8-orange.svg?style=flat)
![Maven](https://img.shields.io/badge/maven-3.3.9-orange.svg?style=flat)
![Build](https://img.shields.io/badge/build-passing-brightgreen.svg?style=flat)
![Release](https://img.shields.io/badge/release-1.0.0-blue.svg?style=flat)
![License MIT](https://img.shields.io/badge/license-MIT-lightgray.svg?style=flat&maxAge=2592000)

Annotation generic based java web interceptor for [Spring MVC](https://spring.io).

## Installation

Download [the latest JAR](http://central.maven.org/maven2/me/mervinz/spring-mvc-annotation-interceptor/1.0.0/spring-mvc-annotation-interceptor-1.0.0.jar) or using Maven:

```xml
<dependency>
    <groupId>me.mervinz</groupId>
    <artifactId>spring-mvc-annotation-interceptor</artifactId>
    <version>1.0.0</version>
</dependency>
```

or **Gradle**:
    
```groovy
compile group: 'me.mervinz', name: 'spring-mvc-annotation-interceptor', version: '1.0.0'
```

## Usage
    
1. Create a java annotation which you want the interceptor to handle. For example `RequireToken`.

```java
package me.mervinz.demo.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface RequireToken {
}
```
    
2. Create your own interceptor class like `TokenInterceptorAdapter` extend by abstract class `me.mervinz.springmvc.interceptor.AnnotationInterceptorAdapter` to intercept the annotation `RequireToken`.
3. Override the method `preAnnotationHandler` and `postAnnotationHandler`.

**Sample**:

```java
package me.mervinz.demo.interceptor;

import me.mervinz.demo.annotation.RequireLogin;
import me.mervinz.springmvc.interceptor.AnnotationInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class TokenInterceptorAdapter extends AnnotationInterceptorAdapter<RequireToken> {

    // This is the replacement for original method 'preHandler'
    @Override
    public boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler,
                                        RequireLogin annotation) throws Exception {
        // put your code here ...
        return false;
    }
    
    // This is the replacement for original method 'postHandler'
    @Override
    public void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                        Object handler, ModelAndView modelAndView, T annotation) throws Exception {
                                        
        // put your code here ...
    }
}
```
    
4. Register the interceptor to you spring mvc configuration.

For **JavaConfig**:
    
```java
package me.mervinz.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import me.mervinz.demo.interceptor.TokenInterceptorAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "me.mervinz.demo")
public class AppConfig extends WebMvcConfigurerAdapter {

    /**
        * Add interceptors
        */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    
        // Register your interceptor
        registry.addInterceptor(new TokenInterceptorAdapter());
        
        // Or
        registry.addInterceptor(new AnnotationInterceptorAdapter<RequireToken>() {
        
            @Override
            public boolean preAnnotationHandler(HttpServletRequest request, HttpServletResponse response, Object handler, RequireLogin annotation) throws Exception {
            
                // Put your code here ...
                return false;
            }
            
            @Override
            public void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                                Object handler, ModelAndView modelAndView, T annotation) throws Exception {
                                                
                // Put your code here ...
            }
        });
        
        // Registry other interceptors
    }
    
}
```
    
For **XML**:

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:context="http://www.springframework.org/schema/context"
        xmlns:mvc="http://www.springframework.org/schema/mvc"
        xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">
    
    <context:component-scan base-package="me.mervinz.demo"/>
    
    <mvc:interceptors>
        <mvc:interceptor>
            <bean class="me.mervinz.demo.interceptor.TokenInterceptorAdapter"/>
        </mvc:interceptor>
    </mvc:interceptors>
    
</beans>
```
    
## Dependencies

For detail see `pom.xml`.

## Contributing

1. Fork it.
2. Create your feature branch. (`$ git checkout feature/my-feature-branch`)
3. Commit your changes. (`$ git commit -am 'What feature I just added.'`)
4. Push to the branch. (`$ git push origin feature/my-feature-branch`)
5. Create a new Pull Request

## Authors

[@Mervin](https://github.com/mervinkid) 

## License

The MIT License (MIT). For detail see [LICENSE](LICENSE).
