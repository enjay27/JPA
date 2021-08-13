package com.jpa.learning.jpa.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract public class BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;
}
