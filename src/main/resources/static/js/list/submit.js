function clickCreateButton() {
    handleSubmit("/recommend/add", "POST");
}

function clickUpdateButton() {
    const listId = document.querySelector('.list-id').value;
    handleSubmit("/recommend/update/" + listId, "PUT");
}

function clickCancelButton() {
    const listId = document.querySelector('.user-id').value;
    location.href = "/mypage/my_recommend_list";
}

function handleSubmit(url, method) {
    const formData = new FormData(document.querySelector('.list-form'));

    const title = document.querySelector('.user-list-title-input');
    if (title.value.trim() === "") {
        alert("리스트 제목을 입력해주세요.");
        return false;
    }

    const bookItems = document.querySelector('.user-list').querySelectorAll('.book-item');
    if (bookItems.length === 0) {
        alert("책을 1개 이상 등록해주세요.");
        return false;
    }
    if (url === "/recommend/add") {
        bookItems.forEach(bookItem => {
            formData.append('bookIdList', bookItem.getAttribute('value'));
        });
    }else{
        formData.append('newBookList', newBookList);
        formData.append('deleteBookList', deleteBookList);
    }

    fetch(url, {
        method: method,
        body: formData,
    }).then(response => {
        if (!response.ok) {
            return response.text().then(msg => {
                if (response.status === 401) {
                    alert(msg);
                } else if (response.status === 400) {
                    alert(msg);
                    throw new Error(msg);
                } else if (response.status === 404) {
                    alert(msg);
                }
            });
        } else {
            return response.text();
        }
    }).then(url => {
        if (url) {
            window.location.replace(url);
        } else {
            window.location.replace("/");
        }
    }).catch(error => {
        console.log(error);
    });
}