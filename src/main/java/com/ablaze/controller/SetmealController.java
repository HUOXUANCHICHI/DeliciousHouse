package com.ablaze.controller;

import com.ablaze.common.R;
import com.ablaze.dto.SetmealDto;
import com.ablaze.entity.Category;
import com.ablaze.entity.Setmeal;
import com.ablaze.service.CategoryService;
import com.ablaze.service.SetmealDishService;
import com.ablaze.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 *
 * @author ablaze
 * @Date: 2023/07/18/18:48
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
@RequiredArgsConstructor
public class SetmealController {

    private final SetmealService setmealService;

    private final SetmealDishService setmealDishService;

    private final CategoryService categoryService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息:{}", setmealDto);
        boolean flag = setmealService.saveWithDish(setmealDto);
        return flag ? R.success("新增套餐成功") : R.error("新增套餐失败");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, String name) {
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null, Setmeal::getName, name);

        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
        boolean flag = setmealService.removeWithDish(ids);
        return flag ? R.success("套餐数据删除成功") : R.error("套餐数据删除失败");
    }

    /**
     * 根据id查询套餐信息和对应菜品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> list(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        if (setmealDto != null) {
            return R.success(setmealDto);
        }

        return R.error("查询套餐和对应菜品信息失败");
    }

    /**
     * 更新套餐数据及关联菜品关系
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        boolean flag = setmealService.updateWithDish(setmealDto);
        return flag ? R.success("更新套餐数据成功") : R.error("更新套餐数据失败");
    }

    /**
     * 根据id修改套餐的状态status(停售和起售)
     *
     * @param status 0停售，1起售
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("根据id修改套餐的状态:{},id为:{}", status, ids);
        boolean flag = false;

        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            flag = setmealService.updateById(setmeal);
        }

        return flag ? R.success("修改套餐状态成功") : R.error("修改套餐状态失败");
    }

}
