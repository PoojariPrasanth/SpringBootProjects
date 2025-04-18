package com.example.ecom_proj.service;

import com.example.ecom_proj.model.Product;
import com.example.ecom_proj.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }



    public ResponseEntity<Product> getProductById(int id) {
        Product product=productRepository.findById(id).orElse(null);
        System.out.println("product "+product);

        //  ->new ResourceNotFoundException("not found"+id));

        if(product!=null){
            return ResponseEntity.ok(product);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageType(imageFile.getContentType());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageData(imageFile.getBytes());
        return productRepository.save(product);
    }


    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
        Product product1= productRepository.findById(id).orElse(null);
        if(product1!=null){
            product1.setName(product.getName());
            product1.setDescription(product.getDescription());
            product1.setBrand(product.getBrand());
            product1.setPrice(product.getPrice());
            product1.setCategory(product.getCategory());
            product1.setReleaseDate(product.getReleaseDate());
            product1.setProductAvailable(product.isProductAvailable());
            product1.setStockQuantity(product.getStockQuantity());

            product1.setImageType(imageFile.getContentType());
            product1.setImageName(imageFile.getOriginalFilename());
            product1.setImageData(imageFile.getBytes());

            return productRepository.save(product1);
        }
        else{
            return null;
        }

    }

    public Product deleteProduct(int id) {
        Product product=productRepository.findById(id).orElse(null);
        if(product!=null){
            productRepository.deleteById(id);
            return product;
        }
        else{
            return null;
        }
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProduct(keyword);
    }
}
