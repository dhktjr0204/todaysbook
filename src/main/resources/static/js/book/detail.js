function clickFavorite(button) {

    const image = button.querySelector('img');

    image.classList.toggle('favorite');

    let url = image.classList.contains('favorite') ? '/favorite_book/add' : '/favorite_book/delete';
    let bookId = document.querySelector('.book-id').value;

    let type = image.classList.contains('favorite') ? 'GET' : 'DELETE';
    let message = image.classList.contains('favorite') ?
        '찜하기 목록에 추가 되었습니다.' : '찜하기 목록에서 제거 되었습니다.';

    $.ajax({

        type: type,
        url: url,
        data: {
            bookId: bookId
        },
        success: function (response) {

            alert(message);
        },
        error: function (error) {

            console.error(error);
        }
    });
}



///업데이트 부분

function addCart(button) {
    // 클릭된 버튼에서 bookId 값 가져오기
    const bookId = button.dataset.id;

    // bookCount 값을 가져오기
    const count = document.querySelector('.count-input').value;


    // 도서 개수가 0보다 큰지 확인
    if (count > 0) {
        // AJAX를 사용하여 서버로 장바구니 추가 요청 전송
        const xhr = new XMLHttpRequest();
        xhr.open('POST', '/cart/add', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    // 장바구니 추가 성공 시 알림창 띄우기
                    alert('장바구니에 상품이 추가되었습니다.');
                } else {
                    // 에러 발생 시 알림창 띄우기
                    alert('장바구니 추가에 실패했습니다. 다시 시도해주세요.');
                }
            }
        };
        xhr.send(JSON.stringify({ bookId: bookId ,count: count}));
    } else {
        // 도서 개수가 0인 경우 알림창 띄우기
        alert('일시 품절 된 상품입니다.');
    }
}