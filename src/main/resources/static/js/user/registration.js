function checkEmailAvailability(email) {
    return fetch('/checkEmailAvailability?email=' + encodeURIComponent(email), {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function (response) {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        });
}

function checkNicknameAvailability(nickname) {
    return fetch('/checkNicknameAvailability?nickname=' + encodeURIComponent(nickname), {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function(response) {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        });
}

document.addEventListener('DOMContentLoaded', function() {
    // 이메일 중복 확인 버튼 클릭 시
    const emailButton = document.querySelectorAll('.button')[0];

    emailButton.addEventListener('click', function() {
        const emailInput = document.querySelectorAll('.input')[1];
        const email = emailInput.value;

        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if(!emailPattern.test(email)){
            alert('올바른 이메일 주소를 입력하세요.');
            return;
        }

        checkEmailAvailability(email) // 이메일 중복 확인 함수 호출
            .then(function(data) {
                if (data.hasOwnProperty('available')) {
                    if (data.available) {
                        alert('사용 가능한 이메일입니다.');
                    } else {
                        alert('이미 사용 중인 이메일입니다.');
                    }
                } else {
                    throw new Error('Invalid response data');
                }
            })
            .catch(function(error) {
                alert('Error: ' + error.message);
                alert(error);
            });
    });

    // 닉네임 중복 확인 버튼 클릭 시
    const nicknameButton = document.querySelectorAll('.button')[1];

    nicknameButton.addEventListener('click', function() {
        const nicknameInput = document.querySelectorAll('.input')[2];
        const nickname = nicknameInput.value;

        checkNicknameAvailability(nickname) // 닉네임 중복 확인 함수 호출
            .then(function(data) {
                if (data.hasOwnProperty('available')) {
                    if (data.available) {
                        alert('사용 가능한 닉네임입니다.');
                    } else {
                        alert('이미 사용 중인 닉네임입니다.');
                    }
                } else {
                    throw new Error('Invalid response data');
                }
            })
            .catch(function(error) {
                alert('Error: ' + error.message);
            });
    });
});

// 가입하기 버튼
document.addEventListener('DOMContentLoaded', function() {
    const submitButton = document.querySelector('.submit');

    submitButton.addEventListener('click', function(event) {
        event.preventDefault(); // 폼의 기본 동작인 제출 방지

        const formInputs = document.querySelectorAll('.input');
        const userData = {};

        formInputs.forEach(function(input, index) {
            const label = ["name", "email", "nickName", "password", "passwordCheck", "address", "detailAddress", "zipcode"];

            if (label[index] === 'detailAddress') {
                userData['address'] += ', ' + input.value;
            } else {
                userData[label[index]] = input.value;
            }
        });

        alert(userData['address']);

        const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&~]{8,}$/;

        if(!passwordPattern.test(userData['password'])) {
            alert('비밀번호는 영어, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다.');
            return;
        }
        else if(userData['password'] !== userData['passwordCheck']) {
            alert('입력된 비밀번호가 같지 않습니다.');
            return;
        }

        // 서버로 보낼 POST 요청 준비
        fetch('/user/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // 회원가입이 완료되면 로그인페이지로 이동
                alert('회원가입이 완료되었습니다.');
                window.location.href = '/login';
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
                // 예: 오류 메시지 표시
                alert('오류가 발생했습니다: ' + error.message);
            });
    });

    const cancelButton = document.querySelector('.cancel');
    cancelButton.addEventListener('click', () => {
        window.location.href = '/';
    });
});







