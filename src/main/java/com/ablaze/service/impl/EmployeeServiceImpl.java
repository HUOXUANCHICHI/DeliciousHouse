package com.ablaze.service.impl;

import com.ablaze.entity.Employee;
import com.ablaze.mapper.EmployeeMapper;
import com.ablaze.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author: ablaze
 * @Date: 2023/07/01/11:24
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
