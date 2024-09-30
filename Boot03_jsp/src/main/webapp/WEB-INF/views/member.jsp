<%@page import="com.example.boot03.dto.MemberDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/webapp/WEB-INF/views/member.jsp</title>
</head>
<body>
	<div class="container">
		<h2>팀 하나의 정보 보기</h2>
		<p>
			순위 : ${dto.num }, 
			팀이름 : ${dto.name }, 
			국적 : ${dto.addr}
		</p>
	</div>
</body>
</html>