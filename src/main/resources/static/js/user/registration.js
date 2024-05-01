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
        const emailInput = document.querySelectorAll('.input')[0];
        const email = emailInput.value;

        checkEmailAvailability(email) // 이메일 중복 확인 함수 호출
            .then(function(data) {
                if (data.hasOwnProperty('available')) {
                    console.log(data.available);
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
        const nicknameInput = document.querySelectorAll('.input')[1];
        const nickname = nicknameInput.value;

        checkNicknameAvailability(nickname) // 닉네임 중복 확인 함수 호출
            .then(function(data) {
                if (data.hasOwnProperty('available')) {
                    console.log(data.available);
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

        // 각 입력 필드에서 사용자 입력을 userData 객체에 저장
        formInputs.forEach(function(input, index) {
            const label = document.querySelectorAll('.label')[index].textContent.trim();
            const value = input.value;
            userData[label] = value;
        });

        // 서버로 보낼 POST 요청 준비
        fetch('/registration', {
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
                // 서버로부터 받은 응답 처리
                console.log(data);
                // 예: 회원가입 성공 시 다음 페이지로 리다이렉트
                window.location.href = '/success';
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
                // 예: 오류 메시지 표시
                alert('오류가 발생했습니다: ' + error.message);
            });
    });
});





