package com.tu.controller;

import com.tu.model.Customer;
import com.tu.model.Role;
import com.tu.repository.CustomerRepository;
import com.tu.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class SignUpController {
    @Autowired
    private CustomerService customerService;
   @Autowired
   private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/signup")
    public String showSingUp(Model model) {
        model.addAttribute("customerss", new Customer());
        return "signup";
    }

    @PostMapping("/signup")
    public String doSignUp(@Valid @ModelAttribute("customerss") Customer customer, BindingResult result, RedirectAttributes attributes, Pageable pageable) {
        if (result.hasErrors()) {
            return "signup";
        }
        Page<Customer> customerTemp = customerRepository.findByEmail(pageable, customer.getEmail());
        if (customerTemp.isEmpty() && (customer.getPassword()).equals(customer.getConfigPassword())){
            Role role = new Role();
            role.setId(3);
            customer.setImage("avt.jpg");
            customer.setRole(role);

            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerService.save(customer);
            attributes.addFlashAttribute("mess", "Thêm mới thành công!! hãy đăng nhập.....!");
            return "redirect:/login";
        }
        if (!customerTemp.isEmpty()){
            attributes.addFlashAttribute("mess", "email đã tồn tại");
            return "redirect:/signup";
        }
        attributes.addFlashAttribute("mess", "Mật khẩu không khớp...!!!");
        return "redirect:/signup";
    }
}
