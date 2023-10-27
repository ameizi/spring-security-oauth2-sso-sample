package com.example.sso.authserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping
public class IndexController {

    @Value("${spring.application.name}")
    private String name;

    @Value("${spring.application.version}")
    private String version;

    @Value("${oa.profile-uri}")
    public String oaProfileUri;

    @Value("${crm.profile-uri}")
    public String crmProfileUri;

    @RequestMapping("/")
    public String index(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("name", name);
        model.addAttribute("version", version);
        model.addAttribute("oaProfileUri", oaProfileUri);
        model.addAttribute("crmProfileUri", crmProfileUri);
        return "index";
    }

    @ResponseBody
    @RequestMapping("/info")
    public Authentication info(Authentication authentication) {
        return authentication;
    }

}
