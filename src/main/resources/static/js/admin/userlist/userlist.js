function clickEditRoleButton(button) {
    const confirmation = confirm("해당 유저의 등급을 바꾸시겠습니까?");

    if (confirmation) {
        const role = button.closest('.user').querySelector('.user-grade').value;
        const userId= button.closest('.user').querySelector('.user-id').value;

        fetch("/admin/userlist?userId=" + userId + "&role=" + role, {
            method: "PUT",
        }).then(response => {
            if (!response.ok) {
                return response.text().then(msg => {
                    if (response.status === 401) {
                        alert(msg);
                    } else if (response.status === 404) {
                        alert(msg);
                    }
                });
            } else {
                return response.text();
            }
        }).then(msg => {
            if(msg){
                alert(msg);
            }
            location.reload();
        }).catch(error => {
            console.log(error);
        });
    }
}

function clickDeleteUserButton(button){
    const confirmation = confirm("해당 유저를 정말 탈퇴시키겠습니까?");

    if (confirmation) {
        const userId= button.closest('.user').querySelector('.user-id').value;

        fetch("/admin/userlist?userId=" + userId, {
            method: "DELETE",
        }).then(response => {
            if (!response.ok) {
                console.log("실패");
            } else {
                return response.text();
            }
        }).then(msg => {
            alert(msg);
            location.reload();
        }).catch(error => {
            console.log(error);
        });
    }
}