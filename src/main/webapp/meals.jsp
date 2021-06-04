<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,300italic,700,700italic">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/milligram/1.4.1/milligram.css">
</head>
<body>
<div class="container">
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <table>
        <c:forEach var="meal" items="${requestScope.meals}">
            <tr style="color:
            <c:choose>
            <c:when test="${meal.excess==true}">
                    red
            </c:when>
            <c:otherwise>
                    green
            </c:otherwise>
                    </c:choose>">
                <td><c:out value="${meal.dateTime.format(DateTimeFormatter.ofPattern(\"d-M-y H:m\"))}"/></td>
                <td><c:out value="${meal.description}"/></td>
                <td><c:out value="${meal.calories}"/></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>