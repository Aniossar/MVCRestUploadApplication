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
    //if(!response.ok) return;

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