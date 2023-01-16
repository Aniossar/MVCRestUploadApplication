Add directory Upload and update the path in application.properties

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
1. PUT /api/changeOwnPassword - change password of authenticated user
2. DELETE /api/users/deleteUser - delete user (only for user with greater role)
3. PUT /api/users/editUser - edit user fields (only for user with greater role)
4. PUT /api/users/editUserRole - edit user Role field (only for admin)
5. PUT /api/users/editUserEnabled - edit user Enabled field (only for admin)

REST Endpoints for logging activity:
1. GET /api/allActivities - get all user activities
2. POST /api/saveCalculatorActivity - saves activity from calculator app

REST Endpoints for system information:
1. GET /api/getApplicationStart - get time the app started
2. GET /api/getApplicationWorkingTime - calculates the period the app is working

REST Endpoint for user online status management:
1. GET /api/pingAlive - request from client to ping that user is active
2. GET /api/showUserStats - get all active users for the last N minutes

Docker:

1. Dockerfile - template for app image
2. docker-compose_dev.yml, docker-compose_test.yml, 
docker-compose_production.yml - make images for 3 
different pack of containers, for dev, test and production.
3. for deploy this on server:




##About the filter into calculator event table:

### GET ALL EVENTS
__request:__

method: POST   /api/appEvents/list - return all events, happens in apps

body - empty

__response:__

```json
{
  "events": [
    {
      "id": 23,
      "date": "12:30 22.12.2022",
      "activity_type": "calculator",
      "user_login" : "userName",
      "message":{
        "type":"show receipt",
        "materials":"Quarz0-subtype0-color0,Acryl1-subtype1-color1,Acryl2-subtype2-color2",
        "materialPrice":"12345.34",
        "addPrice":"12345.34",
        "allPrice":"12345.34",
        "mainCoeff":"1.6",
        "materialCoeff":"1.6",
        "slabs":"4",
        "productSquare":"4"
      }
    },
    {
      "id": 24,
      "date": "12:35 22.12.2022",
      "activity_type": "calculator",
      "user_login" : "userName",
      "message":{
        "type":"show receipt",
        "materials":"Quarz0-subtype0-color0,Acryl1-subtype1-color1,Acryl2-subtype2-color2",
        "materialPrice":"12345.34",
        "addPrice":"12345.34",
        "allPrice":"12345.34",
        "mainCoeff":"1.6",
        "materialCoeff":"1.6",
        "slabs":"4",
        "productSquare":"4"
      }
    },
    {
      "id": 25,
      "date": "12:40 22.12.2022",
      "activity_type": "calculator",
      "user_login" : "userName",
      "message":{
        "type":"show receipt",
        "materials":"Quarz0-subtype0-color0,Acryl1-subtype1-color1,Acryl2-subtype2-color2",
        "materialPrice":"12345.34",
        "addPrice":"12345.34",
        "allPrice":"12345.34",
        "mainCoeff":"1.6",
        "materialCoeff":"1.6",
        "slabs":"4",
        "productSquare":"4"
      }
    }
  ]
}

```

### GET FILTERED EVENT LIST
__request:__

method: POST   /api/appEvents/filteredlist - return all events, happens in apps, 
fit to filter conditions

body :

```json
{
  "dateFrom": "12.12.2022",
  "dateTo": "12.12.2022",
  "shop": "Zetta",
  "concreteAddress": "Zetta",
  "materialPriceFrom": "1000",
  "materialPriceTo": "1000",
  "addPriceFrom": "1500",
  "addPriceTo": "1500",
  "allPriceFrom": "3000",
  "allPriceTo": "4000",
  "materials": "Quarz0-subtype0-color0,Acryl1-subtype1-color1,Acryl2-subtype2-color2"
}
```

if the parameter does not uses - field is empty ("")

__response:__

As in "Get all events", but filtered