
let authStatus = checkAccess();
let userRole
let userLogin


setInterval( ()=>{
    authStatus = checkAccess();

    checkAccessToken(localStorage.getItem(ACCESS_TOKEN_NAME))
    checkRefreshToken(localStorage.getItem(REFRESH_TOKEN_NAME));
}, 5000);

async function checkAccess(){

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);
    let refreshToken = localStorage.getItem(REFRESH_TOKEN_NAME);

    // console.log(ACCESS_TOKEN_NAME + ' = ' + accessToken)
    // console.log(REFRESH_TOKEN_NAME + ' = ' + refreshToken)

    if(accessToken == null || accessToken == undefined
        || refreshToken == null || refreshToken == undefined ){

        window.location.replace(URL_LOGIN);
        return 0;
    }else{
        let response = await fetch(URL_API_ME, {
            method:'GET',
            headers:{
                'Authorization': 'Bearer ' + accessToken
            }

        });

        if(!response.ok){
            await updateAccessToken(accessToken, refreshToken);
            return 0;
        }

        if(response.headers.has('Content-Type')){

            if(response.headers.get('Content-Type') == 'application/json'){

                let content = await response.json()

                userRole = content['roleName']
                userLogin = content['login']

                console.log(userRole)
                if(window.location.pathname == '/'){

                    /** разкомментировать когда будет возвращаться корректная роль*/
                     if(userRole === "ROLE_ADMIN" || userRole === "ROLE_MODERATOR"){
                    //     window.location.replace('admin/dashboard')
                         window.location.replace('/dashboard')
                     }else{
                    //     window.location.replace('user/info')
                         window.location.replace('/profile')
                     }

                }

                return 1;
            }else{
                window.location.replace(URL_LOGIN);
                return 0;
            }
        }else{
            window.location.replace(URL_LOGIN);
            return 0;
        }
    }
}

async function updateAccessToken(accessToken, refreshToken){

    // console.log('refreshToken = ' + refreshToken)
    // console.log(`{"${REFRESH_TOKEN_NAME}": "${refreshToken}"}`)

    let response = await fetch(URL_API_UPDATE_TOKEN, {
        method:'POST',
        headers:{
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json;charset=utf-8'
        },
        body:`{"${REFRESH_TOKEN_NAME}": "${refreshToken}"}`


    });

    if(!response.ok){
        window.location.replace(URL_LOGIN);
    }
    if(response.headers.has('Content-Type')){

        if(response.headers.get('Content-Type') == 'application/json'){

            let content = await response.json()

            // console.log(content)

            let newAccessToken = content[ACCESS_TOKEN_NAME]
            let newRefreshToken = content[REFRESH_TOKEN_NAME]

            localStorage.setItem(ACCESS_TOKEN_NAME, newAccessToken)


            // console.log(newAccessToken)

            if(newAccessToken == null){
                window.location.replace(URL_LOGIN);
            }
            console.log("SUCCESS UPDATE ACCESS TOKEN")

            // let decodedToken = JSON.parse(atob(newAccessToken.split('.')[1]))
            // // let userRole
            // console.log(decodedToken)
            if(window.location.pathname == '/'){

                /** разкомментировать когда будет возвращаться корректная роль*/
                // if(userRole === "ADMIN" || userRole === "MODERATOR"){
                //     window.location.replace('admin/dashboard')
                // }else{
                //     window.location.replace('user/info')
                // }
                window.location.replace('/dashboard')
            }

            return 1;
        }else{
            window.location.replace(URL_LOGIN);
            return 0;
        }
    }else{
        window.location.replace(URL_LOGIN);
        return 0;
    }

}

async  function updateRefreshToken(accessToken, refreshToken){

    let response = await fetch(URL_API_UPDATE_REFRESH_TOKEN, {
        method:'POST',
        headers:{
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json;charset=utf-8'
        },
        body:`{"${REFRESH_TOKEN_NAME}": "${refreshToken}"}`

    });

    if(!response.ok){
        window.location.replace(URL_LOGIN);
    }
    if(response.headers.get('Content-Type') == 'application/json'){

        let content = await response.json()

        // console.log(content)

        let newAccessToken = content[ACCESS_TOKEN_NAME]
        let newRefreshToken = content[REFRESH_TOKEN_NAME]

        localStorage.setItem(ACCESS_TOKEN_NAME, newAccessToken)
        localStorage.setItem(REFRESH_TOKEN_NAME, newRefreshToken)

        console.log("SUCCESS UPDATE REFRESH TOKEN")


        return 1;
    }else{
        return 0;
    }

}

function checkRefreshToken(refreshToken){
    let decodedToken = JSON.parse(atob(refreshToken.split('.')[1]))

    let now = new Date();
    let date = new Date (decodedToken.exp*1000);
    // console.log(now)
    // console.log(date)
    // console.log((date.getTime()- now.getTime()))

    if(date.getTime() - now.getTime() < 60000){
        updateRefreshToken(localStorage.getItem(ACCESS_TOKEN_NAME)
            , localStorage.getItem(REFRESH_TOKEN_NAME))
    }
}

function checkAccessToken(accessToken){
    let decodedToken = JSON.parse(atob(accessToken.split('.')[1]))

    let now = new Date();
    let date = new Date (decodedToken.exp*1000);
    // console.log(now)
    // console.log(date)
    // console.log((date.getTime()- now.getTime()))

    if(date.getTime() - now.getTime() < 60000){
        updateAccessToken(localStorage.getItem(ACCESS_TOKEN_NAME)
            , localStorage.getItem(REFRESH_TOKEN_NAME))
    }
}

function logout(){
    localStorage.removeItem(ACCESS_TOKEN_NAME)
    localStorage.removeItem(REFRESH_TOKEN_NAME)
}
