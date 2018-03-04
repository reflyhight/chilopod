package cn.dtvalley.chilopod.master.controller;

import cn.dtvalley.chilopod.master.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@Controller
public class IndexController {
    @Resource
    private TestService testService;

    @GetMapping("/")
    public String index(Model model) {
        String test  =testService.test();
        System.out.println(test);
        model.addAttribute("test",test);
        return "index";
    }
}
