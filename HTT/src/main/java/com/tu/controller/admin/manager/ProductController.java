package com.tu.controller.admin.manager;


import com.tu.model.Product;
import com.tu.repository.CategoryRepository;
import com.tu.repository.ProductRepository;
import com.tu.service.CategoryService;
import com.tu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/product")

public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String showProduct(Model model, @PageableDefault(size = 7, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("list", productService.showAll(pageable));
        return "admin/manager/product/list-product";
    }

    @GetMapping("/add")
    public String showAdd(Model model, Pageable pageable) {
        model.addAttribute("product", new Product());
        model.addAttribute("categoryList", categoryService.showAll(pageable));
        return "admin/manager/product/add-product";
    }

    @PostMapping("/add")
    public String doAdd(@Valid @ModelAttribute("product") Product product, BindingResult result, RedirectAttributes attributes, Model model, HttpServletRequest request) throws IOException {
            if (result.hasErrors()) {
                model.addAttribute("categoryList", categoryRepository.findAll());
                return "admin/manager/product/add-product";
            }
            List<Product> productTemps = productRepository.findByName(product.getName());
            if (productTemps == null || productTemps.isEmpty()) {
                String uploadRootPath = request.getServletContext().getRealPath("upload");

                File uploadRootDir = new File(uploadRootPath);

                String uploadLocalPath = "D:\\CodeGym\\HTT\\HTT\\src\\main\\webapp\\upload";

                File uploadLocalDir = new File(uploadLocalPath);

                uploadRootDir.mkdirs();

                CommonsMultipartFile[] fileDatas = product.getImageMulti();

                for (CommonsMultipartFile fileData : fileDatas) {
                    String name = fileData.getOriginalFilename();
                    System.out.println("Client File Name = " + name);
                    if (name != null && name.length() > 0) {
                        //ghi file vao server
                        File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
                        FileOutputStream fosServer = new FileOutputStream(serverFile);
                        BufferedOutputStream streamServer = new BufferedOutputStream(fosServer);
                        streamServer.write(fileData.getBytes());
                        streamServer.close();

                        //ghi file vao local
                        File localFile = new File(uploadLocalDir.getAbsolutePath() + File.separator + name);
                        FileOutputStream fosLocal = new FileOutputStream(localFile);
                        BufferedOutputStream streamLocal = new BufferedOutputStream(fosLocal);
                        streamLocal.write(fileData.getBytes());
                        streamLocal.close();

                        //set image and save
                        product.setImage(name);
                        productService.saves(product);
                        attributes.addFlashAttribute("mess", "Th??m m???i th??nh c??ng...!!!");
                    }
                }
            } else {
                attributes.addFlashAttribute("mess", "T??n product ???? t???n t???i");
            }
            return "redirect:/product";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "admin/manager/product/edit-product";
    }

    @PostMapping("/edit")
    public String doEdit(@Valid @ModelAttribute("product") Product product, BindingResult result, RedirectAttributes attributes,HttpServletRequest request) {
        try {
            if (result.hasErrors()) {
                return "admin/manager/product/edit-product";
            }

            List<Product> products = productRepository.findAll();
            for (Product product1 : products) {
                if ((product.getName()).equals(product1.getName()) && (product.getId()) != product1.getId()) {
                    attributes.addFlashAttribute("mess", "T??n ???? t???n t???i...!!!");
                    return "redirect:/product";
                }
            }
            String uploadRootPath = request.getServletContext().getRealPath("upload");

            File uploadRootDir = new File(uploadRootPath);

            String uploadLocalPath = "D:\\CodeGym\\HTT\\HTT\\src\\main\\webapp\\upload";

            File uploadLocalDir = new File(uploadLocalPath);

            uploadRootDir.mkdirs();

            CommonsMultipartFile[] fileDatas = product.getImageMulti();

            for (CommonsMultipartFile fileData : fileDatas) {
                String name = fileData.getOriginalFilename();
                System.out.println("Client File Name = " + name);
                if (name != null && name.length() > 0) {
                    //ghi file vao server
                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
                    FileOutputStream fosServer = new FileOutputStream(serverFile);
                    BufferedOutputStream streamServer = new BufferedOutputStream(fosServer);
                    streamServer.write(fileData.getBytes());
                    streamServer.close();

                    //ghi file vao local
                    File localFile = new File(uploadLocalDir.getAbsolutePath() + File.separator + name);
                    FileOutputStream fosLocal = new FileOutputStream(localFile);
                    BufferedOutputStream streamLocal = new BufferedOutputStream(fosLocal);
                    streamLocal.write(fileData.getBytes());
                    streamLocal.close();

                    //set image and save
                    product.setImage(name);
                    productService.saves(product);
                    attributes.addFlashAttribute("mess", "Th??m m???i th??nh c??ng...!!!");
                }
            }
            productService.saves(product);
            attributes.addFlashAttribute("mess", "Thay ?????i th??nh c??ng...!!!");
        } catch (Exception e) {
            e.getMessage();
            attributes.addFlashAttribute("mess", "Error");
        }
        return "redirect:/product";
    }

    @GetMapping("/view/{id}")
    public String showView(@PathVariable long id, Model model) {
        model.addAttribute("product", productService.findById(id).orElseThrow());
        model.addAttribute("list", productRepository.findAll());
        model.addAttribute("category", categoryRepository.findAll());
        return "admin/manager/product/view-product";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("mess", "x??a th??nh c??ng");
        return "redirect:/product";
    }

    @GetMapping("/showDeleteProduct")
    public String showDelete(Model model, @PageableDefault(size = 7, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("list", productRepository.findAllByDeletedIsTrue(pageable));
        return "admin/manager/product/list-delete-product";
    }

    @GetMapping("/reset/{id}")
    public String reset(@PathVariable long id, RedirectAttributes redirectAttributes) {
        Product product = productService.findById(id).orElseThrow();
        if (!product.getCategory().isDeleted()) {
            product.setDeleted(false);
            productService.saves(product);
            redirectAttributes.addFlashAttribute("mess", "kh??i ph???c th??nh c??ng");
            return "redirect:/product/showDeleteProduct";
        } else
            redirectAttributes.addFlashAttribute("mess", "B???n c???n kh??i ph???c Category tr?????c");
        return "redirect:/category/showDeleteCategory";


//      else
//          redirectAttributes.addFlashAttribute("mess", "B???n c???n kh??i ph???c User tr?????c");
//          return "redirect:/customer/showDeleteCustomer";
    }
}
