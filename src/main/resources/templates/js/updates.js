


authStatus.then(()=>{

    //onFulfilled
    document.querySelector('.user_name').innerHTML = userLogin
    document.querySelector('.user_info').innerHTML = userRole

}, ()=>{
    //onRejected
})

getFilesList()

let tableBody = document.querySelector("tbody")

function createTableRow(tableBody, date, access, name, info, downloadUrl, idFile){

    let newTr = document.createElement("tr")

    newTr.classList.add(name)

    let rowClass = "row_" + idFile
    newTr.innerHTML = `                                
    <td>${idFile} - ${date.toLocaleDateString()}</td>
    <td>
        <div class="file_access ${rowClass}">

            <div><input type="checkbox" name="all" ${access.all} onclick="changeFileAccess(${idFile})"> <label for="all">all</label></div>
            <div><input type="checkbox" name="km" ${access.KM} onclick="changeFileAccess(${idFile})"> <label for="km">KM</label></div>
            <div><input type="checkbox" name="k" ${access.K} onclick="changeFileAccess(${idFile})"> <label for="k">K</label></div>
            <div><input type="checkbox" name="z" ${access.Z} onclick="changeFileAccess(${idFile})"> <label for="z">Z</label></div>
            <div><input type="checkbox" name="pm" ${access.PM} onclick="changeFileAccess(${idFile})"> <label for="pm">PM</label></div>
       
        </div>
    </td>
    <td>
        <div class="tooltip ${rowClass}">
            ${name}
                <span class="tooltiptext info">${info}</span> 
        </div>
    </td>
<!--    <td><a href="##" onclick="downloadUpdateFile('${name.toString()}')">скачать</a></td>-->
    <td><a href="##" onclick="downloadUpdateFile('${name}')" >скачать</a></td>
    <td><a href="##" onclick="deleteUpdateFile(${idFile})">удалить</a></td>
    `

    tableBody.appendChild(newTr)
}

async function getFilesList(){

    let pathname = window.location.pathname

    let url_get_updates_list = URL_UPDATES_GET_LIST

    if(pathname == "/pricesUpdates"){
        url_get_updates_list = URL_PRICES_GET_LIST
    }

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let response = await fetch(url_get_updates_list, {
        method:'GET',
        headers:{
            'Authorization': 'Bearer ' + accessToken
        }
    });

    let content = await response.json()

    content.sort((a, b) =>{
        return  b.id - a.id
    })
    tableBody.innerHTML = ""
    for(let i=0;i < content.length;i++){
        // console.log(content[i])

        let date = new Date(content[i].uploadDate);

        let access = {
            all:"",
            KM:"",
            K:"",
            Z:"",
            PM:""
        }

        let forClients = content[i].forClients.split(',')

        for(let i =0;i< forClients.length;i++){

            if(forClients[i] == APP_ALL) access.all = "checked"
            if(forClients[i] == APP_KOREANIKA_MASTER) access.KM = "checked"
            if(forClients[i] == APP_KOREANIKA) access.K = "checked"
            if(forClients[i] == APP_ZETTA) access.Z = "checked"
            if(forClients[i] == APP_PRO_MEBEL) access.PM = "checked"
        }

        let name = content[i].name

        let info = content[i].info

        let downloadUrl = content[i].url

        let idFile = content[i].id

        console.log('forClients = ' + forClients)

        createTableRow(tableBody, date, access, name,info, downloadUrl, idFile)
    }
}

