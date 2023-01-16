'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');
var progressBarFill = document.querySelector("#progressBar > .progress-bar-fill");
var progressBarText = document.querySelector(".progress-bar-text")
var list = document.querySelector('.fileList')

async function getAllFilesFromDB() {

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let response = await fetch("/api/updatefiles/allFiles",{
        method: 'POST',
        headers:{
            'Authorization': 'Bearer ' + accessToken
            // 'Content-Type': 'multipart/form-data; boundary=something'
        }
    });
    let content = await response.json()

    for (let key in content) {
        list.innerHTML += "<tr> <td><p><a href='" + content[key].url + "'>" + content[key].name + "</a></p></td><td>"
            + content[key].uploadDate.substring(0, 10) + "</td><td>"
            + "<form onclick='deleteFile(" + content[key].id + ")'> <input type='button' value='Delete'/></form>"
    }
}

getAllFilesFromDB()


function deleteFile(id) {
    var xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/api/updatefiles/deleteFile/" + id, false);
    xhr.setRequestHeader('Authorization','Bearer ' + accessToken);
    xhr.send();
    console.log(xhr.responseText);

    location.reload();
}

function uploadSingleFile(file) {

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.setRequestHeader('Authorization','Bearer ' + accessToken);
    xhr.open("POST", "/api/updatefiles/uploadFile");

    xhr.upload.addEventListener("progress", e => {
        const percent = e.lengthComputable ? (e.loaded / e.total) * 100 : 0;
        progressBarFill.style.width = percent.toFixed(0) + "%";
        progressBarText.textContent = percent.toFixed(0) + "%";
    })

    xhr.onload = function () {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if (xhr.status == 200) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>Download url : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

singleUploadForm.addEventListener('submit', function (event) {
    var files = singleFileUploadInput.files;
    if (files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true);