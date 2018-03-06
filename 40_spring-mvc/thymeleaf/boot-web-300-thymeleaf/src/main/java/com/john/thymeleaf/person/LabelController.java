package com.john.thymeleaf.person;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping
public class LabelController {
	

	
	@GetMapping("/label")
	public ModelAndView label(){
		ModelAndView modelAndView = new ModelAndView("label");
		modelAndView.addObject("list","one,two,three".split(",") );
		modelAndView.addObject("src","http://omnfi8q3h.bkt.clouddn.com/upload/user/new/1499933262834.png" );
		return modelAndView;
	}
	
	@GetMapping("/data")
	public ModelAndView data(){
		ModelAndView modelAndView = new ModelAndView("data");
		modelAndView.addObject("num",1);
		modelAndView.addObject("user",new Person(3,"lijun","john"));
		return modelAndView;
	}
	
	@GetMapping("/co")
	public ModelAndView co(){
		ModelAndView modelAndView = new ModelAndView("co");
		modelAndView.addObject("num",1);
		modelAndView.addObject("user",new Person(3,"lijun","john"));
		return modelAndView;
	}
	
	
}
