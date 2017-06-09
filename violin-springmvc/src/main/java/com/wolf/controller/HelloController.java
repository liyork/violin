package com.wolf.controller;

import com.wolf.exceptionresolver.MyException;
import com.wolf.exceptionresolver.SimpleException;
import com.wolf.exceptionresolver.UpdateResponseStatusException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 * <br/> Created on 2016/9/11 8:27
 *
 * @author 李超()
 * @since 1.0.0
 */

@Controller
@RequestMapping("/helloController")
public class HelloController {

	private Logger logger = LoggerFactory.getLogger(HelloController.class);

	@RequestMapping(value = "/hello")
	public String hello() {
		System.out.println("hello");

		logger.info("xxxxxxxxxxxxx1111");
		logger.debug("xxxxxxxxxxxxx22222");
		logger.error("xxxxxxxxxxxxx33333");
		System.out.println(System.getProperty("webapp.violin"));
		return "hello";
	}

	@RequestMapping(value = "/useModeAndView")
	public ModelAndView handleRequest() {
		String hello = "lishehe hello";
		return new ModelAndView("/welcome", "result", hello);
	}


	/***
	 * 用户登陆
	 * <p>注解配置，只允许POST提交到该方法
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(String username, String password) {
		//验证传递过来的参数是否正确，否则返回到登陆页面。
		if (this.checkParams(new String[]{username, password})) {
			//指定要返回的页面为succ.jsp
			ModelAndView mav = new ModelAndView("succ");
			//将参数返回给页面
			mav.addObject("username", username);
			mav.addObject("password", password);
			return mav;
		} else {
			return new ModelAndView("home");
		}
	}

	/***
	 * 验证参数是否为空
	 * @param params
	 * @return
	 */
	private boolean checkParams(String[] params) {
		for (String param : params) {
			if (StringUtils.isEmpty(param)) {
				return false;
			}
		}
		return true;
	}


	@RequestMapping(value = "/testAutoBox")
	public ModelAndView testAutoBox(Person person) {
		System.out.println("name===>" + person.getName());
		ModelAndView modelAndView = new ModelAndView("/autoBox");
		modelAndView.addObject("name", person.getName());
		modelAndView.addObject("age", person.getAge());
		return modelAndView;
	}


	@RequestMapping("/testAjax")
	public void testAjax(String name, PrintWriter pw) {
		pw.write("hello," + name);
	}

	//MappingJackson2HttpMessageConverter
	@RequestMapping("/testajaxRequestBody")
	public void testajaxRequestBody(@RequestBody Person person) {
		System.out.println(person.getName() + "xxx");
	}

	@RequestMapping(value = "/testUpload", method = RequestMethod.POST)
	public String testUpload(HttpServletRequest req) throws Exception {
		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) req;
		MultipartFile file = mreq.getFile("file");
		String fileName = file.getOriginalFilename();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dirPath = req.getSession().getServletContext().getRealPath("/") + "upload/";
		File targetDir = new File(dirPath);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		System.out.println("targetDir==>" + targetDir);
		String path = dirPath + sdf.format(new Date()) + fileName.substring(fileName.lastIndexOf('.'));
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(file.getBytes());
		fos.flush();
		fos.close();

