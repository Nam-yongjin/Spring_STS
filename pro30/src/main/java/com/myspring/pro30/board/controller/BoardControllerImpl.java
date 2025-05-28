package com.myspring.pro30.board.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;
import com.myspring.pro30.member.vo.MemberVO;



@Controller("boardController")
public class BoardControllerImpl implements BoradController{

	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";

	@Autowired
	private BoardService boardService;
	@Autowired
	private ArticleVO articleVO;
	
	@Override
	@RequestMapping(value= "/board/listArticles.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticles(@RequestParam(value="section", required=false) String _section,
			@RequestParam(value="pageNum", required=false) String _pageNum,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		int section = Integer.parseInt(((_section == null) ? "1" : _section));
		int pageNum = Integer.parseInt(((_pageNum == null) ? "1" : _pageNum));
		Map pagingMap = new HashMap();
		pagingMap.put("section", section);
		pagingMap.put("pageNum", pageNum);
		Map articlesMap=boardService.listArticles(pagingMap);
		articlesMap.put("section", section);
		articlesMap.put("pageNum", pageNum);
		
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesMap", articlesMap);
		return mav;
		
	}

	// 한개 이미지 글쓰기  -->>  다중 이미지 포함 글쓰기
	@Override
	@RequestMapping(value = "/board/addNewArticle.do", method = {RequestMethod.POST })
	@ResponseBody
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest,
							HttpServletRequest request, HttpServletResponse response) throws Exception{
		multipartRequest.setCharacterEncoding("utf-8");
		String imageFileName=null;
		
		Map articleMap = new HashMap();
		Enumeration enu = multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			articleMap.put(name,value);
		}

		//로그인 시 세션에 저장된 회원 정보에서 글쓴이 아이디를 얻어와서 Map에 저장
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO)session.getAttribute("member");
		String id = memberVO.getId();
		articleMap.put("id", id);
		
		String parentNO = (String)session.getAttribute("parentNO")  ;
		articleMap.put("parentNO" , (parentNO == null ? 0 : parentNO));
			
		List<String> fileList =upload(multipartRequest);
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		if(fileList!= null && fileList.size()!=0) {
			for(String fileName : fileList) {
				ImageVO imageVO = new ImageVO();
				imageVO.setImageFileName(fileName);
				imageFileList.add(imageVO);
			}
			articleMap.put("imageFileList", imageFileList);
		}
		
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		try {
			int articleNO = boardService.addNewArticle(articleMap);
			if(imageFileList!=null && imageFileList.size()!=0) {
				for(ImageVO  imageVO:imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
					FileUtils.moveFileToDirectory(srcFile, destDir,true);
				}
			}		    
			message = "<script>";
			message += " alert('새글을 추가했습니다.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO=" + articleMap.get("articleNO") + "';" ;
			message +=" </script>";
	       	resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);   		 
		 }catch(Exception e) {
			if(imageFileList!=null && imageFileList.size()!=0) {
			  for(ImageVO  imageVO:imageFileList) {
			  	imageFileName = imageVO.getImageFileName();
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			 	srcFile.delete();
			  }
			}
			message = " <script>";
			message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');";
			message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
			message +=" </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		 }
		return resEnt;
	  }
	
	@RequestMapping(value = "/board/*Form.do", method = {RequestMethod.GET, RequestMethod.POST})
	private ModelAndView form(@RequestParam(value = "parentNO", required = false) String parentNO,
					HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		if(viewName.equals("/board/replyForm")) {
			HttpSession session = request.getSession();
			if(parentNO != null)  {
				session.setAttribute("parentNO", parentNO);
			}
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
	
	// 다중 이미지 업로드하기
	private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception {
		List<String> fileList = new ArrayList<String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			if (originalFileName != "" && originalFileName != null) { // File Null Check
				fileList.add(originalFileName);
				File file = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);
				if (mFile.getSize() != 0) {
					if (!file.exists()) { // 경로상에 파일이 존재하지 않을 경우
						file.getParentFile().mkdirs(); // 경로에 해당하는 디렉토리들을 생성
						mFile.transferTo(new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + originalFileName)); 
						// 임시로 저장된 multipartFile을 실제 파일로 전송
					}
				}
			}
		}
		return fileList;
	}

	//다중 이미지 보여주기
	@RequestMapping(value="/board/viewArticle.do" ,method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
			@RequestParam(value="removeCompleted", required=false) String removeCompleted,
								HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		Map articleMap = boardService.viewArticle(articleNO);
		articleMap.put("removeCompleted", removeCompleted );
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("articleMap", articleMap);
		return mav;
	}
	
	// 한개 이미지 수정 기능
	@RequestMapping(value = "/board/modArticle.do", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest,  
		    										HttpServletResponse response) throws Exception{
		multipartRequest.setCharacterEncoding("utf-8");		
		
		Map articleMap = new HashMap();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			
			if(name.equals("imageFileNO")) {
				String[] values = multipartRequest.getParameterValues(name);
				articleMap.put(name,values);
			}else if(name.equals("oldFileName")) {
				String[] values = multipartRequest.getParameterValues(name);
				articleMap.put(name,values);
			}else {
				String value = multipartRequest.getParameter(name);
				articleMap.put(name,value);
			}
			
			
		}

		List<String> fileList = uploadModImageFile(multipartRequest);
		
		int added_img_num = Integer.parseInt((String)articleMap.get("added_img_num"));
		int pre_img_num = Integer.parseInt((String)articleMap.get("pre_img_num"));	
		
