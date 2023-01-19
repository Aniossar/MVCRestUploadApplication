
let enableCheckbox = document.querySelector('input[name="enable"]')
let roleInput = document.querySelector('select[name="role"]')
let loginInput = document.querySelector('input[name="login"]')
let emailInput = document.querySelector('input[name="email"]')
let fullNameInput = document.querySelector('input[name="fullName"]')
let companyNameInput = document.querySelector('input[name="company"]')
let addressInput = document.querySelector('input[name="address"]')
let certainPlaceAddressInput = document.querySelector('input[name="certain_place_address"]')
let phoneNumberInput = document.querySelector('input[name="phone_number"]')

let allAppCheckbox = document.querySelector('input[name="all"]')
let kmAppCheckbox = document.querySelector('input[name="km"]')
let kAppCheckbox = document.querySelector('input[name="k"]')
let zAppCheckbox = document.querySelector('input[name="z"]')
let pmAppCheckbox = document.querySelector('input[name="pm"]')

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

    getAllUsers()

}, ()=>{
    //onRejected
})



function selectUser(element, userId){

    let usersRows = document.querySelectorAll(".user_row")
    for(let el of usersRows){
        el.style.backgroundColor = 'white'
    }
    element.style.backgroundColor = 'grey'

    showUserInfo(element, userId)
}

function createUserRow(userId, login, fullName, companyName, certainPlaceAddress){
    let newLi = document.createElement("li")

    console.log(userId)

    newLi.innerHTML = `
        <div class="user_row" onclick="selectUser(this, ${userId})">
            <img src="images/user_no_logo.png" alt="">
            <div class="user_info">
                <p class="user_name">${fullName}</p>
                <p class="user_company" >${companyName} / ${certainPlaceAddress}</p>
                <span id="id" hidden>${userId}</span>
                <span id="login" hidden>${login}</span>
            </div>
        </div>`

    return newLi
}

async function getAllUsers(){
    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let response = await fetch(URL_GET_ALL_USERS, {
        method:'GET',
        headers:{
            'Authorization': 'Bearer ' + accessToken
        }
    });

    let content = await response.json()

    content.sort((a, b) =>{
        return  a.id - b.id
    })



    let ul = document.querySelector('.users_list > ul')
    ul.innerHTML = ""
    for(let i=0;i < content.length;i++){
        let newLi = createUserRow(
            content[i].id
            , content[i].login
            , content[i].fullName
            , content[i].companyName
            , content[i].certainPlaceAddress)


        ul.appendChild(newLi)
    }

    console.log(ul.childNodes[0].childNodes[0].nextSibling)
    selectUser(ul.childNodes[0].childNodes[0].nextSibling, content[0].id)
}

async function showUserInfo(element, userId){
    console.log(userId)

    loginInput.disabled = true

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);


    let response = await fetch(URL_GET_USER + userId, {
        method:'GET',
        headers:{
            'Authorization': 'Bearer ' + accessToken
        }
    });

    let content = await response.json()

    console.log(content)


    enableCheckbox.checked = content.enabled
    roleInput.value = content.roleEntity.name

    loginInput.value = content.login


    emailInput.value = content.email
    fullNameInput.value = content.fullName
    companyNameInput.value = content.companyName
    addressInput.value = content.address
    certainPlaceAddressInput.value = content.certainPlaceAddress
    phoneNumberInput.value = content.phoneNumber

    let appAccess = content.appAccess.split(",")

    allAppCheckbox.checked = false
    kmAppCheckbox.checked = false
    kAppCheckbox.checked = false
    zAppCheckbox.checked = false
    pmAppCheckbox.checked = false

    for(let app of appAccess){
        if(app === APP_ALL) allAppCheckbox.checked = true
        if(app === APP_KOREANIKA_MASTER) kmAppCheckbox.checked = true
        if(app === APP_KOREANIKA) kAppCheckbox.checked = true
        if(app === APP_ZETTA) zAppCheckbox.checked = true
        if(app === APP_PRO_MEBEL) pmAppCheckbox.checked = true
    }

}

async function editUser(){

    let login = loginInput.value
    let email = emailInput.value
    let fullName = fullNameInput.value
    let companyName = companyNameInput.value
    let phoneNumber = phoneNumberInput.value
    let address = addressInput.value
    let certainPlaceAddress = certainPlaceAddressInput.value

    let appAccess = ""
    if(allAppCheckbox.checked) appAccess +=APP_ALL
    if(kmAppCheckbox.checked) appAccess +="," + APP_KOREANIKA_MASTER
    if(kAppCheckbox.checked) appAccess +="," + APP_KOREANIKA
    if(zAppCheckbox.checked) appAccess +="," + APP_ZETTA
    if(pmAppCheckbox.checked) appAccess +="," + APP_PRO_MEBEL

    let newRole = roleInput.value

    if(roleInput.value === "ROLE_MODERATOR") newRole = "MODERATOR"
    else if(roleInput.value === "ROLE_ADMIN") newRole = "ADMIN"
    else if(roleInput.value === "ROLE_SUPPLIER") newRole = "SUPPLIER"
    else if(roleInput.value === "ROLE_SHOP") newRole = "SHOP"
    else if(roleInput.value === "ROLE_USER") newRole = "USER"

    let enable = enableCheckbox.checked

    let body = {
        "login":login,
        "email":email,
        "fullName":fullName,
        "companyName":companyName,
        "phoneNumber":phoneNumber,
        "address": address,
        "certainPlaceAddress":certainPlaceAddress,
        "appAccess": appAccess,
        "newRole":newRole,
        "enabled":enable
    }

    alert("Изменить пользователя?")

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let response = await fetch(URL_GET_EDIT_USER, {
        method:'PUT',
        headers:{
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json;charset=utf-8'
        },
        body:JSON.stringify(body)
    });

    //let content = await response.json()

    //console.log(content)

    getAllUsers()
}