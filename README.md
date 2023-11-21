# <u>Online store</u>
## We are the J9 development team.

According to the technical task of the SkyPro company, it is necessary to assemble the backend part of the site in Java.

**The backend part of the project involves the implementation of the following functionality:**

- Authorization and authentication of users.
- Distribution of roles between users: user and administrator.
- CRUD for ads on the site: the administrator can delete or edit all ads, and users — only their own.
- Under each ad, users can leave reviews  and coments.
- In the site header, you can search for ads by name.
- Show and save images of ads.
<hr>

### The front-end functionality is described in [openapi.yaml](openapi.yaml).

**To start, you need:**
1. Clone the project to the development environment</li>
2. Configure the database and register the values in the file **[application.properties](src/main/resources/application.properties)** 
3. Download **[Docker](https://www.docker.com)** and run it.
4. Download and run the Docker image using the command ```docker run --rm -p 3000:3000 ghcr.io/bizinmitya/front-react-avito:v1.19``` 
5. Run the method **main** in the file **[HomeworkApplication.java](src/main/java/ru/skypro/homework/HomeworkApplication.java)** 
<hr>

### Functionality for the user:
___**An anonymous user** can:___
1. View a list of all ads.
2. Register on the website.
   
___**An authorized user** can:___
1. View a list of all ads
2. View each ad.
3. Create an ad.
4. Edit and delete your ad.
5. View all comments on ads.
6. Create comments on any ads.
7. Edit/delete your comments.

___**The administrator** can additionally:___
1.  Edit and delete all ad.
2.  Edit/delete all comments.
<hr>

### Development team J9 are taking part in the development of this project:
* [Квашин Виталий](https://github.com/Vitalik2142VK)
* [Чикин Сергей](https://github.com/SergeyChikin)
* [Ермакова Анна]( https://github.com/anna-ermakova)
* [Василенко Сергей](https://github.com/demoB33)
* [Морозов Степан](https://github.com/hardr1se)
* [Севостьянов Роман](https://github.com/RomanSevostianov)
<hr>

## Development tools:

* **Main project**:
    - Java 11
    - Maven
    - Spring Boot
    - Spring Web
    - Spring Data JPA
    - Spring Security
    - Swagger
    - Lombok
* **Data base**:
    - PostgreSQL
    - Liquibase
