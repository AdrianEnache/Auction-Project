package com.sda.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String description;
    private ProductCategory category;
    private Integer startingPrice;
    private Integer minimumBidStep;
    private LocalDateTime startBiddingTime;
    private LocalDateTime endBiddingTime;


    @Lob
    private byte[] image;


    @ManyToOne(cascade = CascadeType.ALL)
    private User seller;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product", fetch = FetchType.EAGER)
    private List<Bid> bidList;

    @ManyToOne(cascade = CascadeType.ALL)
    private User winner;


}
