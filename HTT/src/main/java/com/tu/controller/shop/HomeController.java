package com.tu.controller.shop;


import com.tu.repository.CategoryRepository;
import com.tu.service.CategoryService;
import com.tu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryRepository categoryRepository;
@Autowired
private CategoryService categoryService;
    @GetMapping("/home")
    public String showHome(Model model, Pageable pageable){
        model.addAttribute("list",productService.showAll(pageable));
        model.addAttribute("categories",categoryRepository.findByDeletedIsFalse (pageable));
        return "shop/home";
    }
}
