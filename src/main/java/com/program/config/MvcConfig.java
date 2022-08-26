package com.program.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
        /*
        thymeleaf的模板浏览器不能直接访问
        在controller中需要一个login的方法
            @RequestMapping("login")
            public String login(){
                return "login";
            }
        但是每添加一个页面就需要在controller中添加一个方法，很不方便
        所以在config类中单独配置
        */
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {

            //ViewController 请求路径
            //ViewName 跳转的视图
            registry.addViewController("login").setViewName("login");
            registry.addViewController("register").setViewName("regist");
            registry.addViewController("addEmp").setViewName("addEmp");
        }
}
