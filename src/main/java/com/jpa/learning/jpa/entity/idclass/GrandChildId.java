package com.jpa.learning.jpa.entity.idclass;

import java.io.Serializable;
import java.util.Objects;

public class GrandChildId implements Serializable {
    private ChildId child;
    private String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrandChildId that = (GrandChildId) o;
        return Objects.equals(child, that.child) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, id);
    }
}
