function submitForm(){
    let keyword=document.querySelector(".search-input").value.trim();

    if(keyword === ""){
        alert("검색어를 입력해주세요");
        return false;
    }else{
        return true;
    }
}