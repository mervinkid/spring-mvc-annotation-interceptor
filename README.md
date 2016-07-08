# annotation-interceptor

![Java](https://img.shields.io/badge/java-1.8-orange.svg?style=flat)
![Maven](https://img.shields.io/badge/maven-3.3.9-orange.svg?style=flat)
![Build](https://img.shields.io/badge/build-passing-brightgreen.svg?style=flat)
![Release](https://img.shields.io/badge/release-none-blue.svg?style=flat)
![License MIT](https://img.shields.io/badge/license-MIT-lightgray.svg?style=flat&maxAge=2592000)

A annotation generic based java web interceptor for [Spring MVC](https://spring.io).

## Usage


1. Add `annotation-inter` to dependencies of your project. 
2. Create a java annotation which you want to handle. For example `RequireToken`.
3. Create your own class like `TokenInterceptorAdapter` extend by `me.mervinz.springmvc.interceptor.AnnotationInterceptorAdapter` to handle the annotation `RequireToken`.
4. Override the function `preAnnotationHandler` and `postAnnotationHandler`. Then write your code.

    ```
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
            // Put your code here ...
            return false;
        }
        
        // This is the replacement for original method 'postHandler'
        @Override
        public void postAnnotationHandler(HttpServletRequest request, HttpServletResponse response,
                                          Object handler, ModelAndView modelAndView, T annotation) throws Exception {
                                          
            // Put your code here ...
        }
    }
    ```
5. Register the interceptor to you spring mvc configuration.

    For **JavaConfig**:
    
    ```
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
            registry.addInterceptor(new TokenInterceptorAdapter());
            // Registry other interceptors
        }
        
    }
    ```
    
    For **xml**:
    
    ```
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/*"/>
            <bean class="me.mervinz.demo.interceptor.TokenInterceptorAdapter"/>
        </mvc:interceptor>
    </mvc:interceptors>
    ```
    
## Dependencies

For detail see `pom.xml`.

## Contributing

1. Fork it.
2. Creature your feature branch. (`$ git checkout feature/my-feature-branch`)
3. Commit your changes. (`$ git commit -am 'What feature I just added.'`)
4. Push to the branch. (`$ git push origin feature/my-feature-branch`)
5. Create a new Pull Request

## Authors

[@Mervin](https://github.com/mofei2816) 

## License

The MIT License (MIT). For detail see [LICENSE](LICENSE).