package com.example.todaysbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/index")
    public String index(Model model) {

        String userName = "테스트";

        model.addAttribute("userName", userName);

        return "index";
    }

    @GetMapping("/search")
    public String search(Model model) {

        return "book/search";
    }

    @GetMapping("/detail")
    public String detail(Model model) {

        return "book/detail";
    }

    @GetMapping("/registration")
    public String registration(Model model) {

        return "user/registration";
    }

    @GetMapping("/mypage/updateinfo")
    public String mypageUpdateInfo(Model model) {

        return "user/mypage/update-info";
    }

    @GetMapping("/mypage/updatepw")
    public String mypageUpdatePw(Model model) {

        return "user/mypage/update-password";
    }

    @GetMapping("/mypage/orderlist")
    public String orderList(Model model) {

        return "user/mypage/orderlist";
    }

    @GetMapping("/mypage/review")
    public String review(Model model) {

        return "user/mypage/review";
    }

    @GetMapping("/mypage/mileage")
    public String mileage(Model model) {

        return "user/mypage/mileage";
    }

    @GetMapping("/mypage/favoritebook")
    public String favoritebook(Model model) {

        return "user/mypage/favorite-book";
    }

    @GetMapping("/mypage/my_recommend_list")
    public String myRecommendList(Model model) {

        return "user/mypage/my-recommendlist";
    }

    @GetMapping("/mypage/users_recommend_list")
    public String userRecommendList(Model model) {

        return "user/mypage/users-recommendlist";
    }

    @GetMapping("/mypage/user/delivery")
    public String userDelivery(Model model) {

        return "user/mypage/delivery";
    }

    @GetMapping("/mypage/user/order_detail")
    public String userOrderDetail(Model model) {

        return "user/mypage/orderlist-detail";
    }

    @GetMapping("/mypage/create_recommendlist")
    public String createRecommendList(Model model) {

        return "user/mypage/create-recommendlist";
    }

    @GetMapping("/admin/userlist")
    public String userList(Model model) {

        return "admin/userlist";
    }

    @GetMapping("/admin/stocklist")
    public String stockList(Model model) {

        return "admin/stocklist";
    }

    @GetMapping("/admin/delivery")
    public String adminDelivery(Model model) {

        return "admin/delivery";
    }

    @GetMapping("/admin/book_registration")
    public String addBook(Model model) {

        return "admin/book-registration";
    }

    @GetMapping("/admin/updateinfo")
    public String adminUpdateInfo(Model model) {

        return "admin/update-info";
    }

    @GetMapping("/admin/updatepw")
    public String adminUpdatePw(Model model) {

        return "admin/update-password";
    }

    @GetMapping("/cart/list")
    public String cartList(Model model) {

        return "cart/list";
    }

    @GetMapping("/payment/info")
    public String paymentInfo(Model model) {

        return "payment/info";
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(Model model) {

        return "payment/success";
    }
}
