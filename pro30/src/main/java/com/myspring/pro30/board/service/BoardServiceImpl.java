package com.myspring.pro30.board.service;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;


@Service("boardService")
@Transactional(propagation = Propagation.REQUIRED)
public class BoardServiceImpl  implements BoardService{

	@Autowired
	BoardDAO boardDAO;
	
	public List<ArticleVO> listArticles() throws Exception{
		List<ArticleVO> articlesList =  boardDAO.selectAllArticlesList();
        return articlesList;
	}



	@Override
	public int addArticle(ArticleVO articleVO) throws Exception{
		return boardDAO.insertNewArticle(articleVO);
	}
//	
//	@Override
//	public ArticleVO viewArticle(int articleNO) throws Exception{
//
//	}
//	
//	@Override
//	public void modArticle(ArticleVO article) throws Exception{
//
//	}
//
//	@Override
//	public List<Integer> removeArticle(int articleNO) throws Exception{
//
//	}
//	
//	@Override
//	public int addReply(ArticleVO article) throws Exception{
//
//	}
//
//	@Override
//	public Map listArticles(Map<String, Integer> pagingMap) throws Exception{  
//
//	}

}
