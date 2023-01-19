
let loginInput = document.querySelector('input[name="login"]')
let emailInput = document.querySelector('input[name="email"]')
let fullNameInput = document.querySelector('input[name="fullName"]')
let companyNameInput = document.querySelector('input[name="company"]')
let addressInput = document.querySelector('input[name="address"]')
let certainPlaceAddressInput = document.querySelector('input[name="certain_place_address"]')
let phoneNumberInput = document.querySelector('input[name="phone_number"]')

let passwordInput = document.querySelector('input[name="pw"]')
let passwordNewInput = document.querySelector('input[name="pw_new"]')
let passwordNewRepeatInput = document.querySelector('input[name="pw_new_repeat"]')

let messageInfoElement = document.querySelector('#msg_profile')
let messagePwElement = document.querySelector('#msg_password')

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

    if(!(userRole === 'ROLE_MODERATOR' || userRole === 'ROLE_ADMIN')){
        document.querySelector('.menu_dashboard').hidden = true
        document.querySelector('.menu_usersList').hidden = true
        document.querySelector('.menu_statistics').hidden = true
        document.querySelector('.menu_appUpdates').hidden = true
        document.querySelector('.menu_pricesUpdates').hidden = true
        document.querySelector('.menu_news').hidden = true
    }

    getUserProfile();

    loginInput.disabled = true

}, ()=>{
    //onRejected
})

function checkPassword(){

    let success = true;

    if((passwordNewInput.value === passwordNewRepeatInput.value) && (passwordNewInput.value !== "")){
        passwordNewInput.style.color = 'var(--color-primary)'
        passwordNewRepeatInput.style.color = 'var(--color-primary)'
    }else{
        passwordNewInput.style.color = 'red'
        passwordNewRepeatInput.style.color = 'red'
        success = false;

    }

    return success
}


async function getUserProfile(){

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let response = await fetch(URL_GET_USER_OWN_INFO, {
        method:'GET',
        headers:{
            'Authorization': 'Bearer ' + accessToken
        }
    });

    let content = await response.json()
    console.log(content)


    loginInput.value = content.login
    emailInput.value = content.email
    fullNameInput.value = content.fullName
    companyNameInput.value = content.companyName
    addressInput.value = content.address
    certainPlaceAddressInput.value = content.certainPlaceAddress
    phoneNumberInput.value =content.phoneNumber

}

async function editUserProfile(){

    let isExecuted = confirm("Вы уверены, что хотите изменить данные?");
    if(!isExecuted){
        console.log("NO")
        return;
    }
    console.log("YES")

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let bodyObj ={
        "email":emailInput.value,
        "fullName": fullNameInput.value,
        "companyName": companyNameInput.value,
        "phoneNumber": phoneNumberInput.value,
        "address": addressInput.value,
        "certainPlaceAddress": certainPlaceAddressInput.value
    }

    let response = await fetch(URL_USER_OWN_EDIT, {
        method:'PUT',
        headers:{
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(bodyObj)
    });

     await response
    // let content = await response.json()
    // console.log(content)


}

async function changePassword(){

    if(!checkPassword()){
        showMessage(messagePwElement, 'red', "Пароли не совпадают или поле пустое")
        return
    }

    let oldPassword = passwordInput.value;
    let newPassword = passwordNewInput.value;

    let body = {
        "oldPassword": oldPassword,
        "newPassword":newPassword
    }

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let response = await fetch(URL_CHANGE_USER_PW, {
        method:'PUT',
        headers:{
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json;charset=utf-8'
        },
        body:JSON.stringify(body)
    });

    let content = await response

    if(response.status === 200){
        showMessage(messagePwElement, 'green', "Пароль успешно изменен!")
        passwordInput.value = ""
        passwordNewInput.value = ""
        passwordNewRepeatInput.value = ""
    }else if(response.status === 409){
        showMessage(messagePwElement, 'red', "Старый пароль неверный")
    }else{
        showMessage(messagePwElement, 'red', "При изменении пароля произошла ошибка")
    }

    //console.log(content)

}

function showMessage(input, color, message){
    input.hidden = false
    input.textContent = message
    input.style.color = color

    setTimeout(()=>{
        input.hidden = true
    }, 3000)
}