		return "hello";
	}

	//	http://localhost:8080/springmvc/helloController/testRequestParam?id1=1&name1=a
	@RequestMapping(value = "/testRequestParam")
	public String testRequestParam(@RequestParam(value = "id1") Integer id,
								   @RequestParam(value = "name1") String name) {
		System.out.println(id + " " + name);
		return "/hello";
	}

	//http://localhost:8080/springmvc/helloController/testJson
	@ResponseBody
	@RequestMapping("/testJson")
	public Person testJson() {
		Person u = new Person();
		u.setName("jayjay");
		u.setAge(11);
		return u;
	}


	//============================exception resolver start====================================================

	//	http://localhost:8080/springmvc/helloController/testRequestParam?id1=1&name1=a
	@RequestMapping(value = "/testExceptionResolver")
	public String testExceptionResolver(@RequestParam(value = "id1") Integer id,
										@RequestParam(value = "name1") String name) {
		System.out.println(id + " " + name);
		int i = 1;
		if (i == 1) {
			throw new SimpleException("11");
		}

		return "/hello";
	}

	//此control内请求遇到异常,拦截到就不执行总异常处理了
	//处理异常范围：优先ExceptionHandler的value，如果没有配置则查询参数Exception类型
	@ExceptionHandler(value = SimpleException.class)
	public ModelAndView testExceptionHandler(Exception ex) {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("exception", ex);
		System.out.println("in testExceptionHandler");
		return mv;
	}


	//测试抓住异常后修改ResponseStatus
	//	http://localhost:8080/springmvc/helloController/testUpdateResponseStatusWhenException
	@RequestMapping(value = "/testUpdateResponseStatusWhenException")
	public void testUpdateResponseStatusWhenException() {
		int i = 1;
		if (i == 1) {
			throw new UpdateResponseStatusException("11");
		}
		System.out.println("testResponseStatus....");
	}

	//抓住异常修改返回status
	@ResponseStatus(reason = "no reason", value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = UpdateResponseStatusException.class)
	public void catchExceptionUpdateResponseStatus(Exception ex) {
		System.out.println("in catchExceptionUpdateResponseStatus");
	}

	//http://localhost:8080/springmvc/helloController/testResponseStatusExceptionResolver?id1=1&name1=a
	//用于测试ExceptionHandlerExceptionResolver处理不了的情况，使用ResponseStatusExceptionResolver处理
	//注：需要注释掉AllControllerException，不然ExceptionHandlerExceptionResolver会用全局的处理
	@RequestMapping(value = "/testResponseStatusExceptionResolver")
	public String testResponseStatusExceptionResolver(@RequestParam(value = "id1") Integer id,
													  @RequestParam(value = "name1") String name) {
		System.out.println(id + " " + name);
		int i = 1;
		if (i == 1) {
			throw new MyException("11");
		}

		return "/hello";
	}

	//如果所有都exceptionresolver都处理不了则直接返回异常
	@RequestMapping("/testError")
	public String testError() {
		int i = 5 / 0;
		return "hello";
	}

	//http://localhost:8080/springmvc/helloController/hello?abc=1
	// 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行，并在产生model
//	preHandle
//	populateModel===
//	postHandle
//	afterCompletion
	/*@ModelAttribute
	public void populateModel(@RequestParam String abc, Model model) {
		System.out.println("populateModel===");
		model.addAttribute("attributeName", abc);
	}*///由于所有方法都要先执行，先注掉


	@RequestMapping(value = "/testModel")
	public String testModel() {
		System.out.println("testModel");
		return "testModel";
	}

	//@RequestBody 会将请求中的参数注入到x中  abc=123&string1=999
	@RequestMapping(value = "/bodyAnnotation", method = RequestMethod.POST)
	public
	@ResponseBody
	String readString(@RequestBody String x) {
		System.out.println("Read string '" + x + "'");
		return "Read string '" + x + "'";
	}

//	//@RequestBody 会将请求中的参数注入到对象中  abc=123&string1=999
//	@RequestMapping(value="/bodyAnnotation", method=RequestMethod.POST)
//	public @ResponseBody String readObject(@RequestBody Per x) {
//		System.out.println("Read string '" + x + "'");
//		return "Read string '" + x + "'";
//	}

	//============================exception resolver end====================================================


	//============================forward redirect====================================================

	@RequestMapping(value = "/testForwardJsp1")
	public ModelAndView testForwardJsp1() {
		System.out.println("testForwardJsp1");
		//springmvc-servlet.xml中有配置前缀和后缀，这里不用/开头
		ModelAndView modelAndView = new ModelAndView("testForwardJsp");
		modelAndView.addObject("msg", "1111");
		return modelAndView;
	}

	@RequestMapping(value = "/testForwardRequest")
	public ModelAndView testForwardRequest() {
		System.out.println("testForwardRequest");
		//request.getRequestDispatcher会加上servlet上下文形成springmvc/forwardController/testForwardRequest
		ModelAndView modelAndView = new ModelAndView("forward:/forwardController/testForwardRequest");
		modelAndView.addObject("msg", "1111");
		return modelAndView;
	}

	@RequestMapping(value = "/testForwardJsp2")
	public ModelAndView testForwardJsp2() {
		System.out.println("testForwardJsp2");
		//request.getRequestDispatcher会加上servlet上下文形成/springmvc/WEB-INF/jsp/testForwardJsp.jsp
		ModelAndView modelAndView = new ModelAndView("forward:/WEB-INF/jsp/testForwardJsp.jsp");
		modelAndView.addObject("msg", "1111");
		return modelAndView;
	}

	@RequestMapping(value = "/testRedirectJsp")
	public ModelAndView testRedirectJsp() {
		System.out.println("testRedirectJsp");

		ModelAndView modelAndView = new ModelAndView("redirect:/testRedirectJsp.jsp");
		modelAndView.addObject("msg", "1111");
		return modelAndView;
	}
}
