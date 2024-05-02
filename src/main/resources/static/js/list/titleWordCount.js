const limit=30;
const titleInput = document.querySelector('.user-list-title-input');

titleInput.addEventListener('keydown', function (e) {
    if (e.key === 'Enter') {
        e.preventDefault();
    }
});

function countTitleLength(input, containerSelector, limit) {
    const content = input.value;
    const titleLength = content.length;

    containerSelector.textContent = titleLength + "/" + limit;

    if (titleLength > limit) {
        alert('허용된 글자수가 초과되었습니다.')
        const truncatedContent = content.substring(0, limit);
        input.value = truncatedContent;
    }
}

function wordCount(textarea){
    const textCount=document.querySelector('.text-count');
    countTitleLength(textarea, textCount, limit);
}

//새로고침되면서 글자수 세기 작동(수정폼일 경우 필요)
document.addEventListener('DOMContentLoaded', function () {
    const textCount = document.querySelector('.text-count');
    countTitleLength(titleInput, textCount, limit);
});
