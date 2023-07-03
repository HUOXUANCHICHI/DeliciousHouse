package com.ablaze.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐
 * @author ablaze
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 分类id
     */
    private Long categoryId;
    /**
     * 套餐名称
     */
    private String name;
    /**
     * 套餐价格
     */
    private BigDecimal price;
    /**
     * 状态 0:停用 1:启用
     */
    private Integer status;
    /**
     * 编码
     */
    private String code;
    /**
     * 描述信息
     */
    private String description;
    /**
     * 图片
     */
    private String image;

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
//    private Integer isDeleted;
}
