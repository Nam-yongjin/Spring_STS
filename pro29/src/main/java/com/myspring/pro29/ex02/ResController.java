package com.myspring.pro29.ex02;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller  // @RestController로 지정하지않아 viewResolver를 거침
public class ResController {
	
	// JSON
	@RequestMapping(value="/res1")
	@ResponseBody  // 설정된 영역만 @RestController 처럼 REST API사용
	public Map<String, Object> res1() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "hong");
		map.put("name", "홍길동");
		return map;
	}
	
	// html
	@RequestMapping(value="/res2")
	public ModelAndView res2() {
		return new ModelAndView("home");
	}
}
