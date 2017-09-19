package com.incra.models;

import com.incra.database.AbstractDatedDatabaseItem;

import javax.persistence.*;

/**
 * Piece of a page
 *
 * @author Jeff Risberg
 * @since February 2014
 */
@Entity
@Table(name = "facility")
public class Facility extends AbstractDatedDatabaseItem {

    @Basic
    private String title;

    @Basic
    protected Double lat;

    @Basic
    protected Double lng;

    public Facility() {
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Facility[title=");
        sb.append(title);
        sb.append("]");

        return sb.toString();
    }
}