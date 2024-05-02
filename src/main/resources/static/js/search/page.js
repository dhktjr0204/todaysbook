function clickNextPageButton(endPage, keyword) {
    location.href = "/book/search?page=" + endPage + "&keyword=" + keyword;
}

function clickPrevPageButton(startPage, keyword) {
    location.href = "/book/search?page=" + (startPage - 6) + "&keyword=" + keyword;
}

function clickPageButton(button, keyword) {
    const page = button.textContent;
    location.href = "/book/search?page=" + (page - 1) + "&keyword=" + keyword;
}

function clickListNextPageButton(endPage, keyword) {
    location.href = "/book/search/list?page=" + endPage + "&keyword=" + keyword;
}

function clickListPrevPageButton(startPage, keyword) {
    location.href = "/book/search/list?page=" + (startPage - 6) + "&keyword=" + keyword;
}

function clickListPageButton(button, keyword) {
    const page = button.textContent;
    location.href = "/book/search/list?page=" + (page - 1) + "&keyword=" + keyword;
}
