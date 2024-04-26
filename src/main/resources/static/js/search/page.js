function clickNextPageButton() {
    location.href = "/book/search?page=" + endPage + "&keyword=" + keyword;
}

function clickPrevPageButton() {
    location.href = "/book/search?page=" + (startPage - 6) + "&keyword=" + keyword;
}

function clickPageButton(button) {
    const page = button.textContent;
    location.href = "/book/search?page=" + (page - 1) + "&keyword=" + keyword;
}