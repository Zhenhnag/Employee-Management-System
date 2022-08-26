package com.program.service;

import com.program.entity.Employee;
import com.program.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

    private EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    /**
     * 员工列表方法
     * @return
     */
    @Override
    public List<Employee> lists() {
        return employeeMapper.lists();
    }

    /**
     * 保存员工信息
     * @param employee
     */
    @Override
    public void save(Employee employee) {
        employeeMapper.save(employee);
    }


    /**
     * 根据id查询一个
     * @param id
     * @return
     */
    @Override
    public Employee findById(Integer id) {
        return employeeMapper.findById(id);
    }


    /**
     * 更新员工信息
     * @param employee
     */
    @Override
    public void update(Employee employee) {
        employeeMapper.update(employee);
    }

    /**
     * 删除员工信息
     * @param id
     */
    @Override
    public void delete(Integer id) {
        employeeMapper.delete(id);
    }
}
