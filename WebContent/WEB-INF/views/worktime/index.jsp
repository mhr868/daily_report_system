<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"/>
            </div>
        </c:if>
		<h2>出勤/退勤報告</h2>
        <c:choose>
            <c:when test="${work_flag}">
                <form method="post" action="<c:url value='/worktime/update'/>">
                    <input type="hidden" name="_token" value="${_token}">
                    <button type="submit">退勤</button>
                    <br/>
                    <br/>
                </form>
            </c:when>
            <c:otherwise>
                <form method="post" action="<c:url value='/worktime/create'/>">
                    <input type="hidden" name="_token" value="${_token}">
                    <button type="submit">出勤</button>
                    <br/>
                    <br/>
                </form>
            </c:otherwise>
        </c:choose>
    </c:param>
</c:import>