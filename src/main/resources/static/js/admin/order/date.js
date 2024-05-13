document.addEventListener('DOMContentLoaded', setInitialDate);

function setInitialDate() {
    const todayDateInput = document.getElementById('todayDate');
    const selectedDate = getQueryParam('date');
    if (selectedDate) {
        todayDateInput.value = selectedDate;
    } else {
        todayDateInput.value = setDate(new Date());
    }
    todayDateInput.addEventListener('change', getData);
}

function getQueryParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

function setDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function getData() {
    const selectedDate = document.getElementById('todayDate').value;
    window.location.href = `/admin/order?date=${selectedDate}`;
}
