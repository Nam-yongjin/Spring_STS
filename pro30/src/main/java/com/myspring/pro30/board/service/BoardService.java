package com.myspring.pro30.board.service;

import java.util.List;
import java.util.Map;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;

public interface BoardService {

	// 단일 이미지 추가  ->> 다중 이미지
	int addNewArticle(Map articleMap) throws Exception;

	//다중 파일 보이기
	Map viewArticle(int articleNO) throws Exception;

	void modArticle(Map articleMap) throws Exception;

	void removeArticle(int articleNO) throws Exception;

	
	Map listArticles(Map<String, Integer> pagingMap) throws Exception;
	
	void removeModImage(ImageVO imageVO) throws Exception;

}