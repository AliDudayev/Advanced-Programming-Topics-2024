package fact.it.recordservice.service;

import fact.it.recordservice.dto.RecordRequest;
import fact.it.recordservice.dto.RecordResponse;
import fact.it.recordservice.model.Product;
import fact.it.recordservice.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public void createProduct(RecordRequest recordRequest){
        Product product = Product.builder()
                .skuCode(recordRequest.getSkuCode())
                .name(recordRequest.getName())
                .description(recordRequest.getDescription())
                .price(recordRequest.getPrice())
                .build();

        recordRepository.save(product);
    }

    public List<RecordResponse> getAllProducts() {
        List<Product> products = recordRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<RecordResponse> getAllProductsBySkuCode(List<String> skuCode) {
        List<Product> products = recordRepository.findBySkuCodeIn(skuCode);

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private RecordResponse mapToProductResponse(Product product) {
        return RecordResponse.builder()
                .id(product.getId())
                .skuCode(product.getSkuCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

}
