package com.program.controller;

import com.program.entity.Employee;
import com.program.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("employee")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private EmployeeService employeeService;

    @Value("${photo.file.dir}")
    private String realPath;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 删除员工
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public String delete(Integer id){
        log.debug("The id of the deleted employee",id);
        //1.删除数据，如果先删除数据，头像路径就找不到了，所以先保存一份
        String photo = employeeService.findById(id).getPhoto();
        employeeService.delete(id);
        //2.删除头像
        File file = new File(realPath, photo);
        if (file.exists())
            file.delete();

        return "redirect:/employee/lists";
    }

    /**
     * 更新员工信息
     * @param employee
     * @param img
     * @return
     */
    @RequestMapping("update")
    public String update(Employee employee,MultipartFile img) throws IOException {
        log.debug("Employee info after update:name:{}, salary:{}, birthday:{}",
                employee.getName(),employee.getSalary(),employee.getBirthday());

        //1.判断是否更新头像，如果不为空，说明需要更新头像
        boolean notEmpty = !img.isEmpty();
        log.debug("Whether to update the avatar:{}",notEmpty);
        //更新头像
        if (notEmpty){
            //删除老头像(根据原始id查询原始头像)
            String oldPhoto = employeeService.findById(employee.getId()).getPhoto();
            File file = new File(realPath, oldPhoto);
            if (file.exists())
                file.delete(); //删除文件
            //新头像上传
            String originalFileName = img.getOriginalFilename();
            String newFileName = uploadPhoto(img, originalFileName);
            //修改员工新的头像名称
            employee.setPhoto(newFileName);
        }

        //2.没有更新员工头像，直接更新基本信息
        employeeService.update(employee);
        return "redirect:/employee/lists";  //更新成功跳转到员工列表
    }

    //文件上传方法
    private String uploadPhoto(MultipartFile img, String originalFileName) throws IOException {
        String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String fileNameSuffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = fileNamePrefix + fileNameSuffix;
        img.transferTo(new File(realPath,newFileName));
        return newFileName;
    }


    /**
     * 根据id查询员工的详细信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("detail")
    public String detail(Integer id,Model model){
        log.debug("query Employee id: {}",id);

        //1.根据id查询一个
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee",employee);
        return "updateEmp";//跳转到更新页面
    }

    /**
     * 保存员工信息
     * @return
     * 文件上传注意事项：
     *      1.表单的提交方式必须是post
     *      2。表单的enctype的属性必须为multipart/form-data
     */
    @RequestMapping("save")
    public String save(Employee employee, MultipartFile img) throws IOException {
        log.debug("name:{}, salary:{}, birthday:{}",
                employee.getName(),employee.getSalary(),employee.getBirthday());
        String originalFilename = img.getOriginalFilename();
        log.debug("avatarName:{}", originalFilename);
        log.debug("avatarSize:{}",img.getSize());
        log.debug("uploadPath:{}",realPath);

        //1.处理头像的上传,修改文件名
        String newFileName = uploadPhoto(img, originalFilename);

        //2.保存员工信息
        employee.setPhoto(newFileName); //保存头像名字
        employeeService.save(employee);

        return "redirect:/employee/lists";//保存成功跳转到列表页面
    }


    /**
     * 查询员工列表
     * @return
     */
    @RequestMapping("lists")
    public String lists(Model model){
        log.debug("Query all employee info");
        List<Employee> employeeList = employeeService.lists();
        model.addAttribute("employeeList",employeeList);
        return "emplist";
    }
}
