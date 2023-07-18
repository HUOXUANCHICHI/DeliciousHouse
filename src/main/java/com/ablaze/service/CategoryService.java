package com.ablaze.service;

import com.ablaze.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author ablaze
 * @Date: 2023/07/03/0:07
 */
public interface CategoryService extends IService<Category> {

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     * @return
     */
    boolean remove(Long id);
}
