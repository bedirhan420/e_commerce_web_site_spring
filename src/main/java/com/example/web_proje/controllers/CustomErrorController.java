package com.example.web_proje.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Hata durumunda gösterilecek sayfa
        return "error"; // error.html sayfasına yönlendirir
    }
}
