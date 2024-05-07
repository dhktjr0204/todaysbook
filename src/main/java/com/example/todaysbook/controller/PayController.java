package com.example.todaysbook.controller;

import com.example.todaysbook.domain.dto.PaymentBookInfoDto;
import com.example.todaysbook.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Controller
public class PayController {

    private final CartService cartService;

    public static int getTotalPrice(List<PaymentBookInfoDto> bookDtoList) {
        int totalPrice = 0;
        for (PaymentBookInfoDto paymentBookInfoDto : bookDtoList) {
            totalPrice += paymentBookInfoDto.getPrice() * paymentBookInfoDto.getQuantity();
        }
        return totalPrice;
    }

//    private static int getTotalMileage(List<PaymentBookInfoDto> bookDtoList) {
//        int totalMileage = 0;
//        for (PaymentBookInfoDto paymentBookInfoDto : bookDtoList) {
//            totalMileage += paymentBookInfoDto.getMileage();
//        }
//        return totalMileage;
//    }
    @GetMapping("/payment")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "redirect:/";
        }

        List<PaymentBookInfoDto> bookDtoList = (List<PaymentBookInfoDto>)session.getAttribute("user1_1");

        String orderName = "";

        for (PaymentBookInfoDto book : bookDtoList) {
            orderName += (book.getBookName() + " (" + book.getQuantity() + "권)\n");
        }
        orderName = orderName.substring(0, orderName.length()-1);
        model.addAttribute("totalPrice", getTotalPrice(bookDtoList));
        model.addAttribute("orderName", orderName);
        return "payment/pay";
    }

    @RequestMapping(value = "/payment/success", method = RequestMethod.GET)
    public String paymentRequest(HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession(false);
        List<PaymentBookInfoDto> bookDtoList = (List<PaymentBookInfoDto>)session.getAttribute("user1_1");
        model.addAttribute("bookDtoList", bookDtoList);
        model.addAttribute("totalPrice", getTotalPrice(bookDtoList));
        return "payment/success";
    }

    @RequestMapping(value = "/payment/fail", method = RequestMethod.GET)
    public String failPayment(HttpServletRequest request, Model model) throws Exception {
        String failCode = request.getParameter("code");
        String failMessage = request.getParameter("message");

        model.addAttribute("code", failCode);
        model.addAttribute("message", failMessage);

        return "payment/fail";
    }

    @RequestMapping(value = "/payment/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {

        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;
        try {
            // 클라이언트에서 받은 JSON 요청 바디입니다.
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = (String) requestData.get("amount");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        };
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        // TODO: 개발자센터에 로그인해서 내 결제위젯 연동 키 > 시크릿 키를 입력하세요. 시크릿 키는 외부에 공개되면 안돼요.
        // @docs https://docs.tosspayments.com/reference/using-api/api-keys
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        // @docs https://docs.tosspayments.com/reference/using-api/authorization#%EC%9D%B8%EC%A6%9D
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제 승인 API를 호출하세요.
        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        // @docs https://docs.tosspayments.com/guides/payment-widget/integration#3-결제-승인하기
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);


        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        // TODO: 결제 성공 및 실패 비즈니스 로직을 구현하세요.
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        // 주어진 reader를 읽어서 JSON 형식으로 변환한 후, 그 결과를 JSONObject로 반환합니다.
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        return ResponseEntity.status(code).body(jsonObject);
    }

    @GetMapping("/payment/info")
    public String paymentInfo(HttpServletRequest req, Model model) {
        // userId가 1인 사용자의 장바구니 목록 조회
//        List<CartBook> cartBooks = cartService.findCartBooksByUserId(1L);
//        int totalPrice = cartService.calculateTotalPrice(cartBooks); // 총 상품 가격을 계산
        HttpSession session = req.getSession(false);
        List<PaymentBookInfoDto> bookDtoList = (List<PaymentBookInfoDto>)session.getAttribute("user1_1");
        model.addAttribute("totalPrice", PayController.getTotalPrice(bookDtoList)); // 모델에 totalPrice를 추가하여 뷰로 전달
        return "payment/info";
    }

    @PostMapping("/payment/info")
    public ResponseEntity<String> paymentInfo(HttpServletRequest request, @RequestBody List<PaymentBookInfoDto> books) throws Exception {
        HttpSession session = request.getSession(true);
        session.setAttribute("user1_1", books);
        return ResponseEntity.ok("/payment/info");
    }

    @PostMapping("/payment/webhook")
    public String webhook() {
        System.out.println("webhook");
        return "redirect:/";
    }
}