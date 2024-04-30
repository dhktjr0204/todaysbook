function submitForm(){
    let keyword=document.querySelector(".search-input").value.trim();

    if(keyword === ""){
        alert("검색어를 입력해주세요");
        return false;
    }else{
        return true;
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