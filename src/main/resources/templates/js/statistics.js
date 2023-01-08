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
