package fact.it.recordservice.controller;

import fact.it.recordservice.dto.RecordRequest;
import fact.it.recordservice.dto.RecordResponse;
import fact.it.recordservice.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createProduct
            (@RequestBody RecordRequest recordRequest) {
        recordService.createProduct(recordRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RecordResponse> getAllProductsBySkuCode
            (@RequestParam List<String> skuCode) {
        return recordService.getAllProductsBySkuCode(skuCode);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RecordResponse> getAllProducts() {
        return recordService.getAllProducts();
    }
}

