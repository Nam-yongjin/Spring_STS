package com.myspring.pro30.board.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.board.vo.ArticleVO;

@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List selectAllArticlesList() throws DataAccessException {
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}

	
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
	@Override
	public int getNewArticleNO() throws DataAccessException{
		int articleNO = sqlSession.insert("mapper.board.selectNewArticleNO");
		return articleNO;
	}

	@Override
	public int insertNewArticle(ArticleVO articleVO) throws DataAccessException{
		int articleNO = getNewArticleNO();
		articleVO.setArticleNO(articleNO);
		int result = sqlSession.insert("mapper.board.insertNewArticle", articleVO);
		
		return result;
	}
//
//	public ArticleVO selectArticle(int articleNO) throws DataAccessException{
//
//	}
//
//	public void updateArticle(ArticleVO article) throws DataAccessException{
//
//	}
//	
//	public List<Integer> selectRemovedArticles(int articleNO) throws DataAccessException{
//
//	}
//	public void deleteArticle(int articleNO) throws DataAccessException{
//	
//	}
}
