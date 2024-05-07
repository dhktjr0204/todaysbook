function clickNextPageButton(endPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/book_registration/search?keyword="+ keyword + "&page=" + (endPage+1);
    }else{
        location.href = "/admin/book_registration?page=" + (endPage+1);

    }
}

function clickPrevPageButton(startPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/book_registration/search?keyword="+ keyword + "&page=" + (startPage - 5);
    }else{
        location.href = "/admin/book_registration?page=" + (startPage - 5);

    }
}

function clickPageButton(button, keyword) {

    const page = button.textContent;

    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/book_registration/search?keyword="+ keyword + "&page=" + page;
    }else {
        location.href = "/admin/book_registration?page=" + page;

    }
}

