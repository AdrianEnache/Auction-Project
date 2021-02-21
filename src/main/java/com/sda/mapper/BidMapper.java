package com.sda.mapper;

import com.sda.dto.BidDto;
import com.sda.model.Bid;
import com.sda.model.Product;
import com.sda.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class BidMapper {


    public Bid map(BidDto bidDto, Product product, User user) {
        Bid bid = new Bid();
        bid.setValue(Integer.valueOf(bidDto.getValue()));
        bid.setProduct(product);
        bid.setUser(user);
        return bid;
    }
}
