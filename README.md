1. Add directory Upload and update the path in application.properties

REST EndPoints:
1. GET /lastFile — get the last uploaded file in JSON
2. GET /allFiles — get all files in JSON
3. GET /getFile/{id} — get file with {id} in JSON
4. POST /uploadFile — upload file; put file in body request
5. GET /downloadFile/{fileName} — download file with {filename}
6. DELETE /deleteFile/{id} — delete file with {id}


Docker:

1. Dockerfile - template for app image
2. docker-compose_dev.yml, docker-compose_test.yml, 
docker-compose_production.yml - make images for 3 
different pack of containers, for dev, test and production.
3. for deploy this on server:
   1. 