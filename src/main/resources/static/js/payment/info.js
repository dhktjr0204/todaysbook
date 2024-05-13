function postAddressAndMileageInfo() {
    // 받으시는 분의 정보와 우편번호, 주소, 상세주소 값을 가져옴
    var user = document.getElementById('user').value;
    var postcode = document.getElementById('postcode').value;
    var address = document.getElementById('address').value;
    var detailAddress = document.getElementById('detailAddress').value;
    var usedMileage = document.querySelector('.mileage-input').value;
    // 원 제거 하기 위한 정규표현식
    var totalPrice = document.getElementById('totalPriceDisplay').innerHTML.replace(/\D/g, '');

    // JSON 형식으로 데이터를 구성
    var dataToSend = {
        user: user,
        postcode: postcode,
        address: address,
        detailAddress: detailAddress,
        usedMileage: usedMileage,
        totalPrice: totalPrice
    };

    fetch('/payment/card', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dataToSend)
        })
        .then(response => response.text()) // 서버로부터의 응답 body를 텍스트로 읽어옴
        .then(data => {
          window.location.href = data; // 페이지를 리다이렉트할 URL로 이동
        })
        .catch(error => {
            // 오류가 발생한 경우 처리합니다.
            console.error('Error sending selected books to server:', error);
        });
}