//		기존의 이미지를 수정해서 첨부한 이미지는 imageFileList에, 새로 추가한 이미지는 modAddImageFileList에 저장한다.
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		List<ImageVO> modAddimageFileList = new ArrayList<ImageVO>();
		
		if(fileList!= null && fileList.size()!=0) {
			String[] imageFileNO = (String[])articleMap.get("imageFileNO");
			for(int i = 0; i < added_img_num; i++) {
				String fileName = fileList.get(i);
				ImageVO imageVO = new ImageVO();
				if(i < pre_img_num) {
					imageVO.setImageFileName(fileName);
					imageVO.setImageFileNO(Integer.parseInt(imageFileNO[i]));
					imageFileList.add(imageVO);
					articleMap.put("imageFileList", imageFileList);
				}else {
					imageVO.setImageFileName(fileName);
					modAddimageFileList.add(imageVO);
					articleMap.put("modAddimageFileList", modAddimageFileList);
				}
			}
			
		}
				
		String articleNO = (String)articleMap.get("articleNO");
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
	    try {
			boardService.modArticle(articleMap);
			if (fileList != null && fileList.size() != 0) {
				for (int i = 0; i < fileList.size(); i++) {
					String fileName = fileList.get(i);
					if(i<pre_img_num) {
						if(fileName != null) {
							File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);
							File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
							FileUtils.moveFileToDirectory(srcFile, destDir, true);
							
							   // 기존 이미지 파일명이 oldFileName으로 map에서 가져와 삭제한다
							String[] oldName = (String[])articleMap.get("oldFileName");
							String oldFileName = oldName[i];
							
							File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+oldFileName);
						    oldFile.delete();
						}
					}else {
						File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);
						File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
						FileUtils.moveFileToDirectory(srcFile, destDir, true);
					}
				}
			    
			}
			
			message = "<script>";
	        message += " alert('글을 수정했습니다.');";
	        message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
	        message +=" </script>";
	        resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			if (fileList != null && fileList.size() != 0) {
				for (int i = 0; i < fileList.size(); i++) {
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileList.get(i));
					srcFile.delete();
				}
				e.printStackTrace();
			}
			message = "<script>";
			message += " alert('오류가 발생했습니다.다시 수정해주세요');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}
		return resEnt;

	}
	
	@Override
	@RequestMapping(value = "/board/removeArticle.do", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html; charset=UTF-8");
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		try {
			boardService.removeArticle(articleNO);
			File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
			FileUtils.deleteDirectory(destDir);
			
			message = "<script>";
			message += " alert('글을 삭제했습니다.');";
			message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		}catch(Exception e) {
			message = "<script>";
			message += " alert('작업중 오류가 발생했습니다.다시 시도해 주세요.');";
			message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		    e.printStackTrace();
		}
		return resEnt;

	}

	@RequestMapping(value = "/board/removeModImage.do", method = RequestMethod.POST)
	@ResponseBody
	public void removeModImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		PrintWriter writer = response.getWriter();

		try {
			String imageFileNO = (String) request.getParameter("imageFileNO");
			String imageFileName = (String) request.getParameter("imageFileName");
			String articleNO = (String) request.getParameter("articleNO");
			
			System.out.println("imageFileNO = " + imageFileNO);
			System.out.println("articleNO = " + articleNO);

			ImageVO imageVO = new ImageVO();
			imageVO.setArticleNO(Integer.parseInt(articleNO));
			imageVO.setImageFileNO(Integer.parseInt(imageFileNO));
			boardService.removeModImage(imageVO);
			
			File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + imageFileName);
			oldFile.delete();
			
			writer.print("success");
		} catch (Exception e) {
			writer.print("failed");
		}

	}
	
	private List<String> uploadModImageFile(MultipartHttpServletRequest multipartRequest) throws Exception {
		List<String> fileList = new ArrayList<String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			if (originalFileName != "" && originalFileName != null) {
				fileList.add(originalFileName);
				File file = new File(ARTICLE_IMAGE_REPO + "\\" + fileName);
				if (mFile.getSize() != 0) { // File Null Check
					if (!file.exists()) { 
						file.getParentFile().mkdirs();
						mFile.transferTo(new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + originalFileName)); // �ӽ÷�
					}
				}
			} else {
				fileList.add(null);
			}

		}
		return fileList;
	}
	
}
