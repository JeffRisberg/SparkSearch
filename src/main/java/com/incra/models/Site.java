package com.incra.models;

import com.incra.database.AbstractDatedDatabaseItem;

import javax.persistence.*;
import java.util.List;

/**
 * Stores site info.
 */
@Entity
@Table(name = "site")
public class Site extends AbstractDatedDatabaseItem {

    @Basic
    private String name;

    @Basic
    protected Double lat;

    @Basic
    protected Double lng;

    public Site() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}