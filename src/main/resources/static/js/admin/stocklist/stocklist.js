function clickEditStockButton(button) {
    const confirmation = confirm("해당 책의 수량을 바꾸시겠습니까?");

    if (confirmation) {
        const stock = button.closest('.book-item').querySelector('.quantity').value;
        const bookId= button.closest('.book-item').querySelector('.book-id').value;

        fetch("/admin/stocklist?bookId=" + bookId + "&stock=" + stock, {
            method: "PUT",
        }).then(response => {
            if (!response.ok) {
                console.log("실패");
            } else {
                return response.text();
            }
        }).then(msg => {
            alert(msg);
            location.reload();
        }).catch(error => {
            console.log(error);
        });
    }
}

function clickDeleteBookButton(button){
    const confirmation = confirm("해당 책을 정말 삭제겠습니까?");

    if (confirmation) {
        const bookId= button.closest('.book-item').querySelector('.book-id').value;

        fetch("/admin/stocklist?bookId=" + bookId, {
            method: "DELETE",
        }).then(response => {
            if (!response.ok) {
                console.log("실패");
            } else {
                return response.text();
            }
        }).then(msg => {
            alert(msg);
            location.reload();
        }).catch(error => {
            console.log(error);
        });
    }
}