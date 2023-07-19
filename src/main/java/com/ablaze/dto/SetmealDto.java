package com.ablaze.dto;

import com.ablaze.entity.Setmeal;
import com.ablaze.entity.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * @author ablaze
 */
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
