package user.consumer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    //获取二维码图片
    @GetMapping("/img")
    String getImg(@RequestParam("img") String img){
        return img;
    }
    @GetMapping("/login")
    public String login(){
        return "login.html";
    }
//    public String login(){
//        return "userPage/login.html";
//    }

    @GetMapping("/err")
    public String error(){
        return "error.html";
    }

    @GetMapping("/up")
    public String mainFace(){
        return "mainFace.html";
    }

    @GetMapping("/mp")
    public String adminFace(){
        return "adminFace.html";
    }
}
