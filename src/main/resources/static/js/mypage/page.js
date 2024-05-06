function clickNextPageButton(endPage, keyword) {

    location.href="/mypage/favoritebook?page=" + endPage;
}

function clickPrevPageButton(startPage, keyword) {

    location.href="/mypage/favoritebook?page=" + (startPage - 6);
}

function clickPageButton(button, keyword) {

    const page = button.textContent;

    location.href="/mypage/favoritebook?page=" + (page - 1);
}