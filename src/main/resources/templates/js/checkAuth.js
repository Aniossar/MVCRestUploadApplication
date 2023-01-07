
let authStatus = checkAuth();
let userRole
let userLogin

checkAuth();
setInterval( ()=>{
    authStatus = checkAuth();
}, 5000);

async function checkAuth(){

    let jwtToken = localStorage.getItem('jwt-token');

    console.log('jwt-token = ' + jwtToken)
    console.log(jwtToken)

    if(jwtToken == null || jwtToken == undefined){
        window.location.replace("/login");
        return 0;
    }else{
        let response = await fetch('/me', {
            method:'GET',
            headers:{
                'Authorization': 'Bearer ' + jwtToken
            }

        });
        if(response.headers.has('Content-Type')){
            console.log(response.headers.get('Content-Type'))
            if(response.headers.get('Content-Type') == 'application/json'){

                let content = await response.json()

                userRole = content['roleName']
                userLogin = content['login']

                console.log(userRole)
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
                window.location.replace("/login");
                return 0;
            }
        }else{
            return 0;
        }
    }
}
