package com.sda.repository;

import com.sda.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid,Integer> {

}
