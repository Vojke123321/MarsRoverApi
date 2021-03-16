package com.vojke.MarsRoverApp.controllers;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vojke.MarsRoverApp.dto.HomeDto;
import com.vojke.MarsRoverApp.model.MarsRoverModel;
import com.vojke.MarsRoverApp.service.MarsRoverService;

import antlr.StringUtils;

@Controller
public class HomeController {

	@Autowired
	private MarsRoverService marsRoverService;

	@GetMapping("/")
	public String home(Model model,HomeDto homeDto) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if(homeDto.getMarsApiRoverData()==null || homeDto.getMarsApiRoverData() == "") {
			homeDto.setMarsApiRoverData("Opportunity");
		}
		
		if(homeDto.getMarsSol() ==null) {
			homeDto.setMarsSol(1);
		}
		
		MarsRoverModel roverData = marsRoverService.getMarsRoverData(homeDto);
		model.addAttribute("roverData", roverData);
		model.addAttribute("homeDto",homeDto);
		model.addAttribute("validCameras",marsRoverService.getValidCameras().get(homeDto.getMarsApiRoverData()));
		return "index";
	}

	

}
