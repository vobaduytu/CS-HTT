package com.tu.controller.shop;

import com.tu.model.Order;

import com.tu.repository.OrderRepository;
import com.tu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping(value = "order")
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("")
    public String showOrder(Model model, Pageable pageable){
        model.addAttribute("list",orderService.showAll(pageable));
        return "shop/order/list-order";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable long id, Model model){
        Optional<Order> orders = orderService.findById(id);
        if(orders != null){
            model.addAttribute("oderDetail",orders);
            return "shop/order/edit-order";
        }else {
            return "erorr";
        }
    }
    @PostMapping("/edit")
    public String doEdit(@Valid @ModelAttribute("order")Order order, BindingResult result, RedirectAttributes attributes){
        if (result.hasErrors()){
            return "shop/order/edit-order";
        }
        orderService.save(order);
        attributes.addFlashAttribute("mess","thay đổi thành công");
        return "redirect:/order";
    }

}
