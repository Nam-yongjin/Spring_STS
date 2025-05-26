<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
request.setCharacterEncoding("UTF-8");
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="article"  value="${articleMap.article}"  />
<c:set var="imageFileList"  value="${articleMap.imageFileList}"  />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글보기</title>
<style>
#tr_btn_modify {
	display: none;
}
</style>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script> 
<script type="text/javascript">
	// 리스트로 돌아가기 버튼
	function backToList(obj) {
		obj.action = "${contextPath}/board/listArticles.do";
		obj.submit();
	}
	// 이미지 있을때 수정
	 function fn_enable(obj){
		 document.getElementById("i_title").disabled=false;
		 document.getElementById("i_content").disabled=false;
		 // <input>태그의 속성 id는의  필요성: 자바스크립트에서 사용할 때 name이 아닌 id를 인용한다. 
		 if(document.getElementById("originalFileName") != null) {
			 document.getElementById("i_imageFileName").disabled=false;
		 }
		 document.getElementById("tr_btn_modify").style.display="block";	// 활성화
		 document.getElementById("tr_btn").style.display="none";	// 비활성화
	 }
	//이미지없을때 수정
     function fn_enable2(obj){
         document.getElementById("i_title").disabled=false;
		 document.getElementById("i_content").disabled=false;
		 document.getElementById("tr_btn_modify").style.display="block";  // 활성화
		 document.getElementById("tr_btn").style.display="none";  // 비활성화
     }

	 // 글수정시 수정한데이터전송
	 function fn_modify_article(obj){
		 obj.action="${contextPath}/board/modArticle.do";
		 obj.submit();
	 }

	 // DOM태그 생성
	 function fn_remove_article(url,articleNO){
		 // 자바스크립트를 이용해 동적으로 <form>태그 생성
	     var form = document.createElement("form");
		     form.setAttribute("method", "post");
		     form.setAttribute("action", url);
		 // 자바스크립트를 이용해 동적으로 <input>태그를 생성후 name과 value를 articleNO와 컨트롤러로 글번호로 설정 
	     var articleNOInput = document.createElement("input");
		     articleNOInput.setAttribute("type","hidden");
		     articleNOInput.setAttribute("name","articleNO");
		     articleNOInput.setAttribute("value", articleNO);
		 
		 // 동적으로 생성된 <input>태그를 동적으로 생성한 <form>태그에 append
	     form.appendChild(articleNOInput);
	     document.body.appendChild(form);  // 완성한 태그를 html에 붙이기
		 form.submit();	 // 전송
	 }

	 function readURL(input) {
	     if (input.files && input.files[0]) {  // files는 선택한 모든 파일을 나열하는FileList 객체
	         var reader = new FileReader();  // 업로한 이미지 미리보기하게 하는 FileReader API
	         reader.onload = function (e) {         //  파일 읽어 들이기 성공했을 때 호출되는 onload 이벤트 핸들러
	             $('#preview').attr('src', e.target.result);  // <img>태그의 src속성에 읽어 들인 File내용을 지정 
	         }
	         reader.readAsDataURL(input.files[0]);  //File내용을 읽어 DataURL형식의 문자열로 저장
	     }
	 }  
	 
	 // 해당 글번호에 대한 답글 쓰기
	 // articleNO->parentNO(DOM->script)
	 function fn_reply_form(url, parentNO){
	      var form = document.createElement("form");
	      form.setAttribute("method", "post");
	      form.setAttribute("action", url);
	      var parentNOInput = document.createElement("input");
	      parentNOInput.setAttribute("type","hidden");
	      parentNOInput.setAttribute("name","parentNO");
	      parentNOInput.setAttribute("value", parentNO);
		 
	      form.appendChild(parentNOInput);
	      document.body.appendChild(form);
	  	  form.submit();
	 }


</script>

</head>
<body>
	<form name="frmArticle" method="post" action="${contextPath}" enctype="multipart/form-data">
		<table border="0" align="center">
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">글번호</td>
				<td>
					<input type="text" value="${article.articleNO }" disabled />
					<%-- 글수정시 글번호를 컨트롤러로 전송하기위해 hidden으로 미리 글번호 저장 --%>
					<input type="hidden" name="articleNO" value="${article.articleNO}" />
				</td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">작성자 아이디</td>
				<td>
					<input type="text" value="${article.id }" name="id" disabled />
				</td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">제목</td>
				<td><input type="text" value="${article.title }" name="title" id="i_title" disabled /></td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">내용</td>
				<td>
					<textarea rows="20" cols="60" name="content" id="i_content"disabled />${article.content }</textarea>
				</td>
			</tr>
			<c:if test="${not empty imageFileList && imageFileList!='null' }">
				<c:forEach var="item" items="${imageFileList}" varStatus="status">
					<tr>
						<td width="150" align="center" bgcolor="#FF9933" rowspan="2">이미지${status.count }</td>
						<td><input type="hidden" name="originalFileName"
							value="${item.imageFileName }" /> 
							<img src="${contextPath}/download.do?articleNO=${article.articleNO}&imageFileName=${item.imageFileName}" 
								id="preview" /><br>
						</td>
					</tr>
					<tr>
						<td><input type="file" name="imageFileName "
							id="i_imageFileName" disabled onchange="readURL(this);" /></td>
					</tr>
				</c:forEach>
			</c:if>
			<tr>
				<td width="20%" align="center" bgcolor="#FF9933">등록일자</td>
				<td><input type=text
					value="<fmt:formatDate value="${article.writeDate}" />" disabled />
				</td>
			</tr>
			<tr id="tr_btn_modify">
				<td colspan="2" align="center">
				<input type=button value="수정반영하기" onClick="fn_modify_article(frmArticle)"> 
				<input type=button value="취소" onClick="backToList(frmArticle)">
				</td>
			</tr>
			<tr id="tr_btn">	
				<td colspan=2 align="center">
				
				<!-- 본인이면 수정 및 삭제 가능 -->
				<c:if test="${member.id == article.id }">
					<!--  이미지 있을 때 -->
	   				<c:if test="${not empty article.imageFileName && article.imageFileName!='null' }"> 
	    				<input type=button value="수정하기" onClick="fn_enable(this.form)">
	    			</c:if>
	     			<!--  이미지 없을 때 -->
	    			<c:if test="${empty article.imageFileName || article.imageFileName =='null' }"> 
	   					<input type=button value="수정하기" onClick="fn_enable2(this.form)">
	    			</c:if>
    			<%-- 삭제하기 클릭시 함수호출하면서 articleNO전달 --%>
					<input type=button value="삭제하기"
						onClick="fn_remove_article('${contextPath}/board/removeArticle.do', ${article.articleNO})">
				
				</c:if>	
				<input type=button value="리스트로 돌아가기" onClick="backToList(this.form)"> 
				<input type=button value="답글쓰기"
					onClick="fn_reply_form('${contextPath}/board/replyForm.do', ${article.articleNO})">
				</td>
			</tr>
		</table>
	</form>
</body>

</html>