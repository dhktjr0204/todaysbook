function clickNextPageButton(endPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/stocklist/search?keyword="+ keyword + "&page=" + endPage;
    }else{
        location.href = "/admin/stocklist?page=" + endPage;

    }
}

function clickPrevPageButton(startPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/stocklist/search?keyword="+ keyword + "&page=" + (startPage - 6);
    }else{
        location.href = "/admin/stocklist?page=" + (startPage - 6);

    }
}

function clickPageButton(button, keyword) {

    const page = button.textContent;

    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/stocklist/search?keyword="+ keyword + "&page=" + (page - 1);
    }else {
        location.href = "/admin/stocklist?page=" + (page - 1);

    }
}

