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