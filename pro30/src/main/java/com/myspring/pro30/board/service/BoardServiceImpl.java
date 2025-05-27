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
	
//	@Override
//	public List<ArticleVO> listArticles() throws Exception{
//		List<ArticleVO> articlesList =  boardDAO.selectAllArticlesList();
//        return articlesList;
//	}

	// 단일 이미지 추가  ->> 다중 이미지
	@Override
	public int addNewArticle(Map articleMap) throws Exception{
		int articleNO = boardDAO.insertNewArticle(articleMap);
		articleMap.put("articleNO", articleNO);
		boardDAO.insertNewImage(articleMap);
		return articleNO;
	}
	
	//다중 파일 보이기
	@Override
	public Map viewArticle(int articleNO) throws Exception {
		Map articleMap = new HashMap();
		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO);
		articleMap.put("article", articleVO);
		articleMap.put("imageFileList", imageFileList);
		return articleMap;
	}

	@Override
	public void modArticle(Map articleMap) throws Exception{
		boardDAO.updateArticle(articleMap);
	}

	@Override
	public void removeArticle(int articleNO) throws Exception {
		boardDAO.deleteArticle(articleNO);
	}

	@Override
	public Map listArticles(Map pagingMap) throws Exception{  
		Map articlesMap = new HashMap();
		// 전달된 pagingMap읈 사용해 글목록 조회
		List articlesList = boardDAO.selectAllArticlesList(pagingMap); // 해당 글 자료 10개 반환 
		int totArticles = boardDAO.selectTotArticles(); 	// 총 글갯수 반환
		// 조회된 글목록을 ArrayList에 저장한후 다시 articlesMap에 저장
		articlesMap.put("articlesList", articlesList); 		
		// 전체 총 글갯수를 articlesMap에 저장
		articlesMap.put("totArticles", totArticles);    
//		articlesMap.put("totArticles", 170);  // 보통 글이 100개가 안넘으니 글 갯수를 170개로 하여 테스트용
		return articlesMap;
	}
	
//	
//	@Override
//	public int addReply(Map articleMap) throws Exception{
//		int articleNO = boardDAO.insertNewArticle(articleMap);
//		articleMap.put("articleNO", articleNO);
//		boardDAO.insertNewImage(articleMap);
//		return articleNO;
//	}


}
