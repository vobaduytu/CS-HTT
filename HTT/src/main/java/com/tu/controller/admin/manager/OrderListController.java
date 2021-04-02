package com.tu.controller.admin.manager;


import com.tu.model.Order;
import com.tu.model.Status;
import com.tu.repository.OrderRepository;
import com.tu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderListController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/orderList")
    public String orderList(Model model,@PageableDefault(size = 7,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        model.addAttribute("orderList",orderService.showAll(pageable));
        return "admin/manager/order/list-order";
    }

    @GetMapping("/submit/{id}")
    public String submit(@PathVariable Long id, RedirectAttributes attributes){
        Order orderTemp = orderService.findById(id).orElseThrow();
        Status status = new Status();
        status.setId(2);
        orderTemp.setStatus(status);
        orderService.save(orderTemp);

        attributes.addFlashAttribute("mess","Đã Duyệt...!!!");
        return "redirect:/orderList";
    }
}
