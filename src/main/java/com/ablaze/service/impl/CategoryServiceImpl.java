package com.ablaze.service.impl;

import com.ablaze.common.CustomException;
import com.ablaze.entity.Category;
import com.ablaze.entity.Dish;
import com.ablaze.entity.Setmeal;
import com.ablaze.mapper.CategoryMapper;
import com.ablaze.service.CategoryService;
import com.ablaze.service.DishService;
import com.ablaze.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ablaze
 * @Date: 2023/07/03/0:08
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final DishService dishService;

    private final SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     *
     * @param id
     */
    @Override
    public boolean remove(Long id) {

        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        if (count1 > 0) {
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，跑出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        long count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐,不能删除");
        }

        //正常删除分类
        return super.removeById(id);
    }
}
