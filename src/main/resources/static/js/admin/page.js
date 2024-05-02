function clickUserNextPageButton(endPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/userlist/search?keyword="+ keyword + "&page=" + endPage;
    }else{
        location.href = "/admin/userlist?page=" + endPage;

    }
}

function clickUserPrevPageButton(startPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/userlist/search?keyword="+ keyword + "&page=" + (startPage - 6);
    }else{
        location.href = "/admin/userlist?page=" + (startPage - 6);

    }
}

function clickUserPageButton(button, keyword) {


    const page = button.textContent;

    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/userlist/search?keyword="+ keyword + "&page=" + (page - 1);
    }else {
        location.href = "/admin/userlist?page=" + (page - 1);

    }
}

function clickStockNextPageButton(endPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/stocklist/search?keyword="+ keyword + "&page=" + endPage;
    }else{
        location.href = "/admin/stocklist?page=" + endPage;

    }
}

function clickStockPrevPageButton(startPage, keyword) {
    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/stocklist/search?keyword="+ keyword + "&page=" + (startPage - 6);
    }else{
        location.href = "/admin/stocklist?page=" + (startPage - 6);

    }
}

function clickStockPageButton(button, keyword) {

    const page = button.textContent;

    const currentUrl = window.location.href;

    if (currentUrl.includes("keyword=")) {
        location.href="/admin/stocklist/search?keyword="+ keyword + "&page=" + (page - 1);
    }else {
        location.href = "/admin/stocklist?page=" + (page - 1);

    }
}

