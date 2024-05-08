function clickUpdateRecommendListButton(listId) {
    location.href = '/recommend/update/' + listId;
}

function clickDeleteRecommendListButton(listId, userId) {

    const confirmation = confirm("정말 삭제하시겠습니까?");

    if (confirmation) {
        fetch("/recommend/remove/" + listId + "?userId=" + userId, {
            method: "DELETE",
        }).then(response => {
            if (!response.ok) {
                return response.text().then(msg => {
                    if (response.status === 401) {
                        alert(msg);
                    }else if(response.status===404){
                        alert(msg);
                    }
                });
            } else {
                return response.text();
            }
        }).then(url => {
            if (url) {
                window.location.replace(url);
            }else{
                window.location.replace("/");
            }
        }).catch(error => {
            console.log(error);
        });
    }
}