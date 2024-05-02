document.addEventListener('DOMContentLoaded', function() {
    const loginButton = document.querySelectorAll(".button")[0];

    loginButton.addEventListener('click', function () {
        const email = document.querySelectorAll('.input')[0].value;
        const password = document.querySelectorAll('.input')[1].value;

        const loginData = {
            'email' : email,
            'password' : password
        };

        fetch('/user/login', {
            method : 'POST',
            headers : {
                'Content-Type' : 'application/json'
            },
            body: JSON.stringify(loginData)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = '/index';
                } else if (response.status === 401) {
                    alert("이메일 또는 비밀번호를 다시 확인해주세요.");
                    window.location.href = '/login';
                } else if (response.status === 403) {
                    alert("탈퇴 요청된 회원입니다");
                    window.location.href = '/login';
                } else {
                    // 기타 다른 에러가 발생한 경우의 처리
                    alert("로그인에 실패했습니다. 다시 시도해주세요.");
                    window.location.href = '/login';
                }
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
                alert('오류가 발생했습니다: ' + error.message);
            });
    });

    const registerButton = document.querySelectorAll('.button')[1];
    registerButton.addEventListener('click', function () {
        window.location.href = '/signup';
    });
});

