function purchaseItem() {

    var bookId = document.querySelector('.book-id').value;

    // bookName 추출
    var bookName = document.querySelector('.title').textContent.trim();

    // quantity 추출 (예시에서는 수량 입력란에 입력된 값을 가져옵니다)
    var quantity = document.querySelector('.count-input').value;
    if (quantity == 0)
        quantity = 1;

    // price 추출
    var priceText = document.querySelector('.price').textContent.trim();
    var price = parseInt(priceText.replace(/[,원]/g, ''));

    let role = document.querySelector('#role').value;

    const selectedBook = [];
    selectedBook.push({
        bookId: bookId,
        cartBookId: 0,
        bookName: bookName,
        quantity: quantity,
        price: price * quantity,
        mileage: setMileage(price * quantity, role)
    });

    // 서버로 선택된 책 정보를 전송합니다. (여기서는 fetch를 사용하여 POST 요청으로 보냅니다.)
    fetch('/payment/info', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedBook)
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

function setMileage(price, role) {

    var mileageRate;

    switch (role) {
        case 'ROLE_BRONZE':
            mileageRate = 0.03;
            break;
        case 'ROLE_SILVER':
            mileageRate = 0.05;
            break;
        case 'ROLE_GOLD':
            mileageRate = 0.07;
            break;
        case 'ROLE_DIAMOND':
            mileageRate = 0.10;
            break;
        default:
            mileageRate = 0.03; // 기본값은 브론즈
            break;
    }

    return price * mileageRate;
}