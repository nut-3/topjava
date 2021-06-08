<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
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
    <form action="" method="get">
        <button class="button float-left" style="background-color: green; border-color: green" name="edit">Добавить
        </button>
    </form>
    <table>
        <thead>
        <tr>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="meal" items="${requestScope.meals}">
            <tr style="${meal.excess ? 'color: red' : 'color: green'}">
                <td>${meal.dateTime.format(TimeUtil.JSP_FORMATTER)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>
                    <form action="" method="get">
                        <button class="button float-right" style="background-color: blue; border-color: blue"
                                name="edit" value="${meal.id}">Изменить
                        </button>
                    </form>
                </td>
                <td>
                    <form action="" method="post">
                        <input hidden name="id" value="${meal.id}">
                        <button class="button float-right" style="background-color: red; border-color: red"
                                name="button" value="delete">Удалить
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>