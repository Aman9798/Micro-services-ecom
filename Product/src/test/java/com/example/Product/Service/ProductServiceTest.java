package com.example.Product.Service;

import com.example.Product.DTO.FilterProductsDTO;
import com.example.Product.DTO.ProductDTO;
import com.example.Product.DTO.UpdateProductDTO;
import com.example.Product.Entity.Product;
import com.example.Product.Enums.Category;
import com.example.Product.Enums.Gender;
import com.example.Product.Exception.*;
import com.example.Product.Filter.ProductQueryService;
import com.example.Product.Mapper.ProductMapper;
import com.example.Product.Repository.ProductRepository;
import com.example.Product.Utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductQueryService productQueryService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    private final Integer PRODUCT_ID = 123;
    private final String VALID_TOKEN = "valid-token";
    private final String INVALID_TOKEN = "invalid-token";

    @Test
    void getAllProducts_success() {
        List<Product> products = List.of(new Product());
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
    }

    @Test
    void getAllProducts_failure() {
        List<Product> products = List.of(new Product());

        when(productRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThrows(
                Exception.class,
                () -> productService.getAllProducts()
        );
    }

    @Test
    void getProductById_success() {
        Product product = new Product();

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(PRODUCT_ID);

        assertNotNull(result);
        assertEquals(product, result);
    }

    @Test
    void getProductById_notFound() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(PRODUCT_ID)
        );
    }

    @Test
    void getProductsByCategory_success() {
        Product product = new Product();
        List<Product> products = List.of(product);

        when(productRepository.findByCategory(Category.SportsEquipments)).thenReturn(products);
        List<Product> result = productService.getProductsByCategory("SportsEquipments");

        assertEquals(1, result.size());
    }

    @Test
    void getProductsByCategory_invalidCategory() {
        assertThrows(
                NoSuchCategoryException.class,
                () -> productService.getProductsByCategory("Invalid")
        );
    }

    @Test
    void getProductsByGender_success() {
        Product product = new Product();
        List<Product> products = List.of(product);

        when(productRepository.findByGender(Gender.Women)).thenReturn(products);
        List<Product> result = productService.getProductsByGender("Women");

        assertEquals(1, result.size());
    }

    @Test
    void getProductsByGender_invalidGender() {
        assertThrows(
                NoSuchGenderException.class,
                () -> productService.getProductsByGender("Invalid")
        );
    }

    @Test
    void addProduct_success() {
        ProductDTO request = new ProductDTO();
        Product product = new Product();

        when(jwtTokenUtil.isAdmin(VALID_TOKEN)).thenReturn(true);
        when(productRepository.save(product)).thenReturn(product);

        try (MockedStatic<ProductMapper> mockedStatic =
                     mockStatic(ProductMapper.class)) {
            mockedStatic
                    .when(() -> ProductMapper.convertToProduct(request))
                    .thenReturn(product);

            Product result = productService.addProduct(request, VALID_TOKEN);

            assertNotNull(result);
            verify(productRepository).save(product);
            assertEquals(product, result);
        }
    }

    @Test
    void addProduct_unauthorized() {
        when(jwtTokenUtil.isAdmin(INVALID_TOKEN)).thenReturn(false);

        assertThrows(
                UnauthorizedAccess.class,
                () -> productService.addProduct(new ProductDTO(), INVALID_TOKEN)
        );

        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_success() {
        Product existingProduct = new Product();
        UpdateProductDTO requestDTO = new UpdateProductDTO();

        when(jwtTokenUtil.isAdmin(VALID_TOKEN)).thenReturn(true);
        when(productRepository.findById(PRODUCT_ID))
                .thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct))
                .thenReturn(existingProduct);

        Product result =
                productService.updateProduct(PRODUCT_ID, requestDTO, VALID_TOKEN);

        assertNotNull(result);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateProduct_unauthorized() {
        when(jwtTokenUtil.isAdmin(INVALID_TOKEN)).thenReturn(false);

        assertThrows(
                UnauthorizedAccess.class,
                () -> productService.updateProduct(PRODUCT_ID, new UpdateProductDTO(), INVALID_TOKEN)
        );

        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_productNotFound() {
        when(jwtTokenUtil.isAdmin(VALID_TOKEN)).thenReturn(true);
        when(productRepository.findById(PRODUCT_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(PRODUCT_ID, new UpdateProductDTO(), VALID_TOKEN)
        );
    }

    @Test
    void deleteProduct_success() {
        Product product = new Product();

        when(jwtTokenUtil.isAdmin(VALID_TOKEN)).thenReturn(true);
        when(productRepository.findById(PRODUCT_ID))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(PRODUCT_ID, VALID_TOKEN);

        verify(productRepository).deleteById(PRODUCT_ID);
    }

    @Test
    void deleteProduct_unauthorized() {
        when(jwtTokenUtil.isAdmin(INVALID_TOKEN)).thenReturn(false);

        assertThrows(
                UnauthorizedAccess.class,
                () -> productService.deleteProduct(PRODUCT_ID, INVALID_TOKEN)
        );

        verify(productRepository, never()).delete(any());
    }

    @Test
    void deleteProduct_productNotFound() {
        when(jwtTokenUtil.isAdmin(VALID_TOKEN)).thenReturn(true);
        when(productRepository.findById(PRODUCT_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProduct(PRODUCT_ID, VALID_TOKEN)
        );
    }

    @Test
    void filterProducts_success() {
        FilterProductsDTO filterDTO = new FilterProductsDTO();
        Product product = new Product();
        List<Product> products = List.of(product);

        when(productQueryService.filterProducts(filterDTO)).thenReturn(products);

        List<Product> result =
                productService.filterProducts(filterDTO);

        assertEquals(1, result.size());
    }

    @Test
    void reduceStock_success() {
        Product product = new Product();
        product.setStock(10L);

        when(productRepository.findById(PRODUCT_ID))
                .thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        productService.reduceStock(PRODUCT_ID, 3);

        assertEquals(7, product.getStock());
        verify(productRepository).save(product);
    }

    @Test
    void reduceStock_productNotFound() {
        when(productRepository.findById(PRODUCT_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.reduceStock(PRODUCT_ID, 2)
        );

        verify(productRepository, never()).save(any());
    }

    @Test
    void reduceStock_insufficientStock() {
        Product product = new Product();
        product.setStock(2L);

        when(productRepository.findById(PRODUCT_ID))
                .thenReturn(Optional.of(product));

        assertThrows(
                InsufficientStockException.class,
                () -> productService.reduceStock(PRODUCT_ID, 5)
        );

        verify(productRepository, never()).save(any());
    }
}
