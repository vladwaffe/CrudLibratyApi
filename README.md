# CrudLibraryApy

In `folder-path/CrudLibraryApy` in terminal use `docker-compose up --build` to build the container and `docker-compose up` to run without build

After that the documentation for the addresses `http://localhost:8082/swagger-ui/index.html#/` and `http://localhost:8081/swagger-ui/index.html#/`(it is not available because I have some problems with it, namely when I add lombok, swagger for some reason issues "No mapping for GET /v3/api-docs
GET /v3/api-docs" and I havn't idea why at all)

I tried to make authorization a separate service, but I still did not understand how to make it check the token not only on its port, but also on two other microservices


# Reg user
`POST http://localhost:8080/auth/register 
--data-raw '{
    "name":"User1",
    "password":"pass",
    "email":"user1@gmail.com"
}'`

# Generate token
`POST 'http://localhost:8080/auth/token' 
--data-raw '{
    "username":"User1",
    "password":"pass",
}'`

I tried to assemble everything into one multi-module project, but there were always some jokes that some modules that had been working before stopped working and after restarting idee everything worked all over again and so constantly. Therefore, after suffering with this for 4 days, I still abandoned this idea and returned to the original structure. The same goes for the edit regarding the dto, I tried to understand, but something didn't go right. Regarding the naming of packages, I seem to have corrected everything, but I might not have noticed something.
