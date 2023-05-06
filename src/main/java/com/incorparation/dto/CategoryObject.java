package com.incorparation.dto;

import com.incorparation.costants.CategoryStatus;

import java.io.Serializable;
import java.util.List;

public class CategoryObject implements Serializable {
    List<CategoryDTO> categories;

    public static class CategoryDTO implements Serializable {
        private Integer id;
        private String name;
        private CategoryStatus status;
    }
}
