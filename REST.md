# TopJava project REST API documentation
## Admin controller

### Admin get user id=100000
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users/100000'
```

### Admin create new user
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

### Admin update user id=100000
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

### Admin get all users
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users'
```

### Admin delete user id=100000
```shell
curl --location --request DELETE 'http://localhost:8080/topjava/rest/admin/users/100000'
```

### Admin get user by email=admin@gmail.com
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users/by?email=admin@gmail.com'
```

### Admin get user with its meals
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/admin/users/100001/with-meals'
```

## Profile controller

### Get current user
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/topjava/rest/profile'
```

### Update current user
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

### Delete current user 
```shell
curl --location --request DELETE 'http://localhost:8080/topjava/rest/profile'
```

### Get UTF-8 text
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/profile/text'
```

### Get current user with its meals
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/profile/with-meals'
```

# meals operations

### Get all current user's meals
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals'
```

### Get current user's meal id=100002
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100002'
```

### Update current user's meal id=100002
```shell
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100002' \
--data-raw '{
  "dateTime": "2020-02-01T10:00:00",
  "description": "Обновленный завтрак",
  "calories": 200
}'
```

### Create new meal for current user
```shell
curl --location --request POST 'http://localhost:8080/topjava/rest/meals' \
--data-raw '{
  "dateTime": "2020-02-01T18:00:00",
  "description": "Созданный ужин",
  "calories": 300
}'
```

### Get meals for current user filtered by dates ant time period 
```shell
curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=11:00&endDate=2020-01-30&endTime=21:00'
```

