package com.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="carinfo")
public class CarInfo implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    private Long id;

    @Column(name="carid", nullable=false)
    private String carid;

    @Column(name="info", nullable=false)
    private String info;

    @Column(name="created_at", updatable = false, nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public CarInfo() {
        this.carid = "";
        this.info = "";
        this.createdAt = new Date();
    }

    public CarInfo(String carid, String info) {
        this.carid = carid;
        this.info = info;
        this.createdAt = new Date();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarId() {
        return carid;
    }

    public void setCarId(String carid) {
        this.carid = carid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
