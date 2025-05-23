package com.myspring.pro30.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.vo.ArticleVO;

public interface BoradController {

	ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	ModelAndView addArticle(ArticleVO article, HttpServletRequest request, HttpServletResponse response)
			throws Exception;
	//	
	//	@Override
	//	@RequestMapping(value = "/board/viewArticle.do", method = { RequestMethod.GET, RequestMethod.POST })
	//	public viewArticle(){
	//
	//	}
	//	
	//	@Override
	//	@RequestMapping(value = "/board/modArticle.do", method = { RequestMethod.GET, RequestMethod.POST })
	//	public modArticle(){
	//
	//	}
	//	
	//	@Override
	//	@RequestMapping(value = "/board/removeArticle.do", method = { RequestMethod.GET, RequestMethod.POST })
	//	public removeArticle(){
	//
	//	}
	//	
	//	@Override
	//	@RequestMapping(value = "/board/replyForm.do", method = { RequestMethod.GET, RequestMethod.POST })
	//	public replyForm(){
	//
	//	}
	//	
	//	@Override
	//	@RequestMapping(value = "/board/addReply.do", method = { RequestMethod.GET, RequestMethod.POST })
	//	public addReply(){
	//
	//	}

}