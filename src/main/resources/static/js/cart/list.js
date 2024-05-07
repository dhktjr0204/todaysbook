function deleteSelectedCartItems() {
    var selectedItems = document.querySelectorAll('.cart-list-body input[type="checkbox"]:checked');
    var selectedIds = [];
    selectedItems.forEach(function(item) {
        selectedIds.push(item.value);
    });

    if (selectedIds.length === 0) {
        alert('삭제할 상품을 선택해주세요.');
        return;
    }

    if (confirm('선택한 상품을 삭제하시겠습니까?')) {
        fetch('/cart/delete-selected', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // JSON 형식으로 데이터 전송을 명시
            },
            body: JSON.stringify(selectedIds) // JSON 형식으로 데이터 변환
        })
            .then(function(response) {
                if (!response.ok) {
                    throw Error(response.statusText);
                }
                return response.json();
            })
            .then(function(data) {
                if (data.success) {
                    window.location.reload();
                } else {
                    alert('삭제 중 오류가 발생했습니다.');
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
                alert('서버 오류가 발생했습니다.');
            });
    }
}

function postSelectedCartItems() {
     // 체크된 checkbox 요소들을 선택합니다.
        const checkedCheckboxes = document.querySelectorAll('input[type="checkbox"]:checked');

        // 선택된 책 정보를 담을 배열
        const selectedBooks = [];

        // 각 checkbox에 대해 정보 추출
        checkedCheckboxes.forEach(checkbox => {
            // checkbox의 부모 요소인 li 태그를 찾습니다.
            const listItem = checkbox.closest('li');

            // li 태그 내부의 필요한 정보들을 추출합니다.
            const bookName = listItem.querySelector('.book-name').textContent;
            const quantity = parseInt(listItem.querySelector('.quantity').textContent);
            const price = parseFloat(listItem.querySelector('.price').textContent);
            const mileage = listItem.querySelector('.mileage').textContent;

            // 추출한 정보를 객체로 저장하여 배열에 추가합니다.
            selectedBooks.push({
                bookName: bookName,
                quantity: quantity,
                price: price,
                mileage: mileage
            });
        });

        // 서버로 선택된 책 정보를 전송합니다. (여기서는 fetch를 사용하여 POST 요청으로 보냅니다.)
        fetch('/payment/info', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(selectedBooks)
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
