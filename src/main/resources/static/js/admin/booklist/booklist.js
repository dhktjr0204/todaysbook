function clickEditButton(button){
    const bookId= button.closest('.book-item').querySelector('.book-id').value;
    location.href="/admin/booklist/edit?bookId="+bookId;
}