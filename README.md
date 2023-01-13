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
2. DELETE /api/deleteUser/{login} - delete user (only for ADMIN)

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
   1. 