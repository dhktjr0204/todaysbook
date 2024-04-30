function clickCreateButton(){
    handleSubmit("/recommend/add", "POST");
}

function clickUpdateButton(){
    const listId= document.querySelector('.list-id').value;
    handleSubmit("/recommend/update/"+listId, "PUT");
}

function clickCancelButton(){
    const listId= document.querySelector('.user-id').value;
    location.href="/mypage/my_recommend_list/"+listId;
}

function handleSubmit(url, method){
    const formData=new FormData(document.querySelector('.list-form'));

    const title=document.querySelector('.user-list-title-input');
    if(title.value.trim()===""){
        alert("리스트 제목을 입력해주세요.");
        return false;
    }

    const bookItems=document.querySelector('.user-list').querySelectorAll('.book-item');
    if(bookItems.length===0){
        alert("책을 1개 이상 등록해주세요.");
        return false;
    }

    bookItems.forEach(bookItem=>{
        formData.append('bookIdList', bookItem.getAttribute('value'));
    });

    fetch(url,{
        method:method,
        body:formData,
    }).then(response=>{
        if(!response.ok){
            console.log("실패");
        }else{
            return response.text();
        }
    }).then(url=>{
        window.location.replace(url);
    }).catch(error=>{
        console.log(error);
    });
}