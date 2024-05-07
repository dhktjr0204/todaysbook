function clickRegistrationButton() {
    const checkBoxes = document.querySelectorAll(".check-box");

    const books = [];

    checkBoxes.forEach(checkbox => {
        if (checkbox.checked) {
            const bookItem = checkbox.closest(".book-item");
            const title = bookItem.querySelector(".book-name").textContent;
            const author = bookItem.querySelector(".author").textContent;
            const price = bookItem.querySelector(".price").getAttribute("value");
            const bookImage = bookItem.querySelector(".book-image").value;
            const publisher = bookItem.querySelector(".publish").textContent;
            const publishDate = bookItem.querySelector(".book-publish-date").value;
            const stock = bookItem.querySelector(".stock").value;
            const isbn = bookItem.querySelector(".isbn").textContent;
            const description = bookItem.querySelector(".description").value;
            const category = bookItem.querySelector('.category').value;

            books.push({
                image: bookImage,
                stock: stock,
                publishDate: publishDate,
                title: title,
                isbn: isbn,
                price: price,
                author: author,
                publisher: publisher,
                description: description,
                category: category
            });
        }
    });

    if (books.length > 0) {
        submitForm(books);
    } else {
        alert("선택된 도서가 없습니다.")
    }

}

function submitForm(books) {
    let confirmation = confirm(`해당 도서를 등록하시겠습니까?\n\n ${createConfirmMessage(books)}`);

    if (confirmation) {
        fetch("/admin/book_registration", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(books)
        }).then(response => {
            if (!response.ok) {
                return response.text().then(msg => {
                    if (response.status === 401) {
                        alert(msg);
                    }else if(response.status===400){
                        alert(msg);
                        throw new Error(msg);
                    } else if (response.status === 404) {
                        alert(msg);
                    }
                });
            }
            return response.text();
        }).then(msg => {
            if (msg) {
                alert(msg);
            }
            window.location.reload();
        })
    }
}

function createConfirmMessage(books) {
    const bookInfo = books.map((book, index) =>
        `${index + 1}. ${book.title} - ${book.author} - ${book.publisher}`
    ).join("\n");
    return bookInfo;
}