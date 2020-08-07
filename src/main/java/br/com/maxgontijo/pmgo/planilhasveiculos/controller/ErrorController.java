package br.com.maxgontijo.pmgo.planilhasveiculos.controller;//package br.com.maxgontijo.pmgo.planilhasveiculos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/")
public class ErrorController {
    @GetMapping(value = "/error")
    public String error() {
        return "Error";
    }
}