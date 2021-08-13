package com.jpa.learning.jpa.entity;

import javax.persistence.AssociationOverride;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

@Entity
public class SubEntityB extends BaseEntity {

    private String shopName;
}
