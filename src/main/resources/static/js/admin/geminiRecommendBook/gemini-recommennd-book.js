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