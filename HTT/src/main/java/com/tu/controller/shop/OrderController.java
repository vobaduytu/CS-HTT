package com.tu.controller.shop;

import com.tu.model.Order;

import com.tu.model.OrderDetail;
import com.tu.model.Status;
import com.tu.repository.OrderDetailRepository;
import com.tu.repository.OrderRepository;
import com.tu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@RequestMapping(value = "order")
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;


    @GetMapping("")
    public String showOrder(Model model, Pageable pageable){
        model.addAttribute("list",orderService.showAll(pageable));
        return "shop/order/list-order";
    }

    @PostMapping("/add")
    public String doAdd(@ModelAttribute("order") Order order,  HttpSession session,Model model,RedirectAttributes redirectAttributes,HttpServletRequest request){
        double total= (double) session.getAttribute("total");
        Order orderCart= (Order) session.getAttribute("order");
        order.setTotalPrice(total);
        Status status = new Status();
        status.setId(1);
        order.setStatus(status);
        orderService.save(order);
        for(OrderDetail orderDetail:orderCart.getOrderDetails()){
            orderDetail.setTotalPrice(orderDetail.getProduct().getPrice()*orderDetail.getQuantity());
            orderDetail.setOrder(order);
            orderDetailRepository.save(orderDetail);
        }

        model.addAttribute("order",new Order());
        session.setAttribute("size", 0);
        session.setAttribute("order", null);
        session.setAttribute("total", total);
        redirectAttributes.addFlashAttribute("mess","them thanh cong");
        return "redirect:" + request.getHeader("referer");
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
        attributes.addFlashAttribute("mess","thay ?????i th??nh c??ng");
        return "redirect:/order";
    }

}
