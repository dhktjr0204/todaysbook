let draggedItem=null;

const newBookList=[];
const deleteBookList=[];

function dragStart(event){
    draggedItem=event.target;
}

function dragOver(event){
    event.preventDefault();
}

//드롭 영역에 요소가 드롭될 때 실행되는 함수
function drop(event){
    event.preventDefault();

    // 드롭된 요소를 추가할 목록을 식별
    const dropzone = document.querySelector('.user-list');
    if (dropzone) {
        const listNode = draggedItem.closest('.book-item');

        //드래그 된 요소의 value 값
        const draggedItemId=listNode.getAttribute('value');

        //user-list에 이미 있는지 없는 지 확인
        let isDuplicate=isDuplicateFunction(draggedItemId);

        if(isDuplicate){
            alert("해당 책은 이미 등록되어 있습니다.");
        }else{
            const userList=document.querySelector('.user-list');
            const itemLength=userList.querySelectorAll('.book-item').length;
            if(itemLength>=10){
                alert("최대 10개까지 등록 가능합니다.");
                return
            }

            //드래그된 요소의 복제본 생성, 복제본은 드래그 할 수 없다.
            const draggedItemClone = listNode.cloneNode(true);

            // 삭제 버튼 생성
            addDeleteButton(draggedItemClone);

            dropzone.appendChild(draggedItemClone);

            //등록된 책이 6개 이상 되는 순간 2번째 페이지로 넘어감
            if(itemLength+1>5){
                slideNextPage(userList);
            }

            newBookList.push(parseInt(draggedItemId, 10));

            const deleteBookIndex=deleteBookList.indexOf(parseInt(draggedItemId, 10));
            if(deleteBookIndex!==-1){
                deleteBookList.splice(deleteBookIndex,1);
            }
        }

        draggedItem = null;
    }
}

function isDuplicateFunction(draggedItemId){
    const userListItems = document.querySelectorAll('.user-list .book-item');
    return Array.from(userListItems).some(item => {
        const itemId = item.getAttribute('value');
        return itemId === draggedItemId;
    });
}

function addDeleteButton(draggedItemClone){
    // 삭제 버튼 생성
    const deleteButton = document.createElement('button');
    deleteButton.classList.add('delete-button');
    deleteButton.textContent = 'x';
    deleteButton.onclick = function() {
        deleteBook(deleteButton);
    };
    // 드래그된 아이템의 첫 번째 자식으로 삭제 버튼 추가
    const figcaption=draggedItemClone.querySelector('figcaption');
    figcaption.insertBefore(deleteButton,figcaption.firstChild);
}

function slideNextPage(userList){
    const listWidth = document.querySelector('.drag-and-drop-container').offsetWidth;
    const newPosition = -1 * listWidth;
    userList.style.transform = `translateX(${newPosition}px)`;
    searchSlideIndex[1]=1;
}

function deleteBook(button) {
    const bookItem = button.closest('.book-item');

    const confirmation = confirm("정말 삭제하시겠습니까?");
    if (confirmation) {
        bookItem.remove();

        const bookId=parseInt(bookItem.getAttribute('value'),10);
        deleteBookList.push(bookId);


        //만약 삭제한 id가 새로 추가된 리스트에 있을때 id 삭제
        const newBookIndex=newBookList.indexOf(bookId);
        if(newBookIndex!==-1){
            newBookList.splice(newBookIndex,1);
        }

    }
}

// 드롭 영역에 이벤트 리스너 추가
const dropzone = document.querySelector('.drag-and-drop-container');

//드래그하면서 마우스가 대상 객체의 영역 위에 자리 잡고 있을 때 발생
dropzone.addEventListener('dragover', dragOver);
//드래그가 끝나서 드래그하던 객체를 놓는 장소에 위치한 객체에 발생
dropzone.addEventListener('drop', drop);
