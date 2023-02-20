Add directory Upload and update the path in application.properties

Database schema: https://dbdiagram.io/d/63d53775296d97641d7cb7f6

_All /api/ endpoints need authorization with bearer token._

REST EndPoints for working with update files:
1. GET /api/updatefiles/lastFile — get the last uploaded file in JSON
2. GET /api/updatefiles/lastFile/{forClients} - get the last uploaded file with {forClients} or with ALL
3. GET /api/updatefiles/allFiles — get all files in JSON
4. GET /api/updatefiles/getFile/{id} — get file with {id} in JSON
5. POST /api/updatefiles/uploadFile — upload file; put file in body request
6. GET /api/updatefiles/downloadFile/{fileName} — download file with {filename}
7. DELETE /api/updatefiles/deleteFile/{id} — delete file with {id}
8. POST /api/updatefiles/editFileInfo/{id} - change info and forClients fields for file with {id}

REST EndPoints for working with price list files:
1. GET /api/pricelists/lastFile — get the last uploaded file in JSON
2. GET /api/pricelists/lastFile/{forClients} - get the last uploaded file with {forClients} or with ALL
3. GET /api/pricelists/allFiles — get all files in JSON
4. GET /api/pricelists/getFile/{id} — get file with {id} in JSON
5. POST /api/pricelists/uploadFile — upload file; put file in body request
6. GET /api/pricelists/downloadFile/{fileName} — download file with {filename}
7. DELETE /api/pricelists/deleteFile/{id} — delete file with {id}
8. POST /api/updatefiles/editFileInfo/{id} - change info and forClients fields for file with {id}

REST Endpoints for authorization:
1. POST /register - register user
2. POST /auth - login user; return access token + refresh token
3. POST /token - return new access token using refresh token
4. GET /me - check user auth; return role
5. POST /api/refreshToken - return new refresh token using old refresh token
6. POST /forgottenPassword - send restoring token for resetting password on user's email
7. POST /resetPassword - takes restoring token with new password and changes in db

REST Endpoints for user management:
1. GET /api/users/getUser/{id} - get user via id (only for admin and moderator)
2. GET /api/users/getAllUsers - get all users in short info form (only for admin and moderator)
3. PUT /api/users/editUser - edit user fields (only for user with greater role + only for admin and moderator)
4. POST /api/users/connectUserAndManager - connect user with key-manager (only for admin and moderator)
5. GET /api/users/getAllUsersWithoutKeyManagers - get all users (shops, suppliers and users) without key-manager (only for admin and moderator)
6. GET /api/users/getMyUsers - get all users associated with this key-manager (only for user itself, only for key-manager)
7. PUT /api/changeOwnPassword - change password of authenticated user (only for user itself)
8. GET /api/getUserInfo - get user fields (only for user itself)
9. PUT /api/editOwnInfo - edit user fields (only for user itself)

REST Endpoints for logging activity:
1. GET /api/allActivities - get all user activities

REST Endpoints for logging application activities:
1. GET /api/app/allCalcActivities - get all activities from calculator app
2. POST /api/app/saveCalcActivity - saves activity from calculator app
3. POST /api/app/calcActivityFilter - returns filtered calculator activities
4. POST /api/app/calcActivityFilterFile - returns filtered calculator activities in .xlsx

REST Endpoints for system information:
1. GET /api/getApplicationStart - get time the app started
2. GET /api/getApplicationWorkingTime - calculates the period the app is working
3. POST /api/getUserStatistics - returns statistics of new users, online users and new receipts for day, week and month

REST Endpoint for user online status management:
1. GET /api/pingAlive - request from client to ping that user is active
2. GET /api/showUserStats - get all active users for the last N minutes

Docker:

1. Dockerfile - template for app image
2. docker-compose_dev.yml, docker-compose_test.yml, 
docker-compose_production.yml - make images for 3 
different pack of containers, for dev, test and production.
3. for deploy this on server:
4. Build artefact (latest .jar executable file of project)
5. Build new Docker image. For this: use command:

   ```docker build -t florence76/koreanikaserverapp .```
"." means that Dockerfile into you workdir
6. login: ```docker login```
7. push you image to dockerhub: ```docker image push florence76/koreanikaserverapp```
8. Next use Docker-compose 
9. to start : ```docker compose -f docker-compose_production.yml up -d```
10. to stop: ```docker compose -f docker-compose_production.yml down```

!!! check that in local storage or on Docker hub is the newest version of image!!!
- check ports it cat be uses another servises: ```sudo netstat -ntlp | grep 80```
- check permissions for volume folders

portal:

vm:
portal.koreanika.ru
log:root
pw: koreanikaPortal76!


##About the filter into calculator event table:

### GET ALL EVENTS
__request:__

method: GET   /api/app/allCalcActivities - return all events, happens in Calculator app

body - empty

__response:__

```json
{
  "events": [
    {
      "id": 23,
      "activityTime": "2023-01-15T07:16:35.645686Z",
      "userId": 1,
      "login": "userName",
      "companyName": "Company Name",
      "certainPlaceAddress": "Moscow",
      "type": "show receipt",
      "materials": "Quarz0-subtype0-color0,Acryl1-subtype1-color1,Acryl2-subtype2-color2",
      "materialPrice": 12345.34,
      "addPrice": 12345.34,
      "allPrice": 12345.34,
      "mainCoeff": 1.6,
      "materialCoeff": 1.6,
      "slabs": 4,
      "productSquare": 4
    },
    {
      "id": 24,
      "activityTime": "2023-01-15T07:16:35.645686Z",
      "userId": 1,
      "login": "userName",
      "companyName": "Company Name",
      "certainPlaceAddress": "Moscow",
      "type": "show receipt",
      "materials": "Quarz0-subtype0-color0,Acryl1-subtype1-color1,Acryl2-subtype2-color2",
      "materialPrice": 12345.34,
      "addPrice": 12345.34,
      "allPrice": 12345.34,
      "mainCoeff": 1.6,
      "materialCoeff": 1.6,
      "slabs": 4,
      "productSquare": 4
    },
    {
      "id": 25,
      "activityTime": "2023-01-15T07:16:35.645686Z",
      "userId": 1,
      "login": "userName",
      "companyName": "Company Name",
      "certainPlaceAddress": "Moscow",
      "type": "show receipt",
      "materials": "Quarz0-subtype0-color0,Acryl1-subtype1-color1,Acryl2-subtype2-color2",
      "materialPrice": 12345.34,
      "addPrice": 12345.34,
      "allPrice": 12345.34,
      "mainCoeff": 1.6,
      "materialCoeff": 1.6,
      "slabs": 4,
      "productSquare": 4
    }
  ]
}

```

### GET FILTERED EVENT LIST
__request:__

method: POST   /api/app/calcActivityFilter - return all events, happens in apps, 
fit to filter conditions

body :

```json
{
  "dateFrom": "2015-12-05",
  "dateTo": "2023-01-18",
  "companyName": "Zetta",
  "certainPlaceAddress": "Zetta",
  "materialPriceFrom": "1000",
  "materialPriceTo": "1000",
  "addPriceFrom": "1500",
  "addPriceTo": "1500",
  "allPriceFrom": "3000",
  "allPriceTo": "4000",
  "materials": "Quarz0-subtype0-color0,Acryl1-subtype1-color1,Acryl2-subtype2-color2"
}
```

if the parameter is not used - field is empty ("")

__response:__

As in "Get all events", but filtered