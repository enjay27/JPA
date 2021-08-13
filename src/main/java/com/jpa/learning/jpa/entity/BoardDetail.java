package com.jpa.learning.jpa.entity;

import javax.persistence.*;

@Entity
public class BoardDetail {
    @Id @GeneratedValue
    @Column(name = "BOARDDETAIL_ID")
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    private String content;
}