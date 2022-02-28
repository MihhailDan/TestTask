Test  Task

1.Mappings

1.1.Get mappings
There are 4 different GET mappings in MainController, each solves own task and used for specific presentation of proxies. Here is short description of every GET mapping:

GET(”/proxies”)
Returns all the proxies without pagination, for overall observation. If there are no elements is database returns Bad Request and message “there are no elements in database”. Otherwise returns all the proxy models in json format.

GET(”/proxies/{pageNum}/{pageSize}”)
Returns all the proxies with pagination parameters provided in URL. If page parameters provided contribute to empty page – returns Bad Request and message “This page is empty”. 

GET(”/proxies/{id}”)
Returns proxy with Id provided in URL. If there is no proxy in database with this Id – returns Bad Request and message “There is no proxy with such id”.

GET(”/proxies/get/{name}/{type}”)
Returns proxy with name and type provided in URL. If there is no match for proxy with these parameters – returns Bad Request and message “There is no proxy with these name and type parameters”

1.2 Post mapping

POST(”/proxies”)
Takes a proxy model* as an input and validates it. If model is valid then it is saved in database, HTTP Created is returned with message “Proxy added”. In case of invalid form, Bad Request is returned with error message.

1.3. Put mapping

PUT(”/proxies/{id}”)
As POST method takes a proxy model* as an input, if there exists proxy with Id provided and input is valid – updates database entry with this id. In case of non-existent Id – Bad request with “There is no proxy with such id”. In case of invalid form returns Bad Request and error message.

1.4 Delete mapping

DELETE(”/proxies/{id}”)
Deletes the proxy with provided Id from database. If there is no proxy with this Id – returns Bad Request and message “There is no proxy with such Id”

*Form to send proxy model.
{
    "name": "abc",
    "type" : "HTTP",
    "hostname" : "abc",
    "port" : 1111,
    "username" : "abc",
    "password" : "abc123",
    "active" : true
}

2.Database

I used postgres 14.2-alpine image to run the database in Docker environment. I created a docker-compose.yml file with all the settings for database. This file can be found in root directory of this repository. To connect to the database application.properties file should be configured with settings of database you try to connect to. 
There is only one migration in this application, which creates a table in database with needed fields and constraints. This can be found in folder db/migrations.


