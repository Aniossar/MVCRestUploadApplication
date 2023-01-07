

authStatus.then(()=>{

    //onFulfilled
    document.querySelector('.user_name').innerHTML = userLogin
    document.querySelector('.user_info').innerHTML = userRole
    getUserActivities()

}, ()=>{
    //onRejected
})

setInterval(()=>{
    document.querySelector('.user_name').innerHTML = userLogin
    document.querySelector('.user_info').innerHTML = userRole
    // console.log('userLogin: ' + userLogin)
    // console.log('userRole: ' + userRole)
    getUserActivities()
}, 5000)

async function getUserActivities(){

    let jwtToken = localStorage.getItem('jwt-token');

    let response = await fetch('/api/allActivities', {
        method:'GET',
        headers:{
            'Authorization': 'Bearer ' + jwtToken
        }
    });

    let content = await response.json()
    event_table.innerHTML = ""
    for(let i=0;i<content.length;i++){
        let date = new Date(content[i]['activityTime'])
        event_table.innerHTML += `
                <tr>            
                    <td>${date.toLocaleString("RU")}</td>
                    <td>${content[i]['login']}</td>
                    <td>${content[i]['activityType']}</td>
                    <td>${content[i]['activityMessage']}</td>
                </tr>`
    }


}



