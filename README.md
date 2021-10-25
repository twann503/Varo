# Varo

Requirements
 - Have Docker Installed
 - Have Maven Installed
    - If on Mac 'brew install maven'

Build
- In root project directory
- `mvn clean package -DskipTests`
- `cp target/VaroM-1.0-SNAPSHOT.jar postgres`

Starting Micro Service
- Navigate to postgres folder
- `docker-compose up`
- Once docker is up, services should be up
- Follow below for list of endpoints and paramters

# REST API

The REST API

## Get List of Users

### Request
`GET /v1/users/`

    curl -i -H 'Accept: application/json' http://localhost:8080/v1/users/

### Response

  HTTP/1.1 200 
  Content-Type: application/json
  Transfer-Encoding: chunked
  Date: Mon, 25 Oct 2021 17:18:05 GMT
  
  []
  
  
## Create/Update User

### Request
`POST /v1/users/`

curl -X POST 'localhost:8080/v1/users' \
-H 'Content-Type: application/json' \
--data-raw '{
    "firstName": "alex",
    "lastName": "pham",
    "email": "alex_pham@example.com",
    "address": {
        "city": "portland",
        "state": "oregon",
        "address1": "test street",
        "zip": "97201"
    }
}'

### Response

HTTP/1.1 201 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Mon, 25 Oct 2021 17:31:16 GMT

{
    "id": 9,
    "firstName": "alex",
    "lastName": "pham",
    "email": "alex_pham@example.com",
    "address": {
        "address1": "test street",
        "address2": null,
        "city": "portland",
        "state": "oregon",
        "zip": "97201"
    },
    "pastEmails": null
}

## Check if User Exist

### Request
`GET /v1/users?email={email}`

curl -i GET 'localhost:8080/v1/users?email=alex_pham@example.com' 

### Response

HTTP/1.1 200 OK
X-Correlation-Id: aaaaaaaa
Date: Mon, 25 Oct 2021 17:30:13 GMT
Content-Length: 0

## Get User Details

### Request
`GET /v1/users/{id}`

curl -i GET 'localhost:8080/v1/users/1' 

### Response

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Mon, 25 Oct 2021 17:33:26 GMT

{
    "id": 1,
    "firstName": "alex",
    "lastName": "pham",
    "email": "alex_pham@gmail.com",
    "address": {
        "address1": "test street",
        "address2": null,
        "city": "portland",
        "state": "oregon",
        "zip": null
    },
    "pastEmails": []
}

## Delete User

### Request
`DELETE /v1/users/{id}`

curl -i -X DELETE 'localhost:8080/v1/users/1' 

### Response

HTTP/1.1 204 
Date: Mon, 25 Oct 2021 17:42:09 GMT
## Update User Email Address

### Request
`PUT /v1/users/{id}`

curl -i -X PUT 'localhost:8080/v1/users/1' -H 'Content-Type: application/json' --data-raw '{"email": "alex@varo.com"}'

### Response

HTTP/1.1 204 
Date: Mon, 25 Oct 2021 17:42:09 GMT



