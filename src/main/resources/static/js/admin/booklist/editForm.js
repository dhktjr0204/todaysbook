function clickEditBookButton() {
    const confirmation = confirm("해당 내용으로 바꾸시겠습니까?");

    if (confirmation) {

        const formData=new FormData(document.querySelector('.book-form'))

        fetch("/admin/booklist/edit", {
            method: "PUT",
            body: formData,
        }).then(response => {
            if (!response.ok) {
                console.log("실패");
            } else {
                return response.text();
            }
        }).then(msg => {
            window.location.replace(document.referrer);
        }).catch(error => {
            console.log(error);
        });
    }
}

function clickCancelButton(){
    window.location.replace(document.referrer);
}