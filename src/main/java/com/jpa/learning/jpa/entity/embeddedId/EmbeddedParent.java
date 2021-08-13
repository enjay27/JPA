package com.jpa.learning.jpa.entity.embeddedId;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Getter @Setter
@Entity
public class EmbeddedParent {

    @EmbeddedId
    private EmbeddedParentId id;

    private String name;
}