package com.myspring.pro30.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface BoradController {

	ModelAndView listArticles(@RequestParam("section") String section,
			@RequestParam("pageNum") String pageNum,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, 
								HttpServletRequest request, HttpServletResponse response) throws Exception;
		
	ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
								HttpServletRequest request, HttpServletResponse response) throws Exception;

	ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest,  
		    											HttpServletResponse response) throws Exception;
	
	ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO,
							HttpServletRequest request, HttpServletResponse response) throws Exception;


//	ResponseEntity addReply(MultipartHttpServletRequest multipartRequest,
//			HttpServletRequest request, HttpServletResponse response) throws Exception;

}