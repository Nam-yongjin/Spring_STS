package com.myspring.pro30.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;

public interface BoardDAO {

	int insertNewArticle(Map articleMap) throws DataAccessException;

	void insertNewImage(Map articleMap) throws DataAccessException;

	ArticleVO selectArticle(int articleNO) throws DataAccessException;

	List selectImageFileList(int articleNO) throws DataAccessException;

	void updateArticle(Map articleMap) throws DataAccessException;

	void deleteArticle(int articleNO) throws DataAccessException;

	List selectAllArticlesList(Map pagingMap) throws DataAccessException;

	int selectTotArticles() throws DataAccessException;

	void updateImageFile(Map articleMap) throws DataAccessException;

	void insertModNewImage(Map articleMap) throws DataAccessException;
	
	void deleteModImage(ImageVO imageVO) throws DataAccessException;

}