function uploadUpdateFile() {

    let pathname = window.location.pathname

    let url_upload_file = URL_UPDATES_UPLOAD_FILE

    if(pathname == "/pricesUpdates"){
        url_upload_file = URL_PRICES_UPLOAD_FILE
    }

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    const uploadProgress = document.getElementById("upload-progress");
    const progressState = document.getElementById("progress_state");

    const formData = new FormData();
    const fileField = document.querySelector('input[type="file"]');
    const textArea = document.querySelector('.info');

    const checkBoxAll = document.querySelector('.upload_file_access input[name="all"]')
    const checkBoxKM = document.querySelector('.upload_file_access input[name="km"]')
    const checkBoxK = document.querySelector('.upload_file_access input[name="k"]')
    const checkBoxZetta = document.querySelector('.upload_file_access input[name="zetta"]')
    const checkBoxProMebel = document.querySelector('.upload_file_access input[name="proMebel"]')

    let access = ""
    if(checkBoxAll.checked) access +=APP_ALL
    if(checkBoxKM.checked) access +="," + APP_KOREANIKA_MASTER
    if(checkBoxK.checked) access +="," + APP_KOREANIKA
    if(checkBoxZetta.checked) access +="," + APP_ZETTA
    if(checkBoxProMebel.checked) access +="," + APP_PRO_MEBEL

    if(fileField.files.length == 0) return;
    uploadProgress.hidden = false
    progressState.hidden = false

    formData.append('file', fileField.files[0]);
    formData.append('info', textArea.value);
    formData.append('forClients', access);

    var xhr = new XMLHttpRequest();

    xhr.open("POST", url_upload_file);
    xhr.setRequestHeader('Authorization','Bearer ' + accessToken);

    xhr.upload.addEventListener("progress", e => {
        uploadProgress.value = e.loaded / e.total;
        progressState.innerText = (Math.trunc((e.loaded / e.total)*100 )+ "%")
    })

    xhr.onload = function () {
        // console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);

        uploadProgress.hidden = true


        if (xhr.status == 200) {
            // singleFileUploadError.style.display = "none";
            fileField.value = null;
            // progressState.hidden = true
            progressState.innerText = "Успех!"
            progressState.style.color = "green";
            setTimeout(()=>{progressState.hidden = true}, 5000)

            getFilesList().then(r => {});

            //singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>Download url : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            //singleFileUploadSuccess.style.display = "block";
        } else {

            progressState.innerText = "Загрузка не удалась"
            progressState.style.color = "red";
            setTimeout(()=>{
                fileField.value = null;
                progressState.hidden = true}, 5000)
            //singleFileUploadSuccess.style.display = "none";
            //singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

function downloadUpdateFile(name) {

    let pathname = window.location.pathname

    let url_download_file = URL_UPDATES_DOWNLOAD_FILE

    if(pathname == "/pricesUpdates"){
        url_download_file = URL_PRICES_DOWNLOAD_FILE
    }

    const startTime = new Date().getTime();

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    request = new XMLHttpRequest();

    request.responseType = "blob";
    request.open("get", url_download_file + name, true);
    request.setRequestHeader('Authorization', 'Bearer ' + accessToken)
    request.send();

    request.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            const imageURL = window.URL.createObjectURL(this.response);

            const anchor = document.createElement("a");
            anchor.href = imageURL;
            anchor.download = name;
            document.body.appendChild(anchor);
            anchor.click();
        }
    };

    request.onprogress = function (e) {
        const percent_complete = Math.floor((e.loaded / e.total) * 100);

        const duration = (new Date().getTime() - startTime) / 1000;
        const bps = e.loaded / duration;

        const kbps = Math.floor(bps / 1024);
    }
}

async function deleteUpdateFile(idFile){

    let pathname = window.location.pathname

    pathname.replace("#", "")

    let url_delete_file = URL_UPDATES_DELETE_FILE

    if(pathname == "/pricesUpdates"){
        url_delete_file = URL_PRICES_DELETE_FILE
    }

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    try {
        const response = await fetch(url_delete_file + idFile, {
            method: 'DELETE',
            headers:{
                'Authorization': 'Bearer ' + accessToken
            }
        })
        //const result = await response.json();
        console.log('Успех:');
        await getFilesList();
    } catch (error) {
        console.error('Ошибка:', error);
    }
}

async function changeFileAccess(idFile){
    let pathname = window.location.pathname

    let rowClass = "row_" + idFile


    const checkBoxAll = document.querySelector(`.${rowClass} input[name="all"]`)
    const checkBoxKM = document.querySelector(`.${rowClass} input[name="km"]`)
    const checkBoxK = document.querySelector(`.${rowClass} input[name="k"]`)
    const checkBoxZetta = document.querySelector(`.${rowClass} input[name="z"]`)
    const checkBoxProMebel = document.querySelector(`.${rowClass} input[name="pm"]`)

    const tooltipSpan = document.querySelector(`.${rowClass} .info`)

    let info = tooltipSpan.textContent;

    let access = ""
    if(checkBoxAll.checked) access +=APP_ALL
    if(checkBoxKM.checked) access +="," + APP_KOREANIKA_MASTER
    if(checkBoxK.checked) access +="," + APP_KOREANIKA
    if(checkBoxZetta.checked) access +="," + APP_ZETTA
    if(checkBoxProMebel.checked) access +="," + APP_PRO_MEBEL

    if (access[0] == ',') access = access.replace(",","")


    let url_edit_file = URL_UPDATES_EDIT_FILE

    if(pathname == "/pricesUpdates"){
        url_edit_file = URL_PRICES_EDIT_FILE
    }

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let newData = {
        "info": info,
        "forClients": access
    }
    console.log(JSON.stringify(newData))

    try {
        const response = await fetch(url_edit_file + idFile, {
            method: 'POST',
            headers:{
                'Authorization': 'Bearer ' + accessToken,
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(newData)
        })
        //const result = await response.json();
        console.log('Успех:');
        await getFilesList();
    } catch (error) {
        console.error('Ошибка:', error);
    }
}