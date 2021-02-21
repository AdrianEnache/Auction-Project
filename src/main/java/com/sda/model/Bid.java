package com.sda.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer value;

    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

}
