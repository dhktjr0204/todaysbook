//객체를 사용하여 각 리스트의 슬라이드 인덱스 관리
const listSlideIndex={};

function prevSlide(listId, button) {
    let currentIndex = listSlideIndex[listId] || 0; // 해당 게시물의 슬라이드 인덱스를 가져오거나, 없으면 0으로 초기화
    const page = Math.floor(button.closest('.list-container').querySelectorAll('.book-item').length/5)+1; //리스트에 보여줄 페이지 개수
    // 음수가 되지 않게 page를 더해주었다.
    currentIndex = (currentIndex - 1 + page) % page;
    moveSlide(listId, currentIndex, button);
}

function nextSlide(listId, button) {
    let currentIndex = listSlideIndex[listId] || 0; // 해당 게시물의 슬라이드 인덱스를 가져오거나, 없으면 0으로 초기화
    const page = Math.floor(button.closest('.list-container').querySelectorAll('.book-item').length/5)+1;//리스트에 보여줄 페이지 개수
    // 현재 위치에서 1칸 이동, 만약 뒤에 더 이미지가 없다면 처음으로 이동
    currentIndex = (currentIndex + 1) % page;
    moveSlide(listId, currentIndex, button);
}

function moveSlide(boardId, newIndex, button) {
    const recommendList = button.closest('.list-container').querySelector('.recommend-list');
    const listWidth = button.closest('.list-container').querySelector('.slider-container').offsetWidth;
    const newPosition = -newIndex * listWidth;
    recommendList.style.transform = `translateX(${newPosition}px)`;
    listSlideIndex[boardId] = newIndex; // 해당 게시물의 슬라이드 인덱스 업데이트
}