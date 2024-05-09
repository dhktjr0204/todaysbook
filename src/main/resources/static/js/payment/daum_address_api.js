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

function getUserInfo() {
    return fetch('/getUserInfo', {
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

document.addEventListener('DOMContentLoaded', function () {
    const searchButton = document.querySelectorAll('.button')[2];
    searchButton.addEventListener('click', (event) => {
        execDaumPostcode();
    });

    const sameInfoWithUserCheckBox = document.querySelector('.check-box');
    if (sameInfoWithUserCheckBox) {
        sameInfoWithUserCheckBox.addEventListener('change', function() {
            if (this.checked) {
                document.querySelector('.detail-address').style.display = 'none';
                const userInfo = getUserInfo().then(function(userInfo) {
                    document.getElementById('user').value = userInfo.name;
                    document.getElementById('address').value = userInfo.address;
                    document.getElementById('postcode').value = userInfo.zipcode;
                }).catch(function(error) {
                    console.error('Error fetching user info:', error);
                });
            } else {
                document.querySelector('.detail-address').style.display = 'flex';
                document.getElementById('user').value = null;
                document.getElementById('address').value = null;
                document.getElementById('postcode').value = null;
            }
        });
    }
});
