<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <p><c:out value="${month}"/>月の出勤情報</p>
        <table>
            <tbody>
                <tr>
                    <th>日付</th>
                    <th>出勤時間</th>
                    <th>退勤時間</th>
                    <th>勤務状態</th>
                </tr>
                <c:forEach var="worktime" items="${worktimes}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><fmt:formatDate value="${worktime.date}" pattern="MM/dd"/></td>
                        <td><fmt:formatDate value="${worktime.worktime_begin}" pattern="hh:mm"/></td>
                        <td><fmt:formatDate value="${worktime.worktime_finish}" pattern="hh:mm"/></td>
                        <c:choose>
                            <c:when test="${worktime.worktime_finish != null}">
                                <td>退勤済</td>
                            </c:when>
                            <c:when test="${worktime.worktime_begin != null}">
                                <td>出勤中</td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </c:param>
</c:import>