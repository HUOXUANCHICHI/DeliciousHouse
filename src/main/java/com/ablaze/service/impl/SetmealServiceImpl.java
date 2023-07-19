package com.ablaze.service.impl;

import com.ablaze.common.CustomException;
import com.ablaze.dto.SetmealDto;
import com.ablaze.entity.Setmeal;
import com.ablaze.entity.SetmealDish;
import com.ablaze.mapper.SetmealMapper;
import com.ablaze.service.SetmealDishService;
import com.ablaze.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ablaze
 * @Date: 2023/07/03/16:56
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    private final SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        boolean setmealDtoFlag = this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream()
                .peek((item) -> item.setSetmealId(setmealDto.getId()))
                .collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        boolean setmealDishesFlag = setmealDishService.saveBatch(setmealDishes);

        return setmealDtoFlag && setmealDishesFlag;
    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeWithDish(List<Long> ids) {

        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        long count = this.count(setmealLambdaQueryWrapper);

        //如果不能删除，抛出一个业务异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中,不能删除");
        }

        //如果可以删除，先删除套餐表中的数据---setmeal
        boolean removeSetmeal = this.removeByIds(ids);

        //删除关系表中的数据---setmeal_dish
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        boolean removeSetmealDish = setmealDishService.remove(setmealDishLambdaQueryWrapper);

        return removeSetmeal && removeSetmealDish;
    }

    /**
     * 根据id查询套餐信息和关联菜品数据
     *
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {

        //查询套餐基本信息，从setmeal表查询
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        //查询当前套餐对应菜品信息，从setmeal_dish表查询
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> dishes = setmealDishService.list(setmealDishLambdaQueryWrapper);

        setmealDto.setSetmealDishes(dishes);

        return setmealDto;
    }

    /**
     * 更新套餐表及关联菜品表
     *
     * @param setmealDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWithDish(SetmealDto setmealDto) {

        //更新setmeal表基本信息
        this.updateById(setmealDto);

        //清理当前套餐对应关联菜品数据--setmeal_dish表的delete操作
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

        //添加当前提交过来的关联菜品数据--setmeal_dish表的insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream()
                .peek((item) -> item.setSetmealId(setmealDto.getId()))
                .collect(Collectors.toList());

        return setmealDishService.saveBatch(setmealDishes);
    }


}
