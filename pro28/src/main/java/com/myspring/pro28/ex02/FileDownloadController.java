package com.myspring.pro28.ex02;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.coobird.thumbnailator.Thumbnails;

@Controller
public class FileDownloadController {
	private static String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";

	@RequestMapping("/download")
	public void download(@RequestParam("imageFileName") String imageFileName,
			                 HttpServletResponse response)throws Exception {
		OutputStream out = response.getOutputStream();
		String filePath = CURR_IMAGE_REPO_PATH + "\\" + imageFileName;
		File image = new File(filePath);

		// 확장자를 제외한 원본 이미지 파일의 이름을 가져옴
		int lastIndex = imageFileName.lastIndexOf(".");
		String fileName = imageFileName.substring(0, lastIndex);
		
		// 원본 이미지 파일 이름과 같은 이름의 썸네일파일에 대한 File객체 생성
		
		//
		
		// 썸네일폴더 생성안하는 방법
		File thumbnail = new File(CURR_IMAGE_REPO_PATH+ "\\" + "thumbnail" + "\\" + fileName + ".png");
		if(image.exists()) {
		    Thumbnails.of(image).size(50,50).outputFormat("png").toOutputStream(out);
		}else {
			return;
		}

		
		byte[] buffer = new byte[1024 * 8];
		out.write(buffer);
		out.close();

	}
}
