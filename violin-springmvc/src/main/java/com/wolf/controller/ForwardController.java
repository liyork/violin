package com.wolf.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:
 * <br/> Created on 2016/9/11 8:27
 *
 * @author 李超()
 * @since 1.0.0
 */

@Controller
@RequestMapping("/forwardController")
public class ForwardController {

	private Logger logger = LoggerFactory.getLogger(ForwardController.class);

	@RequestMapping(value = "/testForwardRequest")
	public String testForwardRequest() {
		System.out.println("testForwardRequest");
		return "testForwardRequest";
	}
}
