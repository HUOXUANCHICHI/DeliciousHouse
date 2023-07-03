package com.ablaze.service.impl;

import com.ablaze.entity.Employee;
import com.ablaze.mapper.EmployeeMapper;
import com.ablaze.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ablaze
 * @Date: 2023/07/01/11:24
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
