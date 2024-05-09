function clickEditBookButton() {
    const confirmation = confirm("해당 내용으로 바꾸시겠습니까?");

    if (confirmation) {

        const formData = new FormData(document.querySelector('.book-form'))

        fetch("/admin/booklist/edit", {
            method: "PUT",
            body: formData,
        }).then(response => {
            if (!response.ok) {
                return response.text().then(msg => {
                    if (response.status === 401) {
                        alert(msg);
                    }else if (response.status === 400) {
                        alert(msg);
                        throw new Error(msg);
                    }
                    else if (response.status === 404) {
                        alert(msg);
                    }
                });
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

function clickCancelButton() {
    window.location.replace(document.referrer);
}

function titleWordCount(){
    const titleTextArea=document.querySelector(".title");
    const titleTextCount = document.querySelector('.title-text-count');

    countBytes(titleTextArea, titleTextCount, 200);
}

function authorWordCount(){
    const authorTextArea=document.querySelector(".author");
    const authorTextCount = document.querySelector('.author-text-count');

    countBytes(authorTextArea, authorTextCount, 200);
}

function publisherWordCount(){
    const publisherTextArea=document.querySelector(".publisher");
    const publisherTextCount = document.querySelector('.publisher-text-count');

    countBytes(publisherTextArea, publisherTextCount, 20);
}

function descriptionWordCount(){
    const descriptionTextArea=document.querySelector(".description");
    const descriptionTextCount = document.querySelector('.description-text-count');

    countBytes(descriptionTextArea, descriptionTextCount, 300);
}

document.addEventListener('DOMContentLoaded', function () {
    titleWordCount();
    authorWordCount()
    publisherWordCount();
    descriptionWordCount();
});