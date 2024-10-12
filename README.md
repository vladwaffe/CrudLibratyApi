# CrudLibraryApy

In `folder-path/CrudLibraryApy` in terminal use `docker-compose up --build` to build the container and `docker-compose up` to run without build

After that the documentation for the addresses `http://localhost:8082/swagger-ui/index.html#/` and `http://localhost:8081/swagger-ui/index.html#/`

I tried to make authorization a separate service, but I still did not understand how to make it check the token not only on its port, but also on two other microservices


#Reg user
POST http://localhost:8080/auth/register 
--data-raw '{
    "name":"User1",
    "password":"pass",
    "email":"user1@gmail.com"
}'

#generate token
POST 'http://localhost:9898/auth/token' 
--data-raw '{
    "name":"User1",
    "password":"pass",
}'

