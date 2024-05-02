function clickBookMarkBtn(button) {
    const listId = button.value;

    if (button.classList.contains("redheart")) {
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

        button.classList.remove("redheart");
        button.classList.add('emptyheart');

        alert("북마크 해제되었습니다.");

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

        button.classList.remove('emptyheart');
        button.classList.add("redheart");

        alert("북마크에 등록되었습니다.");

    } catch (error) {
        console.error(error);
    }
}