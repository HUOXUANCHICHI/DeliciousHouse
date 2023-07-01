package com.ablaze.mapper;

import com.ablaze.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: ablaze
 * @Date: 2023/07/01/11:23
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
