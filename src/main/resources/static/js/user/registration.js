function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detailAddress").focus();
        }
    }).open();
}

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

let emailAvailability = false;
let nicknameAvailability = false;

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
                        emailAvailability = true;
                    } else {
                        alert('이미 사용 중인 이메일입니다.');
                        emailInput.value = null;
                        emailInput.focus();
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

        if(nickname.length > 8) {
            alert('닉네임의 길이는 최대 8글자입니다.');
            return;
        } else if(nickname.length < 2) {
            alert('닉네임의 길이는 최소 2글자입니다.');
            return;
        }

        checkNicknameAvailability(nickname) // 닉네임 중복 확인 함수 호출
            .then(function(data) {
                if (data.hasOwnProperty('available')) {
                    if (data.available) {
                        alert('사용 가능한 닉네임입니다.');
                        nicknameAvailability = true;
                    } else {
                        alert('이미 사용 중인 닉네임입니다.');
                        nicknameInput.value = null;
                        nicknameInput.focus();
                    }
                } else {
                    throw new Error('Invalid response data');
                }
            })
            .catch(function(error) {
                alert('Error: ' + error.message);
            });
    });

    const searchButton = document.querySelectorAll('.button')[2];
    searchButton.addEventListener('click', (event) => {
        event.preventDefault();
        execDaumPostcode();
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

        if(!emailAvailability) {
            alert('이메일 중복 확인을 해주세요');
            return;
        }

        if(!nicknameAvailability) {
            alert('닉네임 중복 확인을 해주세요');
            return;
        }

        for(const key in userData) {
            if(userData.hasOwnProperty(key) && userData[key] === '') {
                alert('입력 칸을 빠짐없이 채워주세요');
                return;
            }
        }

        const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&~]{8,}$/;

        if(!passwordPattern.test(userData['password'])) {
            alert('비밀번호는 영어, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다.');
            formInputs[3].value = null;
            formInputs[4].value = null;
            formInputs[3].focus();
            return;
        } else if(userData['password'] !== userData['passwordCheck']) {
            alert('입력된 비밀번호가 같지 않습니다.');
            formInputs[3].value = null;
            formInputs[4].value = null;
            formInputs[3].focus();
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