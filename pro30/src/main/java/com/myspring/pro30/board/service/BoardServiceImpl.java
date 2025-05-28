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
		
		List<ImageVO> imageFileList = (List<ImageVO>)articleMap.get("imageFileList");
		List<ImageVO> modAddimageFileList = (List<ImageVO>)articleMap.get("modAddimageFileList");
		
		if(imageFileList != null && imageFileList.size() != 0) {
			int added_img_num = Integer.parseInt((String)articleMap.get("added_img_num"));
			int pre_img_num = Integer.parseInt((String)articleMap.get("pre_img_num"));

			if (pre_img_num < added_img_num) {
				boardDAO.updateImageFile(articleMap); // 기존 이미지도 수정하고 새 이미지도 추가한 경우
				boardDAO.insertModNewImage(articleMap);
			} else {
				boardDAO.updateImageFile(articleMap); // 기존의 이미지를 수정만 한 경우
			}
		} else if (modAddimageFileList != null && modAddimageFileList.size() != 0) { // 새 이미지를 추가한 경우
			boardDAO.insertModNewImage(articleMap);
		}

		
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
	
	@Override
	public void removeModImage(ImageVO imageVO) throws Exception {
		boardDAO.deleteModImage(imageVO);
	}
}
