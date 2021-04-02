package com.tu.controller.shop;


import com.tu.model.Order;
import com.tu.repository.CategoryRepository;
import com.tu.repository.CustomerRepository;
import com.tu.service.CategoryService;
import com.tu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/getName")
    public String getAccount(HttpSession session,Principal principal){
        session.setAttribute("role",customerRepository.findByEmail(principal.getName()).getRole());
        System.out.println(customerRepository.findByEmail(principal.getName()).getRole());
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHome(Model model, @PageableDefault(size = 7,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("list",productService.showAll(pageable));
        model.addAttribute("categories",categoryRepository.findByDeletedIsFalse());
        model.addAttribute("order",new Order());
        return "shop/home";
    }
}
