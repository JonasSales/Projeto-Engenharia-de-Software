async function login() {
    const email = document.querySelector("#email").value
    const password = document.querySelector("#password").value

    const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    }
    ).catch(err => console.log(err))


    if (response.status === 200) {
        const responseJson = await response.json()
        const token = responseJson.token
        sessionStorage.setItem("token", token)
        showPopup("Login efetuado com sucesso!", "#06cf17")
        setTimeout(() => {
            redirect("http://localhost:8080/html/main-page.html")
        }, 1500)
    } else {
        showPopup("Usuário e/ou senha incorretos!", "#e62424")
    }
}

async function register() {
    const name = document.querySelector("#name").value
    const email = document.querySelector("#email").value
    const password = document.querySelector("#password").value
    const confirm_password = document.querySelector("#confirm-password").value

    if (password == confirm_password) {
        const response = await fetch("http://localhost:8080/user/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: name,
                email: email,
                password: password
            })
        }
        ).catch(err => console.log(err))

        if (response.status === 201) {
        const responseJson = await response.json()
        const id = responseJson.id
        sessionStorage.setItem("id", id)
        showPopup("Cadastro efetuado com sucesso!", "#06cf17")
        setTimeout(() => {
            redirect("http://localhost:8080/html/login.html")
        }, 1500)
    } else if (response.status === 409) {
        showPopup("Usuário já cadastrado!", "#e62424")
    } else {
        showPopup("Confira os dados e tente novamente!", "#e62424")
    }
    } else {
        showPopup("Senhas diferem!", "#e62424")
    }
}

function showPopup(message, color, duration = 3000) {
    const popup = document.getElementById('popupMessage');
    popup.style.background = color
    popup.textContent = message;
    popup.style.display = 'block';

    setTimeout(() => {
        popup.style.display = 'none';
    }, duration);
}

function redirect(url) {
    window.location.href = url
    console.log('chegou')
}