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
                console.log("실패");
            } else {
                return response.text();
            }
        }).then(url => {
            window.location.replace(url);
        }).catch(error => {
            console.log(error);
        });
    }
}