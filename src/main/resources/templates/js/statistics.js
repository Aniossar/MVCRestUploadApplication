
let filterUseInput = document.querySelector('#filter_use')

let dateUseInput = document.querySelector('#date_use')
let materialUseInput = document.querySelector('#material_use')
let shopUseInput = document.querySelector('#shop_use')
let certainAddressUseInput = document.querySelector('#certain_address_use')
let materialPriceUseInput = document.querySelector('#material_price_use')
let addPriceUseInput = document.querySelector('#add_price_use')
let allPriceUseInput = document.querySelector('#all_use')

let dateFromInput = document.querySelector('#date_from')
let dateToInput = document.querySelector('#date_to')
let materialsInput = document.querySelector('#materials')
let shopInput = document.querySelector('#shop')
let certainAddressInput = document.querySelector('#certainAddress')
let materialPriceFromInput = document.querySelector('#materialPriceFrom')
let materialPriceToInput = document.querySelector('#materialPriceTo')
let addPriceFromInput = document.querySelector('#addPriceFrom')
let addPriceToInput = document.querySelector('#addPriceTo')
let allPriceFromInput = document.querySelector('#allPriceFrom')
let allPriceToInput = document.querySelector('#allPriceTo')

let updateBtn = document.querySelector('#update')

const tableBody = document.querySelector(".table_container table tbody")
// for(let i=0;i<50;i++){
    tableBody.innerHTML += `
        <tr>
            <td>Date</td>
            <td>Салон</td>
            <td>Пользователь</td>
            <td>Тип события</td>
            <td>Материал</td>
            <td>Кол-во слэбов</td>
            <td>Площадь изделия</td>
            <td>Сумма материала</td>
            <td>Сумма доп. работ</td>
            <td>Сумма счета</td>
        </tr>
     `
//
// }

authStatus.then(()=>{

    //onFulfilled
    document.querySelector('.user_name').innerHTML = userLogin
    document.querySelector('.user_info').innerHTML = userRole

    if(userRole === 'ROLE_ADMIN'){
        document.querySelector('.user_name').innerHTML = userLogin + ' (Администратор)'
    }else if(userRole === 'ROLE_MODERATOR'){
        document.querySelector('.user_name').innerHTML = userLogin + ' (Модератор)'
    }else if(userRole === 'ROLE_SUPPLIER'){
        document.querySelector('.user_name').innerHTML = userLogin + ' (Поставщик)'
    }else if(userRole === 'ROLE_SHOP'){
        document.querySelector('.user_name').innerHTML = userLogin + ' (Салон)'
    }else if(userRole === 'ROLE_USER'){
        document.querySelector('.user_name').innerHTML = userLogin + ' (Дизайнер)'
    }

    getAllStatistics()

}, ()=>{
    //onRejected
})

setInterval(()=>{
    document.querySelector('.user_name').innerHTML = userLogin
    document.querySelector('.user_info').innerHTML = userRole
    // console.log('userLogin: ' + userLogin)
    // console.log('userRole: ' + userRole)
}, 5000)


function createTableRow(tableBody, eventObject){

    let newTr = document.createElement("tr")



    let rowClass = "row_" + eventObject.id
    newTr.classList.add(rowClass)

    newTr.innerHTML += `
        <tr>
            <td>${new Date(eventObject["activityTime"]).toLocaleString()}</td>
            <td>${eventObject["companyName"]}</td>
            <td>${eventObject["login"]}</td>
            <td>${eventObject["type"]}</td>
            <td>${eventObject["materials"]}</td>
            <td>${eventObject["slabs"]}</td>
            <td>${eventObject["productSquare"].toFixed(2)}</td>
            <td>${eventObject["materialPrice"].toFixed()}</td>
            <td>${eventObject["addPrice"].toFixed()}</td>
            <td>${eventObject["allPrice"].toFixed()}</td>
        </tr>
     `

    tableBody.appendChild(newTr)
}

function prepareFilter(){

}

async function getAllStatistics(){


    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let response = await fetch(URL_GET_CALC_ACTIVITY_ALL, {
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
        createTableRow(tableBody, content[i])
    }
}

async function getFilteredStatistics(filterObject){

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);



    let response = await fetch(URL_GET_CALC_ACTIVITY_FILTERED, {
        method:'POST',
        headers:{
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json;charset=utf-8'
        },
        body:JSON.stringify(filterObject)
    });

    let content = await response.json()

    content.sort((a, b) =>{
        return  b.id - a.id
    })

    tableBody.innerHTML = ""
    for(let i=0;i < content.length;i++){
        createTableRow(tableBody, content[i])
    }
}


function updateTableView(){
    //using

    if(!filterNumberFieldValidation()) return

    if(filterUseInput.checked){
        console.log("update with filter");
        let filterObject={
            "dateFrom": "",
            "dateTo": "",
            "companyName": "",
            "certainPlaceAddress": "",
            "materialPriceFrom": -1,
            "materialPriceTo": -1,
            "addPriceFrom": -1,
            "addPriceTo": -1,
            "allPriceFrom": -1,
            "allPriceTo": -1,
            "materials": ""
        }


        if(dateUseInput.checked){
            filterObject.dateFrom = dateFromInput.value
            filterObject.dateTo = dateToInput.value
        }
        if(shopUseInput.checked){
            filterObject.companyName = shopInput.value
        }
        if(certainAddressUseInput.checked){
            filterObject.certainPlaceAddress = certainAddressInput.value
        }
        if(materialPriceUseInput.checked){
            filterObject.materialPriceFrom = parseInt(materialPriceFromInput.value)
            filterObject.materialPriceTo = parseInt(materialPriceToInput.value)
        }
        if(addPriceUseInput.checked){
            filterObject.addPriceFrom = parseInt(addPriceFromInput.value)
            filterObject.addPriceTo = parseInt(addPriceToInput.value)
        }
        if(allPriceUseInput.checked){
            filterObject.allPriceFrom = parseInt(allPriceFromInput.value)
            filterObject.allPriceTo = parseInt(allPriceToInput.value)
        }
        if(materialUseInput.checked){
            filterObject.materials = materialsInput.value
        }

        console.log(filterObject)
         getFilteredStatistics(filterObject)
    }else{
        console.log("update without filter")
        getAllStatistics()
    }
}

function filterNumberFieldValidation(){

    let success = true

    if(materialPriceUseInput.checked
        && (isNaN(parseInt(materialPriceFromInput.value)) || isNaN(parseInt(materialPriceToInput.value)))){
        materialPriceFromInput.style.color = 'red'
        materialPriceToInput.style.color = 'red'
        success = false
    }else{
        materialPriceFromInput.style.color = 'black'
        materialPriceToInput.style.color = 'black'
    }

    if(addPriceUseInput.checked
        && (isNaN(parseInt(addPriceFromInput.value)) || isNaN(parseInt(addPriceToInput.value)))){
        addPriceFromInput.style.color = 'red'
        addPriceToInput.style.color = 'red'
        success = false
    }else{
        addPriceFromInput.style.color = 'black'
        addPriceToInput.style.color = 'black'
    }

    if(allPriceUseInput.checked
        && (isNaN(parseInt(allPriceFromInput.value)) || isNaN(parseInt(allPriceToInput.value)))){
        allPriceFromInput.style.color = 'red'
        allPriceToInput.style.color = 'red'
        success = false
    }else{
        allPriceFromInput.style.color = 'black'
        allPriceToInput.style.color = 'black'
    }

    return success
}

function downloadStatisticsAsXls(){

}
