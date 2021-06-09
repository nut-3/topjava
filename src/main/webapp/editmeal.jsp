<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>"${requestScope.meal.id == null ? 'Добавить' : 'Редактировать'}" еду</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,300italic,700,700italic">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/milligram/1.4.1/milligram.css">
</head>
<body>
<div class="container">
    <h2>${requestScope.meal.id == null ? 'Добавить' : 'Редактировать'} еду</h2>
    <form action="" method="post">
        <input hidden name="id" value="${requestScope.meal.id}">
        <label for="dateTime">Дата/Время</label>
        <input type="datetime-local" id="dateTime" name="dateTime" value="${requestScope.meal.dateTime}" autofocus/>
        <label for="description">Описание</label>
        <input type="text" id="description" name="description" value="${requestScope.meal.description}"/>
        <label for="calories">Калории</label>
        <input type="number" id="calories" name="calories" value="${requestScope.meal.calories}"/>
        <div class="float-right">
            <button class="button-outline" style="color: green; border-color: green" name="action" value="cancel" formmethod="get">
                Отменить
            </button>
            <button class="button" style="background-color: green; border-color: green" name="button"
                    value="save">Сохранить
            </button>
        </div>
    </form>
</div>
</body>
</html>
