function clickNextPageButton(endPage, keyword) {

    const currentUrl = window.location.href;
    const url = currentUrl.split("?");

    const sortSelect = document.querySelector('.sort-select');
    let selectValue = sortSelect.options[sortSelect.selectedIndex].value;

    location.href = url[0] + "?page=" + endPage + "&type=" + selectValue;
}

function clickPrevPageButton(startPage, keyword) {

    const currentUrl = window.location.href;
    const url = currentUrl.split("?");

    const sortSelect = document.querySelector('.sort-select');
    let selectValue = sortSelect.options[sortSelect.selectedIndex].value;


    location.href = url[0] + "?page=" + (startPage - 6) + "&type=" + selectValue;
}

function clickPageButton(button, keyword) {

    const page = button.textContent;

    const currentUrl = window.location.href;
    const url = currentUrl.split("?");

    const sortSelect = document.querySelector('.sort-select');
    let selectValue = sortSelect.options[sortSelect.selectedIndex].value;


    location.href = url[0] + "?page=" + (page - 1) + "&type=" + selectValue;
}

document.querySelector('.sort-select').addEventListener('change', function () {

    let selectValue = this.options[this.selectedIndex].value;

    const currentUrl = window.location.href;
    const url = currentUrl.split("?");


    if (selectValue === "title") {
        location.href = url[0];
    }
    if (selectValue === "bestseller") {
        location.href = url[0] + "?type=bestseller";
    }
    if (selectValue === "review") {
        location.href = url[0] + "?type=review";
    }

});