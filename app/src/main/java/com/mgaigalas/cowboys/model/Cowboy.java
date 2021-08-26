package com.mgaigalas.cowboys.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Marius Gaigalas
 */
@EqualsAndHashCode(of = "name")
@Data
public class Cowboy {
    private String name;
    private int health;
    private int damage;
}
