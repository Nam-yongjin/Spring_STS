package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;



@Controller("boardController")
public class BoardControllerImpl implements BoradController{

	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";

	@Autowired
	private BoardService boardService;
	@Autowired
	private ArticleVO articleVO;
	
	@Override
	@RequestMapping(value= "/board/listArticles.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String viewName = (String)request.getAttribute("viewName");
		List articlesList = boardService.listArticles();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesList", articlesList);
		return mav;
		
	}
	
	@RequestMapping(value = "/board/*Form.do", method = RequestMethod.GET)
	private ModelAndView form(@RequestParam(value = "result", required = false) String result,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.addObject("result", result);
		mav.setViewName(viewName);
		return mav;
	}

	@Override
	@RequestMapping(value = "/board/addArticle.do", method = {RequestMethod.POST })
	public ModelAndView addArticle(@ModelAttribute("article") ArticleVO article,
          HttpServletRequest request, HttpServletResponse response) throws Exception{
			
		int result = 0;
		result = boardService.addArticle(article);
		article.setParentNO(0);		// 새 글이므로 부모글 번호 0
		article.setId("hong"); // 비로그인 테스트
		if(article.getImageFileName()!=null && article.getImageFileName().length()!=0) {
		    File srcFile = new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+"\\"+article.getImageFileName());
		    File destDir = new File(ARTICLE_IMAGE_REPO +"\\"+article.getArticleNO());
		    destDir.mkdirs();
		    FileUtils.moveFileToDirectory(srcFile, destDir, true);
		}
		ModelAndView mav = new ModelAndView("redirect:/board/listArticles.do");
		return mav; 		
	}
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
