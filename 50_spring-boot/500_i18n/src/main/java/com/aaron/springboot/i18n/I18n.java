package com.aaron.springboot.i18n;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("i18n")
public class I18n {
	
    @RequestMapping(value = "/market")
    public String lang(){
    	return"market/market";
    }
}
