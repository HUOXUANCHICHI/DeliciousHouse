package com.ablaze.service;

import com.ablaze.dto.SetmealDto;
import com.ablaze.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author ablaze
 * @Date: 2023/07/03/16:54
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     * @return
     */
    boolean saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     * @return
     */
    boolean removeWithDish(List<Long> ids);

    /**
     * 根据id查询套餐信息和关联菜品数据
     * @param id
     * @return
     */
    SetmealDto getByIdWithDish(Long id);

    /**
     * 更新套餐，同时更新和菜品关联数据
     * @param setmealDto
     * @return
     */
    boolean updateWithDish(SetmealDto setmealDto);

}
