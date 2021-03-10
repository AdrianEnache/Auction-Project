package com.sda.repository;

import com.sda.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product,Integer>, PagingAndSortingRepository<Product,Integer> {

    @Query("select p from Product p where :now between p.startBiddingTime and p.endBiddingTime")//this is a query (hibernate style) to give us the products
        // between startbiidingtime and endbidding time
    List<Product> findAllActive(@Param("now") LocalDateTime now);

    @Query("select distinct b.product from Bid b where :authenticatedUserEmail = b.user.email")
    List<Product> findAllByBidder(@Param("authenticatedUserEmail") String authenticatedUserEmail);

    @Query("select p from Product p where :now > p.endBiddingTime and p.winner is null")
    List<Product> findAllExpiredAndUnassigned(@Param("now")LocalDateTime now);

    //ful text search
    @Query(value = "SELECT * FROM product WHERE "
            + "MATCH(name,description) "
            + "AGAINST (?1)",
            nativeQuery = true)
    public List<Product> search(String keyword);
}
