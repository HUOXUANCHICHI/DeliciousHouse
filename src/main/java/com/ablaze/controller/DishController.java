package com.ablaze.controller;

import com.ablaze.common.R;
import com.ablaze.dto.DishDto;
import com.ablaze.entity.Category;
import com.ablaze.entity.Dish;
import com.ablaze.service.CategoryService;
import com.ablaze.service.DishFlavorService;
import com.ablaze.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ablaze
 * @Date: 2023/07/05/12:59
 */
@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
@Slf4j
public class DishController {
    private final DishService dishService;

    private final DishFlavorService dishFlavorService;

    private final CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        boolean flag = dishService.saveWithFlavor(dishDto);
        return flag ? R.success("新增菜品成功") : R.error("新增菜品失败");
    }

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(int page, int pageSize, String name) {

        //构造分页器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream()
                .map((item) -> {
                    DishDto dishDto = new DishDto();
                    BeanUtils.copyProperties(item, dishDto);
                    //分类id
                    Long categoryId = item.getCategoryId();
                    //根据id查询分类对象
                    Category category = categoryService.getById(categoryId);
                    if (category != null) {
                        String categoryName = category.getName();
                        dishDto.setCategoryName(categoryName);
                    }

                    return dishDto;
                }).collect(Collectors.toList());

        Page<DishDto> page1 = dishDtoPage.setRecords(list);

        if (page1 != null) {
            return R.success(dishDtoPage);
        }

        return R.error("员工信息分页查询失败");
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        if (dishDto != null) {
            return R.success(dishDto);
        }
        return R.error("查询菜品和口味信息失败");
    }

    /**
     * 更新菜品信息及口味信息
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        boolean flag = dishService.updateWithFlavor(dishDto);
        return flag ? R.success("新增菜品成功") : R.error("新增菜品失败");
    }

    /**
     * 根据id修改菜品的状态status(停售和起售)
     *
     * @param status 0停售，1起售。
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, Long[] ids) {
        log.info("根据id修改菜品的状态:{},id为:{}", status, ids);
        boolean flag = false;

        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            flag = dishService.updateById(dish);
        }

        return flag ? R.success("修改菜品状态成功") : R.error("修改菜品状态失败");
    }

    /**
     * 删除菜品信息及口味信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping()
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("根据id删除菜品的id为:{}", ids);

        boolean flag = dishService.deleteWithFlavor(ids);

        return flag ? R.success("修改删除成功") : R.error("修改删除失败");
    }

    /**
     * 根据条件查询对应的菜品数据
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1(起售状态)的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        if (list != null) {
            return R.success(list);
        }
        return R.error("查询到菜品数据失败");
    }
}
