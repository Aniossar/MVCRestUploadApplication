
let roleInput = document.querySelector('form select[name="reg_role"]')
let emailInput = document.querySelector('form input[name="reg_email"]')
let loginInput = document.querySelector('form input[name="reg_login"]')
let passwordInput = document.querySelector('form input[name="reg_password"]')



function checkPassword(){
    let pwInput1 = document.querySelector('form input[name="reg_password"]')
    let pwInput2 = document.querySelector('form input[name="reg_password_repeat"]')

    console.log(pwInput1.value + ' - ' + pwInput2.value)
    if(pwInput1.value === pwInput2.value){
        let blocks = document.querySelectorAll('.form_reg_password_block')
        blocks[0].style.borderColor = 'var(--color-primary)'
        blocks[1].style.borderColor = 'var(--color-primary)'
    }else{
        let blocks = document.querySelectorAll('.form_reg_password_block')
        blocks[0].style.borderColor = 'red'
        blocks[1].style.borderColor = 'red'
    }
}

reg_form.addEventListener("submit", (e)=>{
    e.preventDefault();
    let login = loginInput.value
    let password = passwordInput.value
    let role = roleInput.value
    let email = emailInput.value
    register(login, password, email, role)
})
async function register(login, password, email, role){
    let loginData = {
        "login": login,
        "password":password,
        "email":email,
        "desiredRole":role
    }
     let response = await fetch('/api/register', {
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