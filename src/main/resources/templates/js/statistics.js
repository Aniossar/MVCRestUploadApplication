const tableBody = document.querySelector(".table_container table tbody")
for(let i=0;i<50;i++){
    tableBody.innerHTML += `
        <tr>
            <td>Date</td>
            <td>Салон</td>
            <td>Пользователь</td>
            <td>Тип события</td>
            <td>Материал</td>
            <td>Кол-во слэбов</td>
            <td>Площадь изделия</td>
            <td>Сумма материала</td>
            <td>Сумма доп. работ</td>
            <td>Сумма счета</td>
        </tr>
    `

}

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

}, ()=>{
    //onRejected
})

setInterval(()=>{
    document.querySelector('.user_name').innerHTML = userLogin
    document.querySelector('.user_info').innerHTML = userRole
    // console.log('userLogin: ' + userLogin)
    // console.log('userRole: ' + userRole)
}, 5000)


function getStatistics(){

}


function updateTableView(){
    //using
}

function downloadStatisticsAsXls(){

}
