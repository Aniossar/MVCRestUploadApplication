
let roleInput = document.querySelector('form select[name="reg_role"]')
let loginInput = document.querySelector('form input[name="reg_login"]')
let passwordInput = document.querySelector('form input[name="reg_password"]')
let emailInput = document.querySelector('form input[name="reg_email"]')
let fullNameInput = document.querySelector('form input[name="reg_fullName"]')
let companyNameInput = document.querySelector('form input[name="reg_company"]')
let certainPlaceAddressInput = document.querySelector('form input[name="reg_certain_place_address"]')
let phoneNumberInput = document.querySelector('form input[name="reg_phone_number"]')




function checkPassword(){
    let pwInput1 = document.querySelector('form input[name="reg_password"]')
    let pwInput2 = document.querySelector('form input[name="reg_password_repeat"]')

    console.log(pwInput1.value + ' - ' + pwInput2.value)
    if(pwInput1.value === pwInput2.value){
        pwInput1.style.color = 'var(--color-primary)'
        pwInput2.style.color = 'var(--color-primary)'
    }else{
        pwInput1.style.color = 'red'
        pwInput2.style.color = 'red'
    }
}

reg_form.addEventListener("submit", (e)=>{
    e.preventDefault();
    let login = loginInput.value
    let password = passwordInput.value
    let email = emailInput.value

    let fullName = fullNameInput.value
    let companyName = companyNameInput.value
    let phoneNumber = phoneNumberInput.value
    let address = certainPlaceAddressInput.value
    let certainPlaceAddress = certainPlaceAddressInput.value
    let appAccess = APP_KOREANIKA
    let role = roleInput.value

    let loginData ={

        "password":password,//
        "login": login,//
        "email":email,//
        "fullName":fullName,//
        "companyName":companyName,//
        "phoneNumber":phoneNumber,
        "address":address,
        "certainPlaceAddress":certainPlaceAddress,//
        "appAccess":appAccess,
        "desiredRole":role
    }

    register(loginData)
})
async function register(loginData){

     let response = await fetch(URL_API_REGISTER, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(loginData)
    });
    if(response.ok){
        let content = await response.text()

        console.log(content)
        if(content === 'OK'){
            window.location.replace('/login')
        }
        
    }
}