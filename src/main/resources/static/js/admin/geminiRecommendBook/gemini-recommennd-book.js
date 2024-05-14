const quantityRange = document.getElementById('quantityRange');
const quantityValue = document.getElementById('quantityValue');
const temperatureRange = document.getElementById('temperatureRange');
const temperatureValue = document.getElementById('temperatureValue');

quantityRange.addEventListener('input', function() {
    quantityValue.textContent = this.value;
});

temperatureRange.addEventListener('input', function() {
    temperatureValue.textContent = this.value;
});

function recommendBooks() {
    const quantity = quantityValue.textContent;
    const temperature = temperatureValue.textContent;
    const loadingModal = document.getElementById('loading-modal');

    loadingModal.style.display = 'block';

    $.ajax({
        url: '/gemini/recommendBooks',
        type: 'POST',
        data: {
            quantity: quantity,
            temperature: temperature
        },
        success: function(response) {
            loadingModal.style.display = 'none';
            setTimeout(() => {
                alert('책 추천이 완료되었습니다.');
                location.reload();
            }, 500);
        },
        error: function(error) {
            console.log(error);
            loadingModal.style.display = 'none';
            // alert('책 추천 중 오류가 발생했습니다.');
            // location.reload();
        }
    });
}