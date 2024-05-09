document.addEventListener("DOMContentLoaded", function () {
    // 페이지 로딩 시 총 상품 가격과 배송료를 업데이트합니다.
    updateTotalPrice();
});

// 총 주문 금액과 배송료를 업데이트하는 함수
function updateTotalPrice() {
    var totalPriceElement = document.getElementById("totalOrderAmount");
    var totalPrice = parseInt(totalPriceElement.textContent.replace('원', ''));

    var deliveryFeeElement = document.getElementById("deliveryFee");
    var deliveryFee = totalPrice >= 20000 ? "무료" : "3000원";
    deliveryFeeElement.textContent = deliveryFee;

    var totalPriceDisplayElement = document.getElementById("totalPriceDisplay");
    var totalPriceWithDelivery = totalPrice + (deliveryFee === "무료" ? 0 : 3000);
    totalPriceDisplayElement.textContent = totalPriceWithDelivery + "원";
}


//마일리지


// 마일리지 입력란에 입력이 발생할 때마다 호출되는 함수
function handleMileageInput() {
    // 마일리지 입력란에서 입력된 값을 가져옴
    let mileageInput = document.getElementById("mileage-input");
    let usedMileage = parseInt(mileageInput.value);

    // 보유 마일리지를 가져옴
    let totalMileageElement = document.getElementById("mileage");
    let totalMileage = parseFloat(totalMileageElement.textContent.replace('M', ''));

    // 결제 금액을 가져옴
    let totalPriceElement = document.getElementById("totalOrderAmount");
    let totalPrice = parseInt(totalPriceElement.textContent.replace('원', ''));

    // 만약 입력된 마일리지가 보유 마일리지를 초과한다면,
    if (usedMileage > totalMileage) {
        alert("보유 마일리지를 초과합니다!");
        // 마일리지 입력란을 초기화합니다.
        mileageInput.value = '';
        usedMileage = 0; // 사용된 마일리지를 0으로 설정
    }


    // 총 결제 금액을 계산
    let totalPriceAfterMileage = isNaN(usedMileage) ? totalPrice : totalPrice - usedMileage;

// // 사용된 마일리지를 업데이트 (보유 마일리지에서 차감)
//     totalMileage -= usedMileage;
// // 보유 마일리지를 업데이트
//     totalMileageElement.textContent = totalMileage + "M";

    // 총 결제 금액을 업데이트
    let totalPriceDisplayElement = document.getElementById("totalPriceDisplay");
    totalPriceDisplayElement.textContent = totalPriceAfterMileage + "원";

}

// 마일리지 입력란에 이벤트 리스너를 추가
let mileageInput = document.getElementById("mileage-input");
mileageInput.addEventListener('input', handleMileageInput);


//////
// 전액 사용 체크박스의 상태에 따라 처리하는 함수
function handleUseAllMileageCheckbox() {
    // 전액 사용 체크박스의 상태를 가져옴
    let useAllMileageCheckbox = document.getElementById("use-all-mileage-checkbox");
    let isChecked = useAllMileageCheckbox.checked;

    // 만약 체크박스가 체크되어 있다면,
    if (isChecked) {
        // 가지고 있는 마일리지를 가져옴
        let totalMileageElement = document.getElementById("mileage");
        let totalMileage = parseFloat(totalMileageElement.textContent.replace('M', ''));

        // 마일리지 입력란에 가지고 있는 마일리지를 입력
        let mileageInput = document.getElementById("mileage-input");
        mileageInput.value = totalMileage;

        // 마일리지 입력 이벤트를 발생시켜 결제 금액을 업데이트
        handleMileageInput();
    } else {
        // 체크박스가 체크되어 있지 않다면, 마일리지 입력란을 비웁니다.
        let mileageInput = document.getElementById("mileage-input");
        mileageInput.value = '';

        // 마일리지 입력 이벤트를 발생시켜 결제 금액을 업데이트
        handleMileageInput();
    }
}

// 전액 사용 체크박스에 이벤트 리스너를 추가
let useAllMileageCheckbox = document.getElementById("use-all-mileage-checkbox");
useAllMileageCheckbox.addEventListener('change', handleUseAllMileageCheckbox);




