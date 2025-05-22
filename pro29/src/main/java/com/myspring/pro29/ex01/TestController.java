package com.myspring.pro29.ex01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController // viewResolver를 거치치 않음
@RequestMapping("/test/*")
public class TestController {
	@RequestMapping("/hello")
	public String hello() {
		return "Hello REST!!";
	}
	
	@RequestMapping("/member")
	public MemberVO member() {
		MemberVO vo = new MemberVO();
		vo.setId("hong");
		vo.setPwd("1234");
		vo.setName("홍길동");
		vo.setEmail("hong@test.com");
		return vo;
	}
	
	@RequestMapping("/membersList")
	public List<MemberVO> listMembers() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		for(int i = 0; i<10; i++){
			MemberVO vo = new MemberVO();
			vo.setId("hong"+i);
			vo.setPwd("123"+i);
			vo.setName("홍길동"+i);
			vo.setEmail("hong"+i+"@test.com");
			list.add(vo);
		}
		return list;
	}
	
	@RequestMapping("/membersMap")
	public Map<Integer, MemberVO> membersMap() {
		Map<Integer, MemberVO> map = new HashMap<Integer, MemberVO>();
		for(int i = 0; i<10; i++){
			MemberVO vo = new MemberVO();
			vo.setId("hong"+i);
			vo.setPwd("123"+i);
			vo.setName("홍길동"+i);
			vo.setEmail("hong"+i+"@test.com");
			map.put(i, vo);
		}
		return map;
	}
	
	// @PathVariable() : 상세페이지 나타낼때 값을 꺼낼때 사용
	@RequestMapping(value="/notice/{num}", method=RequestMethod.GET)
	public int nitice(@PathVariable("num") int num) throws Exception{
		return num;
	}
	
	static Logger logger = LoggerFactory.getLogger(TestController.class);
	// @RequestBody : JSON으로 들어온것을 자바형식으로 자동 parse해줌 
	@RequestMapping(value="/info", method=RequestMethod.POST)
	public void modify(@RequestBody MemberVO vo) {
		logger.info(vo.toString());
	}
	
	//@RestController는 별도의 View를 제공하지 않은 채 데이터를 전달하므로 전달 과정에서 예외가발생할 수 있음
	// 예외에 대해 좀 더 세밀한 제어가 필요한 경우 ResponseEntity 클래스를 사용함
	@RequestMapping("/membersList2")
	public ResponseEntity<List<MemberVO>> listMembers2() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		for(int i = 0; i<10; i++){
			MemberVO vo = new MemberVO();
			vo.setId("hong"+i);
			vo.setPwd("123"+i);
			vo.setName("홍길동"+i);
			vo.setEmail("hong"+i+"@test.com");
			list.add(vo);
		}
		return new ResponseEntity(list, HttpStatus.INTERNAL_SERVER_ERROR); // 오류코드 500
	}
	
	
	// HttpHeaders 클래스를 이용해 ResponseEntity로 전송할 데이터의 종류와 한글 인코딩을 설정할 수 있음
	@RequestMapping(value = "/res3")
	public ResponseEntity res3() {
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	    String message = "<script>";
	    message += "alert('새 회원을 등록합니다.');";
	    message += "location.href='/pro29/test/membersList2';";
	    message += "</script>";
	    return new ResponseEntity(message, responseHeaders, HttpStatus.CREATED); // 201
	}

}
