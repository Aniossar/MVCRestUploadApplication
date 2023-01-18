

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

    getUserActivities()
    getSysAndUsersInfo()
    console.log(userRole)

}, ()=>{
    //onRejected
})

setInterval(()=>{
    // document.querySelector('.user_name').innerHTML = userLogin
    // document.querySelector('.user_info').innerHTML = userRole
    // console.log('userLogin: ' + userLogin)
    // console.log('userRole: ' + userRole)
    getUserActivities()
    getSysAndUsersInfo()
}, 5000)

async function getUserActivities(){

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let response = await fetch('/api/allActivities', {
        method:'GET',
        headers:{
            'Authorization': 'Bearer ' + accessToken
        }
    });

    let content = await response.json()

    let table = document.querySelector('.table_container > table > tbody')
     table.innerHTML = ""
    for(let i=0;i<content.length;i++){
        let date = new Date(content[i]['activityTime'])
        table.innerHTML += `
                <tr>            
                    <td>${date.toLocaleString("RU")}</td>
                    <td>${content[i]['login']}</td>
                    <td>${content[i]['activityType']}</td>
                    <td>${content[i]['activityMessage']}</td>
                </tr>`
    }


}

async function getSysAndUsersInfo(){

    let accessToken = localStorage.getItem(ACCESS_TOKEN_NAME);

    let response = await fetch('/api/getApplicationStart', {
        method:'GET',
        headers:{
            'Authorization': 'Bearer ' + accessToken
        }
    });

    let appStartedAt = await response.text()
    if(response.status !== 200) appStartedAt = 'Ошибка'

    response = await fetch('/api/getApplicationWorkingTime', {
        method:'GET',
        headers:{
            'Authorization': 'Bearer ' + accessToken
        }
    });

    let appWorksTime = await response.text()
    if(response.status !== 200) appWorksTime = 'Ошибка'

    // response = await fetch('/api/getApplicationMemoryStatus', {
    //     method:'GET',
    //     headers:{
    //         'Authorization': 'Bearer ' + accessToken
    //     }
    // });
    //
    // let appMemoryStatus = await response.text()


    // response = await fetch('/api/pingAlive', {
    //     method:'GET',
    //     headers:{
    //         'Authorization': 'Bearer ' + accessToken
    //     }
    // });
    //
    // let appAllUsersNumber = await response.text()
    //
    response = await fetch('/api/showUserStats ', {
        method:'GET',
        headers:{
            'Authorization': 'Bearer ' + accessToken
        }
    });

    let appActiveUser = await response.json()
    if(response.status !== 200) appActiveUser = 'Ошибка'

    document.querySelector('.sys_start_date > p + p').textContent = appStartedAt
    document.querySelector('.sys_work_time > p + p').textContent = appWorksTime
    document.querySelector('.sys_memory > p + p').textContent = "Нет запроса"

    document.querySelector('.users_all > p + p').textContent = "Нет запроса"
    document.querySelector('.users_online > p + p').textContent = appActiveUser.length
    document.querySelector('.users_offline > p + p').textContent = "Нет запроса"


}




