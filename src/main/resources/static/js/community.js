function postComment() {
    var id = $("#question_id").val();
    var content = $("#comment-content").val();
    var type = 1;
    $.ajax({
        type: "POST",
        url: "/community/comment/add.do",
        contentType: "application/json",
        data: JSON.stringify({
            "questionId":id,
            "parentId": id,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                $("#section_comment").hide();
            } else {
                if (response.code == 1000) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=ef32a1e0f6f10780b9d3&redirect_uri=http://localhost:8887/community/callback&scope=user&state=1");
                        window.localStorage.setItem("closeable", true);
                    }
                } else {
                    alert(response.message);
                }

            }
        },
        dataType: "json"
    });
}

function showAnswers(e) {
    // 获取元素数据
    var commentId = e.getAttribute("data");
    var answerdiv = $("#template");
    var well = $("#well-" + commentId);
    $.ajax({
        type: "POST",
        url: "/community/comment/queryAnswers.do",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": commentId,
            "type": 2
        }),
        success: function (response) {
            well.empty();
            if (response.code == 200) {
                // 进行数据的拼接 首先模块信息得展示 先清空
                $.each(response.data, function (i, data) {
                        var ad = answerdiv.clone();
                        ad.find("#answer-avatarUrl").attr("src", data.user.avatarUrl);
                        ad.find("#answer-user-name").html(data.user.name);
                        ad.find("#answer-create").html(moment(data.gmtCreate).format('YYYY-MM-DD HH:mm:ss'));
                        ad.find("#answer-content").html(data.content);
                        ad.attr("id", "ready-" + i);//改变绑定好数据的行的id
                        ad.css('display', 'block');
                        ad.appendTo(well);
                    }
                );
            } else {
                well.append($("<span/>", {
                    "class": "fail-tip",
                    "html": "暂无数据"
                }));
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var id = $("#question_id").val();
    var commentId = e.getAttribute("data");
    var content = $("#input-" + commentId).val();
    var type = 2;
    $.ajax({
        type: "POST",
        url: "/community/comment/add.do",
        contentType: "application/json",
        data: JSON.stringify({
            "questionId":id,
            "parentId": commentId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                showAnswers(e);
                $("#input-" + commentId).val("");
            } else {
                if (response.code == 1000) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=ef32a1e0f6f10780b9d3&redirect_uri=http://localhost:8887/community/callback&scope=user&state=1");
                        window.localStorage.setItem("closeable", true);
                    }
                } else {
                    alert(response.message);
                }

            }
        },
        dataType: "json"
    });
}

function cancel(e) {
    var commentId = e.getAttribute("data");
    $("#" + commentId).removeClass("in");
}

function selectTag() {
    $("#publish-tag-content").css("display", "block");

}

function bindTag(e) {
    // 首先获取变迁名字
    var tagName = e.getAttribute("data-name");

    // 获取输入框值
    var inputContent = $("#tag").val();
    // 最多添加5个标签
    var strs = inputContent.split("，");
    if(strs.length >=5){
        alert("最多可选5个标签");
        return;
    }
    if (inputContent.indexOf(tagName) == -1) {
        if (inputContent) {
            $("#tag").val(inputContent + "，" + tagName);
        }else {
            $("#tag").val(tagName);

        }
    }

}