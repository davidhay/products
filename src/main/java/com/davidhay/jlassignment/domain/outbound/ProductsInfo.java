package com.davidhay.jlassignment.domain.outbound;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsInfo {

  private List<ProductInfo> products;

}
