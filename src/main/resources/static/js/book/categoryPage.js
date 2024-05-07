function clickNextPageButton(endPage, keyword) {
    const currentUrl = window.location.href;
    const url =currentUrl.split("?");


    location.href=url[0]+"?page=" + endPage;
}

function clickPrevPageButton(startPage, keyword) {

    const currentUrl = window.location.href;
    const url =currentUrl.split("?");


    location.href=url[0]+"?page=" + (startPage - 6);
}

function clickPageButton(button, keyword) {

    const page = button.textContent;

    const currentUrl = window.location.href;
    const url =currentUrl.split("?");

    location.href=url[0]+"?page="+ (page - 1);
}