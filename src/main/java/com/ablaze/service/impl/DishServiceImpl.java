package com.ablaze.service.impl;

import com.ablaze.entity.Dish;
import com.ablaze.mapper.DishMapper;
import com.ablaze.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ablaze
 * @Date: 2023/07/03/16:55
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
