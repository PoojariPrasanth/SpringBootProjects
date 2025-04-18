package com.example.ecom_proj.controller;


import com.example.ecom_proj.model.Product;
import com.example.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("greet")
    public String greet(){
        return "Welcome";
    }


    @GetMapping("products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return productService.getAllProducts();
    }


    @GetMapping("product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        return productService.getProductById(id);

    }

    @PostMapping("product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) throws IOException {

        Product product1=productService.addProduct(product, imageFile);
        System.out.println("product :"+product1);
        if(product1!=null){
            return new ResponseEntity<>("product added  successfully",HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("product not added", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
        Product product= productService.getProductById(productId).getBody();
        byte[] imagefile=product.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType()))
                .body(imagefile);
    }

    @PutMapping("product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile) throws IOException {
        Product product1=productService.updateProduct(id,product,imageFile);
        if(product1!=null){
            return new ResponseEntity<>("product updated successfully",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("failed to update the product",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product= productService.deleteProduct(id);
        if(product!=null){
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product not found",HttpStatus.OK);
        }

    }

    @GetMapping("products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){

        System.out.println("searching with "+ keyword);
        List<Product> products=productService.searchProducts(keyword);

        return new ResponseEntity<>(products,HttpStatus.OK);

    }

}
