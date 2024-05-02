function commentWordCount(textarea){

    const commentForm = textarea.closest('.review-form');
    const commentTextCount = commentForm.querySelector('.comment-text-count');
    countBytes(textarea, commentTextCount, 500);
}

function countBytes(editor, containerSelector, limit) {

    const content = editor.value;
    const byteCount = countUtf8Bytes(content);

    containerSelector.textContent = byteCount + "/" + limit;

    if (byteCount > limit) {
        alert('허용된 글자수가 초과되었습니다.')
        const truncatedContent = cutByLen(content, limit);
        editor.value = truncatedContent;
    }
}

function countUtf8Bytes(str) {
    let byteCount = 0;

    for (let i = 0; i < str.length; i++) {
        const charCode = str.charCodeAt(i);
        (charCode==10) ? byteCount+=2: byteCount+=1;
    }

    return byteCount;
}

function cutByLen(str, maxByte) {
    for (let b = i = 0; c = str.charCodeAt(i); i++) {

        b += c == 10 ? 2 : 1;
        if (b > maxByte) break;
    }

    return str.substring(0, i);
}

function clickLike(button, reviewId) {

    button.classList.toggle('liked');
    button.parentElement.classList.toggle('on');

    let url = button.classList.contains('liked') ? '/review/add_like' : '/review/delete_like';
    let userId = 1;

    let type = button.classList.contains('liked') ? 'GET' : 'DELETE';
    let message = button.classList.contains('liked') ?
        '추천 완료' : '추천 취소 완료';


    $.ajax({

        type: type,
        url: url,
        data: {
            reviewId: reviewId,
            userId: userId
        },
        success: function (response) {

            alert(message);

            let newLikeCount = response;
            let likeCountElement = button.parentElement.querySelector('.like-count');
            likeCountElement.innerHTML = newLikeCount;
        },
        error: function (error) {

            console.error(error);
        }
    });
}

function clickDislike(button, reviewId) {

    button.classList.toggle('disliked');
    button.parentElement.classList.toggle('on');

    let url = button.classList.contains('disliked') ? '/review/add_dislike' : '/review/delete_dislike';
    let userId = 1;

    let type = button.classList.contains('disliked') ? 'GET' : 'DELETE';
    let message = button.classList.contains('disliked') ?
        '비추천 완료' : '비추천 취소 완료';

    $.ajax({

        type: type,
        url: url,
        data: {
            reviewId: reviewId,
            userId: userId
        },
        success: function (response) {

            alert(message);

            let newDislikeCount = response;
            let dislikeCountElement = button.parentElement.querySelector('.dislike-count');
            dislikeCountElement.innerHTML = newDislikeCount;
        },
        error: function (error) {

            console.error(error);
        }
    });
}

function clickAddReview() {

    const url = "/review/add";
    const method = "POST";

    const content = document.querySelector('.review-input').value;
    const bookId = document.querySelector('.book-id').value;
    const score = parseInt(document.querySelector('.review-score').textContent);

    if (content.trim() === "") {
        alert("리뷰 내용을 입력해주세요.");
        return false;
    }

    $.ajax({
        url: url,
        type: method,
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify({
            content: content,
            bookId: bookId,
            score: score
        }),
        success: function (data) {
            alert("리뷰가 등록 되었습니다.");
            $('.review-div').html(data);
        },
        error: function (error) {
            throw new Error('리뷰 등록 실패');
        }
    });
}

function deleteReview() {

    const url = "/review/delete";
    const method = "DELETE";

    const bookId = document.querySelector('.book-id').value;
    const reviewId = document.querySelector('.review-id').value;

    $.ajax({
        url: url,
        type: method,
        data: {
            bookId: bookId,
            reviewId: reviewId
        },
        success: function (data) {
            alert("리뷰가 삭제 되었습니다.");
            $('.review-div').html(data);
        },
        error: function (error) {
            throw new Error('리뷰 삭제 실패');
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {

    const stars = document.querySelectorAll('.review-star');
    const reviewScore = document.querySelector('.review-score');
    let isClicked = false;
    let previousValue = 0;

    stars.forEach(star => {
        star.addEventListener('mousedown', function () {
            isClicked = true;
            const value = this.getAttribute('data-value');
            if (value === previousValue) {

                reviewScore.textContent = '0';
                highlightStars(0);
                previousValue = 0;
            } else {
                reviewScore.textContent = value;
                highlightStars(value);
                previousValue = value;
            }
        });

        star.addEventListener('mouseover', function () {
            if (isClicked) {
                const value = this.getAttribute('data-value');
                reviewScore.textContent = value;
                highlightStars(value);
            }
        });

        star.addEventListener('mouseup', function () {
            isClicked = false;
        });
    });

    function highlightStars(value) {
        stars.forEach(star => {
            if (parseInt(star.getAttribute('data-value')) <= value) {
                star.src = '/images/star.png';
            } else {
                star.src = '/images/blank_star.png';
            }
        });
    }
});