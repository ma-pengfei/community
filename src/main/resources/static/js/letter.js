$(function () {
    $("#sendBtn").click(send_letter);
    $(".close").click(delete_msg);
});

function send_letter() {
    $("#sendModal").modal("hide");

    // 获取csrf令牌，在发送AJAX请求之前，将令牌设置到请求的消息头中
    var token = $("meta[name = '_csrf']").attr("content");
    var header = $("meta[name = '_csrf_header']").attr("content");

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    $.post(
        CONTEXT_PATH + "/letter/send",
        {
            "toName": $("#recipient-name").val(),
            "content": $("#message-text").val()
        },
        function (data) {
            if (data.code === 0) {
                $("#hintBody").text("发送成功！")
            } else {
                $("#hintBody").text(data.msg)
            }
            $("#hintModal").modal("show");
            setTimeout(function () {
                $("#hintModal").modal("hide");
                location.reload();
            }, 2000);
        }

    )
}

function delete_msg() {
    // TODO 删除数据
    $(this).parents(".media").remove();
}