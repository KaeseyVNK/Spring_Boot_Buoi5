package com.example.Demo4.Controller;

import com.example.Demo4.models.Product;
import com.example.Demo4.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.nio.file.Path;
import org.springframework.core.io.ResourceLoader;
@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    public static String UPLOAD_DIRECTORY = "src/main/resources/static/images/";

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("")
    public String index(Model model)
    {
        model.addAttribute("listproduct", productService.GetAll());
        return "products/index";
    }

    @GetMapping("/create")
    public String create(Model model)
    {
        model.addAttribute("product",new Product());
        return "products/create";
    }
    @PostMapping("/create")
    public String create(@Valid Product newProduct, @RequestParam MultipartFile imageProduct, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            return "products/create";
        }

        if (imageProduct != null && imageProduct.getSize() > 0) {
            try {
                String fileName = imageProduct.getOriginalFilename();
                Path imagePath = Paths.get(UPLOAD_DIRECTORY + fileName);
                Files.copy(imageProduct.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        productService.add(newProduct);
        return "redirect:/products";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") long id, Model model) {
        Product product = productService.get((int) id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/edit";
    }


    @PostMapping("/edit/{id}")
    public String Edit(@PathVariable("id") long id, @Valid Product newProduct, @RequestParam MultipartFile imageProduct, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            return "products/edit";
        }

        Product existingProduct = productService.get((int)id);

        if (existingProduct == null) {
            return "redirect:/products";
        }

        if (imageProduct != null && imageProduct.getSize() > 0) {
            try {
                String fileName = imageProduct.getOriginalFilename();
                Path imagePath = Paths.get(UPLOAD_DIRECTORY + fileName);
                Files.copy(imageProduct.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        productService.edit(newProduct);
        return "redirect:/products";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        productService.delete(id);
        return "redirect:/products";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String query, Model model) {
        List<Product> searchResults = productService.search(query);
        model.addAttribute("listproduct", searchResults);
        return "products/index";
    }


}
