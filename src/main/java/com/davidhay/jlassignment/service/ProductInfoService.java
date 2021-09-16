package com.davidhay.jlassignment.service;

import com.davidhay.jlassignment.domain.inbound.ProductType;
import com.davidhay.jlassignment.domain.outbound.ProductInfo;
import com.davidhay.jlassignment.mapper.ProductToProductInfoMapper;
import com.davidhay.jlassignment.repository.ProductCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProductInfoService {

    private final ProductCatalogRepository repo;
    private final ProductToProductInfoMapper mapper;

    public ProductInfoService(ProductCatalogRepository repo,
                               ProductToProductInfoMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<ProductInfo> getProductInfo(ProductType productType, Predicate<ProductInfo> filter, Comparator<ProductInfo> comparator, Consumer<ProductInfo> labeller) {
        return repo.getProductsFromCatalog(productType)
                .stream()
                .map(mapper::mapToProductInfo)
                .filter(filter)
                .sorted(comparator)
                .peek(labeller)
                .collect(Collectors.toList());
    }
}
