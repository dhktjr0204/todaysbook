function clickFavorite(button) {

    const image = button.querySelector('img');

    image.classList.toggle('favorite');

    let url = image.classList.contains('favorite') ? '/favorite_book/add' : '/favorite_book/delete';
    let bookId = 1;

    let type = image.classList.contains('favorite') ? 'GET' : 'DELETE';
    let message = image.classList.contains('favorite') ?
        '찜하기 목록에 추가 되었습니다.' : '찜하기 목록에서 제거 되었습니다.';

    $.ajax({

        type: type,
        url: url,
        data: {
            bookId: bookId
        },
        success: function (response) {

            alert(message);
        },
        error: function (error) {

            console.error(error);
        }
    });
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

    submitHandler("/review/add", "POST");
}

function submitHandler(url, method) {

    const content = document.querySelector('.review-input').value;
    const bookId = document.querySelector('.book-id').value;
    const score = parseInt(document.querySelector('.review-score').textContent);

    if(content.trim() === "") {
        alert("리뷰 내용을 입력해주세요.");
        return false;
    }

    fetch(url, {

        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            content: content,
            bookId: bookId,
            score: score
        })
    }).then(response => {

        if(response.ok) {
            return response.text();
        } else {
            throw new Error('리뷰 등록 실패');
        }
    }).then(data => {
        alert("리뷰가 등록되었습니다.");
    }).catch(error => {
        console.error(error);
    });
}

document.addEventListener('DOMContentLoaded', function() {

    const stars = document.querySelectorAll('.review-star');
    const reviewScore = document.querySelector('.review-score');
    let isClicked = false;
    let previousValue = 0;

    stars.forEach(star => {
        star.addEventListener('mousedown', function() {
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

        star.addEventListener('mouseover', function() {
            if (isClicked) {
                const value = this.getAttribute('data-value');
                reviewScore.textContent = value;
                highlightStars(value);
            }
        });

        star.addEventListener('mouseup', function() {
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