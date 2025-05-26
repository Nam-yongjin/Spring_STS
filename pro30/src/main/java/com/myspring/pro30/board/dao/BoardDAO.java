package com.myspring.pro30.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.board.vo.ArticleVO;

public interface BoardDAO {

	List selectAllArticlesList() throws DataAccessException;

	int insertNewArticle(Map articleMap) throws DataAccessException;

	void insertNewImage(Map articleMap) throws DataAccessException;

	ArticleVO selectArticle(int articleNO) throws DataAccessException;

	List selectImageFileList(int articleNO) throws DataAccessException;

	void updateArticle(Map articleMap) throws DataAccessException;

	void deleteArticle(int articleNO) throws DataAccessException;
	//	@Override
	//	public List selectAllArticles() throws DataAccessException{
	//
	//	}
	//	
	//	// 오버로딩
	//	public List selectAllArticles(Map pagingMap) throws DataAccessException{
	//
	//	}
	//
	//	@Override
	//	public int selectTotArticles() throws DataAccessException{
	//
	//	}
	//

}