package com.myspring.pro30.board.service;

import java.util.List;
import java.util.Map;

import com.myspring.pro30.board.vo.ArticleVO;

public interface BoardService {

//	List<ArticleVO> listArticles() throws Exception;

	// 단일 이미지 추가  ->> 다중 이미지
	int addNewArticle(Map articleMap) throws Exception;

	//다중 파일 보이기
	Map viewArticle(int articleNO) throws Exception;

	void modArticle(Map articleMap) throws Exception;

	void removeArticle(int articleNO) throws Exception;

//	int addReply(Map articleMap) throws Exception;
	
	Map listArticles(Map<String, Integer> pagingMap) throws Exception;

}