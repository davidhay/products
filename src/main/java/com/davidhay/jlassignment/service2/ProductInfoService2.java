package com.davidhay.jlassignment.service2;

import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.mapper.ProductToProductInfoMapper;
import com.davidhay.jlassignment.repository.ProductCatalogRepository2;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProductInfoService2 {

    private final ProductCatalogRepository2 repo;
    private final ProductToProductInfoMapper mapper;

    public ProductInfoService2(ProductCatalogRepository2 repo,
            ProductToProductInfoMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<ProductInfo> getProductInfo(String productName, Predicate<ProductInfo> filter, Comparator<ProductInfo> comparator, Consumer<ProductInfo> labeller) {
        return repo.getProductsFromCatalog(productName)
                .stream()
                .map(mapper::mapToProductInfo)
                .filter(filter)
                .sorted(comparator)
                .peek(labeller)
                .collect(Collectors.toList());
    }
}
