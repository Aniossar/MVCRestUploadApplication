let passwordNewInput = document.querySelector('input[name="pw_new"]')
let passwordNewRepeatInput = document.querySelector('input[name="pw_new_repeat"]')



let formRestore = document.querySelector('.formRestore')


formRestore.addEventListener("submit", (e)=>{
    e.preventDefault();

    saveNewPassword()

})


function checkPassword(){

    let success = true;

    if((passwordNewInput.value === passwordNewRepeatInput.value) && (passwordNewInput.value !== "")){
        passwordNewInput.style.color = 'var(--color-primary)'
        passwordNewRepeatInput.style.color = 'var(--color-primary)'
    }else{
        passwordNewInput.style.color = 'red'
        passwordNewRepeatInput.style.color = 'red'
        success = false;

    }

    return success
}

async function saveNewPassword(){

    if(!checkPassword()){
        return
        console.log("WRONG PASSWORD");
    }


    let restoreToken = window.location.pathname.split("toker=")[1].replaceAll("{").replaceAll("}");
    console.log(restoreToken)

    let filterObject = {
        "restoreToken":restoreToken,
        "newPassword":passwordNewInput.value,
    }

    let response = await fetch("/resetPassword", {
        method:'POST',
        headers:{
            'Authorization': 'Bearer ' + restoreToken,
            'Content-Type': 'application/json;charset=utf-8'
        },
        body:JSON.stringify(filterObject)
    });

    await response

    if(response.status == 200){
        console.log("SUCCESS CHANGING PW")
    }else {
        console.log("FAIL CHANGING PW : " + response.status)
    }

}

