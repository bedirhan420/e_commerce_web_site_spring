package com.example.web_proje.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    /*
     Ne Zaman Çalışır?
        Örneğin kullanıcı /adsfkjadsnfkadsf gibi var olmayan bir sayfaya giderse,
        Bir hata (exception) oluşursa ve global handler yoksa,
        handleError() metodu çalışır ve error.html sayfası render edilir.
    */

    @RequestMapping("/error")
    public String handleError() {
        return "error"; // error.html sayfasına yönlendirir
    }
}
