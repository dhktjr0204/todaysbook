function clickBookMarkBtn(button) {
    const listId = button.closest('.recommend-list-header')
        .querySelector('.recommend-list-title')
        .getAttribute("value");

    const bookMark = button.textContent;

    if (bookMark === "O") {
        const url = `/bookmark/cancel?listId=${listId}`;
        cancelBookMark(url, button);
    } else {
        const url = `/bookmark/add?listId=${listId}`;
        addBookMark(url, button);
    }
}

async function cancelBookMark(url, button) {
    try {
        const response = await fetch(url, {
            method: 'DELETE'
        });

        if (!response.ok) {
            console.log("북마크 해제 실패");
            return;
        }

        button.textContent = 'X';

    } catch (error) {
        console.error(error);
    }
}

async function addBookMark(url, button) {
    try {
        let response = await fetch(url, {
            method: 'POST',
        })

        if (!response.ok) {
            console.log("북마크 등록 실패")
            return;
        }

        button.textContent = 'O';

    } catch (error) {
        console.error(error);
    }
}