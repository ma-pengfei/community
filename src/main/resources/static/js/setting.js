$(function () {

    // 表单提交触发该函数
    $("#uploadForm").submit(upload);
})

function upload() {

    // 获取csrf令牌，在发送AJAX请求之前，将令牌设置到请求的消息头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.ajax({
        url: "http://upload-z1.qiniup.com",
        method: "post",
        processData: false,
        contentType: false,
        data: new FormData($("#uploadForm")[0]),
        success: function (data) {
            if (data && data.code === 0) {
                // 更新头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName": $("input[name = 'key']").val()},
                    function (data) {
                        data = $.parseJSON(data);
                        if (data.code === 0) {
                            window.location.reload();
                        } else {
                            alert(data.msg);
                        }
                    }
                )
            } else {
                alert("上传失败！")
            }
        }
    })


    // 终止表单的提交
    return false;
}