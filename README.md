1. Add directory Upload and update the path in application.properties

REST EndPoints for working with update files:
1. GET /api/updatefiles/lastFile — get the last uploaded file in JSON
2. GET /api/updatefiles/allFiles — get all files in JSON
3. GET /api/updatefiles/getFile/{id} — get file with {id} in JSON
4. POST /api/updatefiles/uploadFile — upload file; put file in body request
5. GET /api/updatefiles/downloadFile/{fileName} — download file with {filename}
6. DELETE /api/updatefiles/deleteFile/{id} — delete file with {id}

REST EndPoints for working with price list files:
1. GET /api/pricelists/lastFile — get the last uploaded file in JSON
2. GET /api/pricelists/allFiles — get all files in JSON
3. GET /api/pricelists/getFile/{id} — get file with {id} in JSON
4. POST /api/pricelists/uploadFile — upload file; put file in body request
5. GET /api/pricelists/downloadFile/{fileName} — download file with {filename}
6. DELETE /api/pricelists/deleteFile/{id} — delete file with {id}

REST Endpoints for authorization:
1. POST /register - register user
2. POST /auth - login user; return access token + refresh token
3. POST /token - return new access token using refresh token
4. GET /me - check user auth; return role
5. POST /api/refreshToken - return new refresh token using old refresh token
6. PUT /api/changeOwnPassword - change password of authenticated user

REST Endpoints for activity:
1. GET /api/allActivities - get all user activities
2. POST /api/saveCalculatorActivity - saves activity from calculator app

REST Endpoints for system information:
1. GET /api/getApplicationStart - get time the app started
2. GET /api/getApplicationWorkingTime - calculates the period the app is working

Docker:

1. Dockerfile - template for app image
2. docker-compose_dev.yml, docker-compose_test.yml, 
docker-compose_production.yml - make images for 3 
different pack of containers, for dev, test and production.
3. for deploy this on server:
   1. 