package com.program.controller;

import com.program.entity.User;
import com.program.service.UserService;
import com.program.utils.VerifyCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequestMapping("user")
@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 安全退出
     * @return
     */
    @RequestMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();   //session失效
        return "redirect:login";
    }
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     * 由于密码已经被加密，如果继续使用明文的密码去找是肯定找不到的
     * 所以就需要先去查找用户名
     * 然后再将用户输入的密码加密，再去数据库中对比
     */
    @RequestMapping("login")
    public String login(String username,String password,HttpSession session){
        log.debug("Username for this login: {}",username);
        log.debug("password for this login: {}",password);

        try {
            //1.调用service进行登录
            User user = userService.login(username,password);
            //如果代码执行到这里说明用户名和密码都正确
            //保存用户信息
            session.setAttribute("user",user);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login";//登录失败回到登录界面
        }
        //不能直接跳转页面，因为没有数据
        //应该跳转到查询所有员工信息controller的路径，再由员工信息跳转到emplist.html
        return "redirect:/employee/lists";//登录成功之后,跳转到查询所有员工信息控制器路径
    }

    /**
     * 用户注册
     * @return
     *
     * SpringMVC和springboot在收集参数时，
     * 要求html表单的input的属性名与方法中参数的变量名一致
     * 就会自动完成赋值
     */
    @RequestMapping("register")
    public String register(User user, String code,HttpSession session){
        log.debug("用户名: {},真是姓名: {},密码: {},性别: {},",user.getUsername(),user.getRealname(),user.getPassword(),user.getGender());
        log.debug("用户输入验证码: {}",code);

        try {
            //1.判断用户输入验证码和session中验证码是否一致
            String sessionCode = session.getAttribute("code").toString();
            if(!sessionCode.equalsIgnoreCase(code))
                throw new RuntimeException("Incorrect verification code!");

            //2.注册用户
            userService.register(user);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "redirect:/register"; //注册失败回到注册
        }

        //跳转到login页面
        return "redirect:/login";
    }

    /**
     *生成验证码
     */
    @RequestMapping("generateImageCode")
    //想要的是图片，不能跳转，所以void
    public void generateImageCode(
            HttpSession session, HttpServletResponse response) throws IOException {
        //1.生成随机数
        String code = VerifyCodeUtils.generateVerifyCode(4);

        //2.保存到session作用域
        //  request占用资源比较少，缺乏持续性
        //  这一次响应完，可能下一次request的作用域就没了
        //  而且必须在用户输入后从session中取出对比是否一致
        session.setAttribute("code",code);

        //3.更具随机数生成图片
        //4.通过response响应图片
        //5.设置响应类型
        response.setContentType("img/png");
        ServletOutputStream os = response.getOutputStream();
        VerifyCodeUtils.outputImage(100,40,os,code);


    }
}
