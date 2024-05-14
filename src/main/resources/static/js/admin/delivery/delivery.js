function clickEditStatusButton(button) {
    const confirmation = confirm("배송 상태를 바꾸시겠습니까?");

    if (confirmation) {
        const status = button.closest('.delivery').querySelector('.delivery-status').value;
        const deliveryId= button.closest('.delivery').querySelector('.delivery-id a').textContent;

        fetch("/admin/delivery?deliveryId=" + deliveryId + "&status=" + status, {
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