async function clickDeleteBookMarkButton(listId){
    const confirmation = confirm("정말 삭제하시겠습니까?");

    if(confirmation) {
        try {
            const response = await fetch(`/bookmark/cancel?listId=${listId}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                console.log("북마크 해제 실패");
                return;
            }

            location.reload();

        } catch (error) {
            console.error(error);
        }
    }
}