package com.jpa.learning.jpa.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "SUB_A_ID"))
public class SubEntityA extends BaseEntity {

    private String email;
}
