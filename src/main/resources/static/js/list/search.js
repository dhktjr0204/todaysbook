let currentKeyword;
let currentLastPage = 1;
let searchSlideIndex = []

function listSubmitForm(event) {
    event.preventDefault();
    const keyword = document.querySelector(".search-input").value.trim();

    if (keyword === "") {
        alert("검색어를 입력해주세요");
        return false;
    } else {
        currentKeyword = keyword;
        searchBook(keyword, 0);
    }
}

async function searchBook(keyword, page) {
    try {
        let response = await fetch("/book/search/list?page=" + page + "&keyword=" + keyword, {
            method: 'GET'
        })

        if (!response.ok) {
            alert("실패");
            return;
        }

        const bookList = await response.json();

        const bookItems = createBookItem(bookList.content);

        //전체 페이지 저장
        currentLastPage = bookList.totalPages;

        const searchList = document.querySelector('.search-list');
        
        //첫 요청일 경우 초기화
        if (page === 0) {
            searchList.innerHTML = '';
            searchList.style.transform = `translateX(0px)`;
            searchSlideIndex[0] = 0;
        }

        bookItems.forEach(bookItem => {
            searchList.appendChild(bookItem);
        });

    } catch (error) {
        console.log(error);
    }
}

function createBookItem(bookList) {
    const bookItemList = [];

    bookList.forEach(book => {
        const li = document.createElement('li');
        li.classList.add('book-item');

        //드래그 가능하게
        li.draggable=true;
        li.addEventListener('dragstart', dragStart);

        const bookImage = document.createElement('img');
        bookImage.src = book.image;
        bookImage.alt = book.title;
        bookImage.setAttribute('value', book.id);

        const bookTitle=document.createElement('p');
        bookTitle.textContent=book.title;

        li.appendChild(bookImage);
        li.appendChild(bookTitle);

        bookItemList.push(li);

    });

    return bookItemList;
}

function listFormPrevSlide(type, button) {
    let currentIndex = searchSlideIndex[type] || 0; // search slide와 user-list slide 따로 관리

    const bookItemLength = button.closest('.list-container').querySelectorAll('.book-item').length
    let page = 1;
    if (bookItemLength !== 0) {
        page = Math.ceil(bookItemLength / 5);//리스트에 보여줄 페이지 개수
    }

    // 음수가 되지 않게 page를 더해주었다.
    currentIndex = (currentIndex - 1 + page) % page;
    listFormMoveSlide(type, currentIndex, button);
}

function listFormNextSlide(type, button) {
    let currentIndex = searchSlideIndex[type] || 0; // 해당 게시물의 슬라이드 인덱스를 가져오거나, 없으면 0으로 초기화

    const bookItemLength = button.closest('.list-container').querySelectorAll('.book-item').length
    let page = 1;
    if (bookItemLength !== 0) {
        page = Math.ceil(bookItemLength / 5);//리스트에 보여줄 페이지 개수
    }

    //현재 누른 페이지 버튼이 search쪽이라면 동작
    if(button.closest('.list-container').querySelector('.search-list')){
        // 현재 위치에서 1칸 이동, 만약 뒤에 더 이미지가 없다면 처음으로 이동
        if ((currentIndex + 1) % page === 0) {
            if (currentLastPage <= page) {
                alert("마지막 페이지입니다.");
            } else {
                searchBook(currentKeyword, page);
                listFormMoveSlide(type, page, button);
            }
        } else {
            currentIndex = (currentIndex + 1) % page;
            listFormMoveSlide(type, currentIndex, button);
        }
    }else{//현재 누른 페이지 버튼이 user쪽이라면 옆으로 그냥 넘기기
        currentIndex = (currentIndex + 1) % page;
        listFormMoveSlide(type, currentIndex, button);
    }
}

function listFormMoveSlide(type, newIndex, button) {
    //현재 버튼을 누른 container찾기
    const currentList = button.closest('.list-container');
    let targetList;

    //만약 누른 버튼의 container가 search쪽이라면
    if(currentList.querySelector('.search-list')){
        targetList=currentList.querySelector('.search-list');
    }else{//누른 버튼의 container가 user-list쪽이라면
        targetList=currentList.querySelector('.user-list');
    }

    //width는 search쪽과 user-list쪽 둘다 같기 때문에 같은 요소 사용
    const listWidth = document.querySelector('.slider-container').offsetWidth;
    const newPosition = -newIndex * listWidth;
    targetList.style.transform = `translateX(${newPosition}px)`;
    searchSlideIndex[type] = newIndex; // 해당 게시물의 슬라이드 인덱스 업데이트
}