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

function updateNickname(newNickname) {
    const requestBody = {
        nickName: newNickname
    };

    fetch('/user/update/nickname', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(function(response) {
            if (!response.ok) {
                throw new Error('Failed to update nickname');
            }
        })
        .then(function(data) {

        })
        .catch(function(error) {
            alert('Error: ' + error.message);
            console.error('Error:', error);
        });
}

function updateAddressInfo(address, zipcode) {
    const requestBody = {
        address: address,
        zipcode: zipcode
    };

    fetch('/user/update/addressInfo', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(function(response) {
            if (!response.ok) {
                throw new Error('Failed to update nickname');
            }
        })
        .then(function(data) {
            alert('회원정보가 수정되었습니다.');
            window.location.href = '/';
        })
        .catch(function(error) {
            alert('Error: ' + error.message);
            console.error('Error:', error);
        });
}

document.addEventListener('DOMContentLoaded', function () {
    const nicknameButton = document.querySelectorAll('.button')[0];
    const inputList = document.querySelectorAll('.input');
    let nickname;

    nicknameButton.addEventListener('click', function(event) {
        event.preventDefault();

        nickname = inputList[0].value;

        if(nickname === null) {
            alert('한 글자 이상의 닉네임이 필요합니다.');
            return;
        }

        checkNicknameAvailability(nickname)
            .then(function(data) {
                if(data.hasOwnProperty('available')) {
                    if(data.available) {
                        alert('사용 가능한 닉네임입니다.');
                    } else {
                        alert('이미 사용 중인 닉네임입니다.');
                    }
                } else {
                    throw new Error('Invalid response data');
                }
            })
            .catch(function (error) {
                console.error('회원 정보 수정 중 오류가 발생하였습니다.\n' + error.message);
            });
    });


    const submitButton = document.querySelector('.submit');
    submitButton.addEventListener('click', function (event) {
        event.preventDefault();
        updateNickname(nickname);
        updateAddressInfo(inputList[1].value, inputList[2].value);
    })
});