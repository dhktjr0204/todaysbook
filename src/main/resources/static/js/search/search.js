function submitForm(e){
    e.preventDefault();

    let keyword=document.querySelector(".search-input").value.trim();

    if(keyword === ""){
        alert("검색어를 입력해주세요");
    }else{
        const type=document.querySelector('.select-box-button').querySelector('p').textContent;
        if(type==="리스트"){
            location.href="/book/search/list?keyword="+keyword;
        }else{
            location.href="/book/search?keyword="+keyword;
        }
    }
}

const selectButton = document.querySelector('.select-box-button');
const optionList = document.querySelector('.select-box-option');
const selectedText = document.querySelector('.select-box-button p');

selectButton.addEventListener('click', function() {
    if (optionList.style.display === 'none' || optionList.style.display === '') {
        optionList.style.display = 'block';
    } else {
        optionList.style.display = 'none';
    }
});

const optionItems = document.querySelectorAll('.select-box-option button');
optionItems.forEach(function(item) {
    item.addEventListener('click', function() {
        const selectedItemText = item.textContent;
        selectedText.textContent = selectedItemText;
        optionList.style.display = 'none';
    });
});



function addToCart(button) {
    // 클릭된 버튼에서 bookId 값 가져오기
    const bookId = button.dataset.bookId;

    // bookCount 값을 가져오기
    const bookCount = button.parentElement.querySelector('.book-count').value;


    console.log('장바구니에 담을 도서의 ID:', bookId);
    console.log('도서 개수:', bookCount);
    // AJAX를 사용하여 서버로 장바구니 추가 요청 전송
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/cart/add', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // 장바구니 추가 성공 시 알림창 띄우기
                alert('장바구니에 상품이 추가되었습니다.');
            } else {
                // 에러 발생 시 알림창 띄우기
                alert('장바구니 추가에 실패했습니다. 다시 시도해주세요.');
            }
        }
    };
    xhr.send(JSON.stringify({ bookId: bookId ,bookCount: bookCount}));
}

