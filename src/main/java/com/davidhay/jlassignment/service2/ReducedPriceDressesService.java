package com.davidhay.jlassignment.service2;

import com.davidhay.jlassignment.controller.LabelType;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.formatter.PriceLabelFormatter;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
public class ReducedPriceDressesService {

    public static final String PRODUCT_DRESSES = "dresses";

    private final ProductInfoService2 productInfoService;
    private final PriceLabelFormatter labelFormatter;

    public ReducedPriceDressesService(ProductInfoService2 productInfoService, PriceLabelFormatter labelFormatter) {
        this.productInfoService = productInfoService;
        this.labelFormatter = labelFormatter;
    }

    public List<ProductInfo> getReducedPriceDresses(LabelType labelType) {
        Predicate<ProductInfo> filter = ProductInfo::hasPriceReduction;
        Comparator<ProductInfo> comparator = Comparator.comparing(ProductInfo::getPriceReductionAmountPence).reversed();
        Consumer<ProductInfo> labeller = getLabelType(labelType);
        return productInfoService.getProductInfo(PRODUCT_DRESSES, filter, comparator, labeller);
    }

    private Consumer<ProductInfo> getLabelType(LabelType labelType){
        return productInfo -> {
            String label = this.labelFormatter.getPriceLabel(productInfo.getPriceInfo(),
                    labelType);
            productInfo.setPriceLabel(label);
        };
    }

}
