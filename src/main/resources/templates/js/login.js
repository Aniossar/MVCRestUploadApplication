let loginInput = document.querySelector('form input[name="auth_login"]')
let passwordInput = document.querySelector('form input[name="auth_password"]')

auth_form.addEventListener("submit", (e)=>{
    e.preventDefault();

    let login = loginInput.value
    let password = passwordInput.value
    auth(login, password)
})

async function auth(login, password){

    let loginData = {
        "login": login,
        "password":password
    }
    let response = await fetch(AUTH_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(loginData)
    });
    console.log(response)
    if(response.status != 200){
        console.log("wrong data")
        let div = document.querySelector('.form_auth_block_content')
        div.innerHTML +='<div class="wrong_data"><p>Не верный логин или пароль</p></div>'


        setTimeout(()=>{
            document.querySelector('.wrong_data').remove()
        }, 4000);
        //return;
    }

    if(response.headers.get('Content-Type') == 'application/json'){
        let content = await response.json()
        let token = content['token']
        console.log(content)
        console.log(content['jwt-token'])
        console.log(`login = ${login} password = ${password}`)

        localStorage.setItem('jwt-token', token)

        console.log(`jwt-token from localStorage = ${localStorage.getItem('jwt-token')}`)

        window.location.replace('/')
    }



}