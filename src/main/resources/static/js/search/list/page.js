function clickNextPageButton(endPage, keyword) {
    location.href = "/book/search/list?page=" + endPage + "&keyword=" + keyword;
}

function clickPrevPageButton(startPage, keyword) {
    location.href = "/book/search/list?page=" + (startPage - 6) + "&keyword=" + keyword;
}

function clickPageButton(button, keyword) {
    const page = button.textContent;
    location.href = "/book/search/list?page=" + (page - 1) + "&keyword=" + keyword;
}
