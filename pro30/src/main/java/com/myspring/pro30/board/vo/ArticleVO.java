package com.myspring.pro30.board.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;

import org.springframework.stereotype.Component;

@Component("articleVO")
public class ArticleVO {
	private int level;
	private int articleNO;
	private int parentNO;
	private String title;
	private String content;
	private String imageFileName;
	private String id;
	private Date writeDate;
	
	public ArticleVO() {
		System.out.println("ArticleVO 생성자");
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getArticleNO() {
		return articleNO;
	}

	public void setArticleNO(int articleNO) {
		this.articleNO = articleNO;
	}

	public int getParentNO() {
		return parentNO;
	}

	public void setParentNO(int parentNO) {
		this.parentNO = parentNO;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageFileName() {
		try {
			// 파일의 null과 blank 처리하면서 디코딩
			if(imageFileName != null && imageFileName.length() != 0) {
				imageFileName = URLDecoder.decode(imageFileName, "UTF-8");
			}
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		try {
			//파일이름에 특수문자가 있을 경우 인코딩
			if(imageFileName != null && imageFileName.length() != 0) {
				this.imageFileName = URLEncoder.encode(imageFileName, "UTF-8");
				
			//이미지 파일 없이 첨부될 때 null값으로 초기화 해야함
			}else {
				this.imageFileName = null;
			}
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

}
