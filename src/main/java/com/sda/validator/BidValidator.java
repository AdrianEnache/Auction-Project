package com.sda.validator;

import com.sda.dto.BidDto;
import com.sda.model.Bid;
import com.sda.model.Product;
import com.sda.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Comparator;
import java.util.Optional;

@Service
public class BidValidator {

    private ProductRepository productRepository;

    @Autowired
    public BidValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(String productId, BidDto bidDto, BindingResult bindingResult) {
        /*TODO */
        Optional<Product> optionalProduct = productRepository.findById(Integer.valueOf(productId));
        if (!optionalProduct.isPresent()) {
            bindingResult.addError(new FieldError("bidDto", "value", "Invalid product id"));
            return;
        }
        validateBidValue(bidDto, bindingResult, optionalProduct.get());

    }

    private boolean isBidValueNotNumber(String someNumber) {
        try {
            Integer.parseInt(someNumber);
        } catch (NumberFormatException exception) {
            return true;
        }
        return false;
    }

    private void validateBidValue(BidDto bidDto, BindingResult bindingResult, Product product) {
        if (isBidValueNotNumber(bidDto.getValue())) {
            bindingResult.addError(new FieldError("bidDto", "value", "This field should be a number!"));
            return;
        }

        Optional<Bid> optionalMaxBid = getMaxBid(product);
        int productStartingPrice = product.getStartingPrice();
        int bidDtoValue = Integer.parseInt(bidDto.getValue());
        int bidStep = product.getMinimumBidStep();
        boolean isError = false;
        String errorMessage = null;
        if (optionalMaxBid.isPresent()) {
           int productCurrentPrice = optionalMaxBid.get().getValue();
            if (bidDtoValue <= productCurrentPrice) {
                isError = true;
                errorMessage = "Value is smaller than the last bid!";
            } else if ((bidDtoValue - productCurrentPrice) < bidStep) {
                isError = true;
                errorMessage = "Bid step is smaller than the minimum Bid Step";
            }
        } else if (bidDtoValue < productStartingPrice) {
            isError = true;
            errorMessage = "Value is smaller than the starting price!";

        }
        if (isError) {
            bindingResult.addError(new FieldError("bidDto", "value", errorMessage));
        }
    }

    private Optional<Bid> getMaxBid(Product product) {
        Optional<Bid> optionalMaxBid = product.getBidList()
                .stream()
                .max(Comparator.comparing(Bid::getValue));
        return optionalMaxBid;
    }
}
