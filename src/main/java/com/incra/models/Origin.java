package com.incra.models;

import com.incra.database.AbstractDatedDatabaseItem;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Region on a page
 *
 * @author Jeff Risberg
 * @since February 2014
 */
@Entity
@Table(name = "box")
public class Origin extends AbstractDatedDatabaseItem {

    @Basic
    protected String name;

    @Basic
    protected Date date;

    @Basic
    protected Double lat;

    @Basic
    protected Double lng;

    // Constructor
    public Origin() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Origin[name=");
        sb.append(name);
        sb.append("]");

        return sb.toString();
    }
}