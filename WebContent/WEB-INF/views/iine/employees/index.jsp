<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
	<c:param name="content">
	    <h2>日報をいいねした従業員一覧</h2>
	    <table>
	        <tr>
	            <th>氏名</th>
	        </tr>
	        <c:forEach var="employee" items="${iine_employees}" varStatus="status">
                <tr class="row${status.count % 2}">
                    <td><c:out value="${employee.name}"/></td>
                </tr>
			</c:forEach>
	    </table>

	    <div id="pagination">
	    (全 ${iine_employees_count} 件) <br />
	        <c:forEach var="i" begin="1" end="${(iine_employees_count - 1) / 15 + 1}" step="1">
                <c:choose>
                    <c:when test="${page == i}">
                        <c:out value="${i}"></c:out>&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/iine/employees/index?id=${id}&page=${i}' />"><c:out value="${i}"></c:out>&nbsp;</a>
                    </c:otherwise>
                </c:choose>
			</c:forEach>
	    </div>
  	</c:param>
</c:import>