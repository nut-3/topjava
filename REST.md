# users operations
## Admin controller

### GET AdminGet
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users/100000'
```

### POST AdminCreate
```shell
curl --location --request POST 'http://localhost:8080/topjava/rest/admin/users' \
--data-raw '{
  "name": "New2",
  "email": "new2@yandex.ru",
  "password": "passwordNew",
  "roles": [
    "USER"
  ]
}'
```

### PUT AdminUpdate
```shell
curl --location --request PUT 'http://localhost:8080/topjava/rest/admin/users/100000' \
--data-raw '{
  "name": "UserUpdated",
  "email": "user@yandex.ru",
  "password": "passwordNew",
  "roles": [
    "USER"
  ]
}'
```

### GET AdminGetAll
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users'
```

### DEL AdminDelete
```shell
curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/users/100000'
```

### GET GetBy
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users/by?email=admin@gmail.com'
```

### GET AdminGetWithMeals
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users/100001/with-meals'
```

## Profile controller
### GET Get
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/topjava/rest/profile'
```

### PUT Update
```shell
curl --location --request PUT 'http://localhost:8080/topjava/rest/profile' \
--data-raw '{
  "name": "New777",
  "email": "new777@yandex.ru",
  "password": "passwordNew",
  "roles": [
    "USER"
  ]
}'
```

### DEL Delete
```shell
curl --location --request DELETE 'http://localhost:8080/topjava/rest/profile'
```

### GET Text
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/profile/text'
```

### GET GetWithMeals
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/profile/with-meals'
```

# meals operations
### GET GetAll
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals'
```

### GET Get
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100002'
```

### PUT Update
```shell
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100002' \
--data-raw '{
  "dateTime": "2020-02-01T10:00:00",
  "description": "Обновленный завтрак",
  "calories": 200
}'
```

### POST Create
```shell
curl --location --request POST 'http://localhost:8080/topjava/rest/meals' \
--data-raw '{
  "dateTime": "2020-02-01T18:00:00",
  "description": "Созданный ужин",
  "calories": 300
}'
```

### GET GetBetween
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=11:00&endDate=2020-01-30&endTime=21:00'
```

