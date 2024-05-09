function withdrawUser() {
    return fetch('/user/withdraw', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(function (response) {
            if(!response.ok) {
                throw new Error('Network response was not ok');
            }
        });
}

function logoutUser() {
    return fetch('/logout', {
        method: 'GET'
    })
        .then(function (response) {
            if (!response.ok) {
                throw new Error('Logout request failed');
            }
        });
}

document.addEventListener('DOMContentLoaded', function() {
    const withdrawText = document.querySelector('.withdraw');

    withdrawText.addEventListener('click', function(event) {
        event.preventDefault();

        const withdrawYN = confirm('정말로 탈퇴하시겠습니까?');

        if(withdrawYN) {
            withdrawUser().then(() => {
                alert('회원 탈퇴되었습니다');
                logoutUser().then(() => {
                    window.location.href = '/';
                }).catch(error => {
                    console.error('Logout failed:', error);
                });
            }).catch(error => {
                console.error('Withdraw failed:', error);
            });
        } else {
            alert('회원 탈퇴가 취소되었습니다.');
        }
    });
});
