package com.tu.controller.admin.manager;

import com.tu.model.Category;
import com.tu.model.Customer;
import com.tu.repository.CustomerRepository;
import com.tu.repository.RoleRepository;
import com.tu.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("")
    public String showList(Model model, Pageable pageable) {
        model.addAttribute("list", customerService.showAll(pageable));
        return "admin/manager/customer/list-customer";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("customer",new Customer());
        return "admin/manager/customer/add-customer";
    }

    @PostMapping("/doAdd")
    public String doAdd(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, RedirectAttributes attributes, Model model
    ) {
        if (result.hasFieldErrors()){
            return "redirect:/customer/add";
        }

        if ((customer.getPassword()).equals(customer.getConfigPassword())){
            customerService.save(customer);
            attributes.addFlashAttribute("mess","Thêm mới thành công...!!!");
            return "redirect:/customer";
        }else {
            attributes.addFlashAttribute("mess","Mật khẩu không khớp...!!!");
            return "redirect:/customer/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String doEditForm(@PathVariable Long id, Model model) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer != null){
            model.addAttribute("roles",roleRepository.findAll());
            model.addAttribute("customer",customer);
            return "admin/manager/customer/edit-customer";
        }else {
            return "error";
        }
    }

    @PostMapping("/doEdit")
    public String doEdit(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, RedirectAttributes attributes) {
        if (result.hasFieldErrors()){
            attributes.addFlashAttribute("mess","Lỗi...!!!");
            return "redirect:/customer";
        }
        if ((customer.getPassword()).equals(customer.getConfigPassword())){
            customerService.save(customer);
            attributes.addFlashAttribute("mess", "Thay đổi thành công...!!!");
        }else {
            attributes.addFlashAttribute("mess","Mật khẩu không khớp...!!!");
        }
        return "redirect:/customer";
    }



    @GetMapping("/delete/{id}")
    public String doDelete(@PathVariable long id,RedirectAttributes redirectAttributes) {
        customerService.delete(id);
        redirectAttributes.addFlashAttribute("mess", "xóa thành công");
        return "redirect:/customer";
    }

    @GetMapping("/showDeleteCustomer")
    public String showDelete(Model model, Pageable pageable) {
        model.addAttribute("list", customerRepository.findAllByDeletedIsTrue(pageable));
        return "admin/manager/customer/list-delete-customer";
    }
    @GetMapping("/reset/{id}")
    public String reset(@PathVariable long id, RedirectAttributes redirectAttributes) {
       Customer customer = customerService.findById(id).orElseThrow();
        customer.setDeleted(false);
        customerService.save(customer);

        redirectAttributes.addFlashAttribute("mess", "khôi phục thành công");
        return "redirect:/customer/showDeleteCustomer";
    }
}