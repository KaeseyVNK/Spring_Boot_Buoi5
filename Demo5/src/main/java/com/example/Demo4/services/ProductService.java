package com.example.Demo4.services;

import com.example.Demo4.models.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private List<Product> listProduct= new ArrayList<>();
    int index = 0;
    public ProductService(){

    }
    public void add(Product newProduct){
        newProduct.setId(index);
        index++;
        listProduct.add(newProduct);
    }
    public List<Product> GetAll()
    {
        return listProduct;
    }
    public Product get(int id)
    {
        return listProduct.stream().filter(p->p.getId()== id).findFirst().orElse(null);
    }
    public void edit(Product editProduct)
    {
        Product find = listProduct.get(editProduct.getId());
        if(find!=null) {
            find.setName(editProduct.getName());
            find.setImage(editProduct.getImage());
            find.setPrice(editProduct.getPrice());
        }
    }

    public void delete(long id) {
        listProduct.removeIf(p -> p.getId() == id);
    }

    public List<Product> search(String productName) {
        return listProduct.stream()
                .filter(p -> p.getName().equalsIgnoreCase(productName))
                .collect(Collectors.toList());
    }


}
