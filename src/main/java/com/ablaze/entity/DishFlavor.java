package com.ablaze.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜品口味
 * @author ablaze
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 菜品id
     */
    private Long dishId;
    /**
     * 口味名称
     */
    private String name;
    /**
     * 口味数据list
     */
    private String value;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
    /**
     * 是否删除
     */
    private Integer isDeleted;

}
