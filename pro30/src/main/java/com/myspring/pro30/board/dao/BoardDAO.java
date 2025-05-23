package com.myspring.pro30.board.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.board.vo.ArticleVO;

public interface BoardDAO {

	List selectAllArticlesList() throws DataAccessException;
	
	int getNewArticleNO() throws DataAccessException;
	
	int insertNewArticle(ArticleVO article) throws DataAccessException;

}