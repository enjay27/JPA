package com.jpa.learning.jpa.entity;

import javax.persistence.*;

@Entity
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "BOARD_ID")
    private Long id;

    private String title;

    @JoinColumn(name = "board_detail_ID")
    @OneToOne(mappedBy = "board")
    private BoardDetail boardDetail;
}
