
let usersRows = document.querySelectorAll(".user_row")
function selectUser(element){

    for(let el of usersRows){
        el.style.backgroundColor = 'white'
    }
    element.style.backgroundColor = 'grey'

}

function showUserInfo(){

}