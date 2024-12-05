package com.example.oumarket.Class;

public class Ward {
    private String code;
    private String name;
    private String parent_code;
    private String path;

    public Ward(String code, String name, String path, String parent_code) {
        this.code = code;
        this.name = name;
        this.parent_code = parent_code;
        this.path = path;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
