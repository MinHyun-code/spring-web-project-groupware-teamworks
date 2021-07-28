<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/header/header.jsp"%>


<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/views/header/headerHead.jsp"%>
<!-- 서머노트를 위해 추가해야할 부분 -->
<script
	src="${pageContext.request.contextPath}/board/js/summernote/summernote-lite.js"></script>
<script
	src="${pageContext.request.contextPath}/board/js/summernote/lang/summernote-ko-KR.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/board/js/summernote/summernote-lite.css">
	<script defer src="${pageContext.request.contextPath}/board/js/view.js"></script>
	
<!--  <script src="${pageContext.request.contextPath}/board/js/view.js"></script> -->

<title>Insert title here</title>
</head>

<body>
			<div id="side">
		<button type="button" id="btn1">글쓰기</button>
		<div>
			<a id="button1" class="button1"><span id="span_hover"><img
					alt="image"
					src="${pageContext.request.contextPath}/images/right.png"
					style="width: 16px; height: 12px; transition:0.5s;" id="img1"></span> <span
				id="span_none"><img alt="image"
					src="${pageContext.request.contextPath}/images/white.png"
					style="width: 16px; height: 12px;" id="img1"></span> 게시판</a>
			<ol id="scroll" style="display: none; list-style: none;">
				<c:forEach var="bs" items="${listBoard }">
				<li class="li">${bs.bd_name}</li>
				</c:forEach>

			</ol>
		</div>
	</div>

	<div id="content">
		<input type="hidden" id="m_id" value="${view.m_id}">
		<input type="hidden" id="loginId" value="${view.loginId}">
		<form
			action="${pageContext.request.contextPath}/board/update?p_num=${view.p_num}&m_id=${view.m_id}&loginId=${view.loginId}"
			method="post">
			<sec:csrfInput />
			<section id="point">
				<article class="pt pt1">
					<table class="pt_tb">
						<tr>
							<td id="p_title" name="p_title" value="${view.p_name}">글 확인하기</td>
						</tr>
						<tr>
							<td class="td_subject"><input id="subject" type="text"
								name="p_name" value="${view.p_name}" placeholder="글 제목"
								class="form_box"></td>
							<td><button type="button" id="buttonRecommand" onclick="location.href='${pageContext.request.contextPath}/board/recommend?p_num=${view.p_num}'">
									<c:if test="${statusOfLike == 0}">
										좋아요									
									</c:if>
									<c:if test="${statusOfLike == 1}">
										좋아요 취소									
									</c:if>
								</button></td>
							<td>조회수: ${view.p_view}</td>
						</tr>
						<tr>
							<td colspan="5"><textarea id="summernote" class="summernote"
									name="p_content" placeholder="글 내용" rows="15" readonly >${view.p_content}</textarea>

								</td>
						</tr>
					</table>
				</article>
			</section>
			<button type="submit" id="btnUpdete" >수정</button>
			<button type="button" id="btnDelete" 
				onclick="location.href='${pageContext.request.contextPath}/board/delete?p_num=${view.p_num}'">삭제</button>
					<button type="submit" id="btnUpdete"onclick="location.href='${pageContext.request.contextPath}/board'">등록</button>
					</form>
				<div>
				<form action="${pageContext.request.contextPath}/board/reply_insert?p_num=${view.p_num}&loginId=${view.loginId}"
			method="post">
					<sec:csrfInput />
					<table border="2"   style="width: 750px; height:30px;  "><tr><td><textarea rows = "2" cols = "5" id = "rp_content" name = "rp_content"></textarea></td></tr></table>
					<button type="submit" id="btnUpdete"onclick="location.href='${pageContext.request.contextPath}/board/view'">댓글등록</button>
					<!--  <button type="submit" id="btnUpdete"onclick="location.href='${pageContext.request.contextPath}/board/view">댓글삭제</button>-->
					
					
					<table id="list_table" style="margin-top: 4%; border-top: 2px solid #EAEAEA;">
						<c:forEach var="reply" items="${listReply}">
							<tr id="rp_${reply.rp_num}">								
								<td>
									<c:if test="${reply.rp_depth > 1}">
										<c:forEach begin="3" end="${reply.rp_depth}" step="1">
											&nbsp;&nbsp;&nbsp;
										</c:forEach>
										ㄴ
									</c:if>
									${reply.rp_content}
								
								</td>
								<td><button type="button" id="btnUpdete"onclick="clickDelete(${reply.rp_num})">삭제</button></td>
							</tr>
						</c:forEach>
						
					</table>
				</form>
	</div>
	</div>

	</div>
		
	<%@include file="/WEB-INF/views/header/headerFooter.jsp"%>
</body>
</html>