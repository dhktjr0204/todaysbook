function deleteSelectedCartItems() {
    var unselectedItems = document.querySelectorAll('.cart-list-body input[type="checkbox"]:not(:checked)');
    var unselectedIds = [];
    unselectedItems.forEach(function(item) {
        unselectedIds.push(item.value);
    });

    if (unselectedIds.length === 0) {
        alert('삭제할 상품을 선택해주세요.');
        return;
    }

    if (confirm('선택되지 않은 품목들은 삭제하시겠습니까?')) {
        fetch('/cart/delete-unselected', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // JSON 형식으로 데이터 전송을 명시
            },
            body: JSON.stringify(unselectedIds) // JSON 형식으로 데이터 변환
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
            const quantity = parseInt(listItem.querySelector('.quantity_count').textContent);
            const price = parseFloat(listItem.querySelector('.price').textContent);
            const mileage = listItem.querySelector('.mileage').textContent.replace('M', '');

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

//총 금액 실시간 업데이트

// 페이지 로딩 시 전체 상품 선택 및 총 주문 금액 초기화
document.addEventListener("DOMContentLoaded", function() {
    var allCheckBox = document.querySelectorAll('.cart-list-body input[type="checkbox"]');
    allCheckBox.forEach(function(item) {
        item.checked = true; // 모든 체크박스 선택
    });
    updateTotalPrice();
});


// 체크박스 상태 변경 시 총 주문 금액 업데이트
// document.querySelectorAll('.cart-list-body input[type="checkbox"]').forEach(function(checkbox) {
//     checkbox.addEventListener('change', function() {
//         updateTotalPrice();
//
//
//     });
// });




// function updateTotalPrice() {
//     var totalPriceElement = document.querySelector('.cart-list-bottom p:first-child');
//     var totalPrice = 0;
//
//
//     // 체크된 상품의 가격을 합산하여 총 주문 금액 계산
//     document.querySelectorAll('.cart-list-body input[type="checkbox"]:checked').forEach(function (item) {
//         var cartItem = item.parentElement.parentElement; // 각 상품 리스트 아이템
//         var itemPrice = parseInt(cartItem.querySelector('.price').innerText.replace('원', '')); // 상품 가격
//         // var itemQuantity = parseInt(cartItem.querySelector('.quantity').innerText); // 상품 수량
//         totalPrice += itemPrice
//     });
//
//
//     // 총 주문 금액을 업데이트하여 화면에 표시
//     totalPriceElement.innerText = '총 주문 금액 ' + totalPrice + '원';
//
//     // 마일리지 업데이트 함수
//     function updateTotalMileage(totalPrice) {
//         var totalMileageElement = document.getElementById("totalMileage");
//         var membershipLevel = document.getElementById("membershipLevel").innerText.trim();
//
//         // 등급에 따른 마일리지 비율 계산
//         var mileageRate;
//         switch (membershipLevel) {
//             case '브론즈':
//                 mileageRate = 0.03;
//                 break;
//             case '실버':
//                 mileageRate = 0.05;
//                 break;
//             case '골드':
//                 mileageRate = 0.07;
//                 break;
//             case '다이아몬드':
//                 mileageRate = 0.10;
//                 break;
//             default:
//                 mileageRate = 0.03; // 기본값은 브론즈
//                 break;
//         }
//
//         // 총 마일리지 계산
//         var totalMileage = totalPrice * mileageRate;
//
//         // 총 마일리지를 화면에 업데이트
//         totalMileageElement.innerText = '총 적립 마일리지 ' + totalMileage.toFixed(2) + 'M';
//     }
function updateTotalPrice() {
    var totalPriceElement = document.querySelector('.cart-list-bottom p:first-child');
    var totalPrice = 0;

    // 체크된 상품의 가격을 합산하여 총 주문 금액 계산
    document.querySelectorAll('.cart-list-body input[type="checkbox"]:checked').forEach(function(item) {
        var cartItem = item.parentElement.parentElement; // 각 상품 리스트 아이템
        var itemPrice = parseInt(cartItem.querySelector('.price').innerText.replace('원', '')); // 상품 가격
        totalPrice += itemPrice;
    });

    // 총 주문 금액을 업데이트하여 화면에 표시
    totalPriceElement.innerText = '총 주문 금액 ' + totalPrice + '원';

    // 총 주문 금액에 따른 배송료 업데이트
    var deliveryFeeElement = document.getElementById("deliveryFee");
    if (totalPrice >= 20000) {
        deliveryFeeElement.textContent = "무료";
    } else {
        deliveryFeeElement.textContent = "3000원";
    }

    // 총 주문 금액을 반환
    return totalPrice;
}

function updateTotalMileage(totalPrice) {
    var totalMileageElement = document.getElementById("totalMileage");
    var membershipLevel = document.getElementById("membershipLevel").innerText.trim();

    // 등급에 따른 마일리지 비율 계산
    var mileageRate;
    switch (membershipLevel) {
        case 'BRONZE':
            mileageRate = 0.03;
            break;
        case 'SILVER':
            mileageRate = 0.05;
            break;
        case 'GOLD':
            mileageRate = 0.07;
            break;
        case 'DIAMOND':
            mileageRate = 0.10;
            break;
        default:
            mileageRate = 0.03; // 기본값은 브론즈
            break;
    }

    // 총 마일리지 계산
    var totalMileage = totalPrice * mileageRate;

    // 총 마일리지를 화면에 업데이트
    totalMileageElement.innerText = '총 적립 마일리지 ' + totalMileage + 'M';
}


document.querySelectorAll('.cart-list-body input[type="checkbox"]').forEach(function(checkbox) {
    checkbox.addEventListener('change', function() {
        var totalPrice = updateTotalPrice(); // 총 주문 금액 업데이트 및 반환
        updateTotalMileage(totalPrice); // 총 마일리지 업데이트
    });
});

    // // 총 주문 금액에 따른 배송료 업데이트
    // var deliveryFeeElement = document.getElementById("deliveryFee");
    // if (totalPrice >= 20000) {
    //     deliveryFeeElement.textContent = "무료";
    // } else {
    //     deliveryFeeElement.textContent = "3000원";
    // }




function deleteSelectedCartItemsIfNotChecked() {
    var selectedItems = document.querySelectorAll('.cart-list-body input[type="checkbox"]:checked');
    var allItems = document.querySelectorAll('.cart-list-body input[type="checkbox"]');

    if (selectedItems.length !== allItems.length) {
        if (confirm('선택되지 않은 품목은 삭제해주세요.')) {
            return true;
        } else {
            return false;
        }
    }
    // 모든 아이템이 선택되었을 경우에는 바로 주문 페이지로 이동
    location.href = '/payment/info';
}




////수량 증가 감소

function increaseQuantity(cartBookId) {
    fetch('/cart/increase-quantity/' + cartBookId, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                window.location.reload(); // 페이지 새로고침
            } else {
                alert('수량 증가 중 오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버 오류가 발생했습니다.');
        });
}

// 감소 버튼 클릭 시
function decreaseQuantity(cartBookId) {
    fetch('/cart/decrease-quantity/' + cartBookId, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                window.location.reload(); // 페이지 새로고침
            } else {
                alert('수량 감소 중 오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버 오류가 발생했습니다.');
        });
}