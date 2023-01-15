let loginInput = document.querySelector('form input[name="auth_login"]')
let passwordInput = document.querySelector('form input[name="auth_password"]')


auth_form.addEventListener("submit", (e)=>{
    e.preventDefault();

    let login = loginInput.value
    let password = passwordInput.value
    auth(login, password)
})

// btn_submit.addEventListener("click", (e)=>{
//     e.preventDefault();
//
//     let login = loginInput.value
//     let password = passwordInput.value
//     auth(login, password)
// })

async function auth(login, password){

    let loginData = {
        "login": login,
        "password":password
    }
    let response

    try {
        response = await fetch(URL_API_AUTH, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(loginData)
        });
        console.log(response)
    }catch (e){
        console.log('response ERROR = ' + response.status)
    }

    if(response.status != 200){
        console.log("wrong data")
        let divExt = document.querySelector('.form_auth_block')
        let divMsg = document.createElement("div", )
        divMsg.classList.add("wrong_data")
        divMsg.innerHTML += '<p>Не верный логин или пароль</p>'
        console.log(document.querySelector('.wrong_data'))
        if(document.querySelector('.wrong_data') === null){
            divExt.append(divMsg)

            setTimeout(()=>{
                document.querySelector('.wrong_data').remove()
            }, 4000);
        }

        return;
    }

    if(response.headers.get('Content-Type') == 'application/json'){
        let content = await response.json()
        let accessToken = content[ACCESS_TOKEN_NAME]
        let refreshToken = content[REFRESH_TOKEN_NAME]
        console.log(content)
        console.log('accessToken = ' + content[ACCESS_TOKEN_NAME])
        console.log(`login = ${login} password = ${password}`)

        localStorage.setItem(ACCESS_TOKEN_NAME, accessToken)
        localStorage.setItem(REFRESH_TOKEN_NAME, refreshToken)

        console.log(ACCESS_TOKEN_NAME + ` from localStorage = ${localStorage.getItem(ACCESS_TOKEN_NAME)}`)
        console.log(REFRESH_TOKEN_NAME + ` from localStorage = ${localStorage.getItem(REFRESH_TOKEN_NAME)}`)

        window.location.replace('/')
    }



}