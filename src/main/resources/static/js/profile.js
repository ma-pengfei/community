$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
    var entityId = $("#entityId").val();
	if($(btn).hasClass("btn-info")) {
		// 关注TA
        $.post(
            CONTEXT_PATH + "/follow",
            {"entityId":entityId,"entityType":3},
            function (data) {
                data = $.parseJSON(data);
                if (data.code === 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        )
		$(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
	} else {
		// 取消关注
        $.post(
            CONTEXT_PATH + "/unfollow",
            {"entityId":entityId,"entityType":3},
            function (data) {
                data = $.parseJSON(data);
                if (data.code === 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        )
		$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
	}
}