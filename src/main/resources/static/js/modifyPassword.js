function checkPasswordAvailability(originPassword, newPassword) {
    return fetch('/checkPasswordAvailability?originPassword=' + encodeURIComponent(originPassword) + '&newPassword=' + encodeURIComponent(newPassword), {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function (response) {
            if(response.status === 400) {
                alert('이전 비밀번호가 올바르지 않습니다.');
                document.querySelectorAll('.input')[0].value = null;
                document.querySelectorAll('.input')[0].focus();
                return;
            } else if (!(response.ok)) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        });
}

async function updateUserPassword(newPassword) {
    const requestBody = JSON.stringify({
        password: newPassword
    });

    try {
        const response = await fetch('/user/update/password', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: requestBody
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(msg => {
                        if(response.status === 401) {
                            alert(msg);
                        } else if(response.status === 400) {
                            alert(msg);
                            throw new Error(msg);
                        } else if(response.status === 404) {
                            alert(msg);
                        }
                    });
                } else {
                    return response.text();
                }
            })
            .then(data => {
                alert('비밀번호가 변경되었습니다.');
                window.location.href = '/';
            })
            .catch(function(error) {
                console.error('Error:', error);
            });

        const responseData = await response.json();
        console.log('Password updated successfully:', responseData);
    } catch (error) {
        console.error('Error updating password:', error.message);
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const modifyButton = document.querySelector('.submit');

    modifyButton.addEventListener('click', async function (event) {
        event.preventDefault();
        const passwordInputList = document.querySelectorAll('.input');

        if (Array.from(passwordInputList).some(input => input.value.trim() === '')) {
            alert('빈 칸을 빠짐없이 작성해주세요');
            return;
        }

        const originPassword = passwordInputList[0].value;
        const newPassword = passwordInputList[1].value;
        const newPasswordCheck = passwordInputList[2].value;

        const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&~]{8,}$/;

        if(!passwordPattern.test(newPassword)) {
            alert('비밀번호는 영어, 숫자, 특수문자를 포함하여 8자 이상, 20자 이하이어야 합니다.');
            passwordInputList[1].value = null;
            passwordInputList[2].value = null;
            passwordInputList[1].focus();
            return;
        } else if(newPassword !== newPasswordCheck) {
            alert('입력된 비밀번호가 같지 않습니다.');
            passwordInputList[1].value = null;
            passwordInputList[2].value = null;
            passwordInputList[1].focus();
            return;
        } else {
            checkPasswordAvailability(originPassword, newPassword)
                .then(function (data) {
                    if (data.hasOwnProperty('available')) {
                        if (data.available) {
                            updateUserPassword(newPassword);
                        } else {
                            alert('비밀번호 변경이 실패되었습니다.');
                        }
                    } else {
                        throw new Error('Invalid response data');
                    }
                })
                .catch(function (error) {
                    console.error('비밀번호 변경 중 오류가 발생하였습니다.\n' + error.message);
                });
        }
    });
});