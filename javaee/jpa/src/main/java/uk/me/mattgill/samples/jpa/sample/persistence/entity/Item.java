/**
 * This file was generated by the Jeddict
 */
package uk.me.mattgill.samples.jpa.sample.persistence.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Item implements Serializable {

    @Id
    @Basic
    private String name;

    @Basic
    private Integer count;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}