package com.yy.miaosha.domain;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-01-31 17:56
 **/

public class User {
    private int id;
    private String name;

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}