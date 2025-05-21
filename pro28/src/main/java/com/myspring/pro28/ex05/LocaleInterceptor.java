package com.myspring.pro28.ex05;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class LocaleInterceptor extends  HandlerInterceptorAdapter{ // 사용자정의 인터셉터 상속 필수
	
		// 컨트롤러 실행전 호출
	   @Override
	   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
	      HttpSession session=request.getSession();
	      // 브라우저에서 전달안 locale정보를 가져옴
	      String locale=request.getParameter("locale");
	      if(locale==null)
	         locale="ko";	// 최초 요청시 locale을 한국어로 설정
	      
	      // LOCALE속성값을 세션에 저장해 SesstionLocaleResolver가 사용할수 있게 함
	      session.setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE",new Locale(locale));
	      return true;
	   }

	   // 컨트롤러 실행후 호출
	   @Override
	   public void postHandle(HttpServletRequest request, HttpServletResponse response,
	                           Object handler, ModelAndView modelAndView) throws Exception {
	   }

	   // jsp 실행후 호출
	   @Override
	   public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
	                                    Object handler, Exception ex)    throws  Exception {
	   }
}

