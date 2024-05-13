function clickNextPageButton(endPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/sales_book?keyword="+ keyword + "&page=" + endPage;
    }else{
        location.href = "/admin/sales_book?page=" + endPage;

    }
}

function clickPrevPageButton(startPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/sales_book?keyword="+ keyword + "&page=" + (startPage - 6);
    }else{
        location.href = "/admin/sales_book?page=" + (startPage - 6);

    }
}

function clickPageButton(button, keyword) {

    const page = button.textContent;

    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/sales_book?keyword="+ keyword + "&page=" + (page - 1);
    }else {
        location.href = "/admin/sales_book?page=" + (page - 1);

    }
}

