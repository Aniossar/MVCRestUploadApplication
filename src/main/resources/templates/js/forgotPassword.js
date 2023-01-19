let emailInput = document.querySelector('input[name="email"]')
let formEmail = document.querySelector('.formEmail')

formEmail.addEventListener("submit", (e)=>{
    e.preventDefault();

    sendEmail()

})


async function sendEmail(){


    let messageObject = {
        "message":emailInput.value
    }

    let response = await fetch("/forgottenPassword", {
        method:'POST',
        headers:{
            'Content-Type': 'application/json;charset=utf-8'
        },
        body:JSON.stringify(messageObject)
    });

    await response

    if(response.status == 200){
        console.log("SUCCESS")
    }else {
        console.log("FAIL")
    }
}