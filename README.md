1. Add directory Upload and update the path in application.properties

REST EndPoints:
GET /lastFile — get the last uploaded file in JSON
GET /allFiles — get all files in JSON
GET /getFile/{id} — get file with {id} in JSON
POST /uploadFile — upload file; put file in body request
GET /downloadFile/{fileName} — download file with {filename}
DELETE /deleteFile/{id} — delete file with {id}
