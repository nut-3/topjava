<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Добавить еду</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,300italic,700,700italic">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/milligram/1.4.1/milligram.css">
</head>
<body>
<div class="container">
    <form action="" method="post">
        <input type="hidden" id="id" name="id" value="${requestScope.id}">
        <label for="dateTime">Дата/Время</label>
        <input type="datetime-local" id="dateTime" name="dateTime" value="${requestScope.dateTime}" autofocus/>
        <label for="description">Описание</label>
        <input type="text" id="description" name="description" value="${requestScope.description}"/>
        <label for="calories">Калории</label>
        <input type="number" id="calories" name="calories" value="${requestScope.calories}"/>
        <div class="float-right">
            <button class="button-outline" style="color: green; border-color: green" name="buttonPress" value="editMealCancel">Отменить</button>
            <button class="button" style="background-color: green; border-color: green" name="buttonPress" value="editMealSubmit">Сохранить</button>
        </div>
    </form>
</div>
</body>
</html>
