package com.tu.controller.shop;


import com.tu.model.Category;
import com.tu.model.Product;
import com.tu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class SearchProductController {
@Autowired
private ProductService productService;
    @PostMapping("/searchProduct")
    public String search(@RequestParam Optional<String> search , Model model, @PageableDefault(size = 5) Pageable pageable, RedirectAttributes attributes){
        Page<Product> products = productService.findAllByNameContaining(search.get(),pageable);
        if (!products.isEmpty()){
            model.addAttribute("list", products);
            return "shop/shop-page";
        }else {
          model.addAttribute("mess","Không có sản phẩm nào khớp với dữ liệu tìm kiếm");
            return "shop/shop-page";
        }
    }
}
