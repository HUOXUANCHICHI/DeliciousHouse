package com.ablaze.service;

import com.ablaze.dto.DishDto;
import com.ablaze.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author ablaze
 * @Date: 2023/07/03/16:53
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     * @return
     */
    boolean saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息及口味信息
     * @param dishDto
     * @return
     */
    boolean updateWithFlavor(DishDto dishDto);

    /**
     * 删除菜品信息及口味信息
     * @param id
     * @return
     */
    boolean remove(Long id);
}
