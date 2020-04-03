package com.kyser.demosuite.service.model;

import java.io.Serializable;

public class CategoryModel  implements Serializable {
    private Integer id;
    private String category_attr_template_id;
    private Integer cid;
    private Integer lid;
    private Integer parentcid;
    private String poster;
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory_attr_template_id() {
        return category_attr_template_id;
    }

    public void setCategory_attr_template_id(String category_attr_template_id) {
        this.category_attr_template_id = category_attr_template_id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public Integer getParentcid() {
        return parentcid;
    }

    public void setParentcid(Integer parentcid) {
        this.parentcid = parentcid;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
