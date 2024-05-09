function clickSyncButton() {

    let url = '/admin/sync';
    let type = 'GET';

    $.ajax({

       type: type,
       url: url,
       success: function (response) {

           alert("동기화 완료");
       },
        error: function (error) {

           alert("동기화 실패");
        }
    });
}