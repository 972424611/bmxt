var doMainName = 'http://localhost:8080/bmxt';

$(function () {
    var team = $.cookie('team');
    if(team !== 'CCA') {
        document.getElementById('myform3').style.display = 'none';
    } else if(team === 'CCA') {
        document.getElementById('ggg').style.display = 'none';
    }
});

$(function () {
    $.ajax({
        type : 'post',
        dataType : 'json',
        url : doMainName + '/match/list',
        success : function (data) {
            var result = data.data;
            constructTable(result);
        },
        error : function (jqXHR) {
            console.log(jqXHR);
        }
    })
});

function constructTable(result) {
    for(var i = 0; i < result.length; i++){
        var data = result[i];
        constructTr(data);
    }
}
function constructTr(data) {
    console.log(data.status);
    var matchId = data.id;
    var matchName = data.name;
    var event = data.event;
    var host = data.host;
    var startTime = fmtDate(data.startTime);
    var endTime = fmtDate(data.endTime);
    var status = data.status;
    if(status === 1){
        $('#table-body').append(' <tr>\n' +
            '                                    <td>' + matchId + '</td>\n'+
            '                                    <td>' + matchName + '</td>\n' +
            '                                    <td>' + event + '</td>\n' +
            '                                    <td>' + host + '</td>\n' +
            '                                    <td>' + startTime + '</td>\n' +
            '                                    <td>' + endTime + '</td>\n' +
            '                                    <td>\n' +
            '                                        <div>\n' +
            '                                            <i class="icon-pass">&#xe62f;</i>\n' +
            '                                            <a href="./signUpItem.html" onclick="setCookie($(this))">编辑</a>\n' +
            '                                        </div>\n' +
            '                                    </td>\n' +
            '                                    <td>' + '更改状态' + '</td>\n' +
        '                                </tr>');
    } else {
        $('#table-body').append(' <tr>\n' +
            '                                    <td>' + matchId + '</td>\n'+
            '                                    <td>' + matchName + '</td>\n' +
            '                                    <td>' + event + '</td>\n' +
            '                                    <td>' + host + '</td>\n' +
            '                                    <td>' + startTime + '</td>\n' +
            '                                    <td>' + endTime + '</td>\n' +
            '                                    <td>\n' +
            '                                        <div>\n' +
            '                                            <i class="icon-notPass">&#xe62f;</i>\n' +
            '                                        </div>\n' +
            '                                    </td>\n' +
            '                                </tr>');
    }
}

function fmtDate(obj){
    var date =  new Date(obj);
    var y = 1900+date.getYear();
    var m = "0"+(date.getMonth()+1);
    var d = "0"+date.getDate();
    return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
}

function setCookie(obj) {
    var oId = obj.parent().parent().parent().children().eq(0).html();
    var oMatchName = obj.parent().parent().parent().children().eq(1).html();
    var Obj = {
        oId : oId,
        oMatchName : oMatchName

    };
    $.cookie('Obj',JSON.stringify(Obj));
}

$(function(){
    /**添加运动员信息部分, 图片上传部分开始*/
    $('#file-upload3').on('change',function(){
        var fileName = this.files[0].name;
        var format = fileName.substring(fileName.indexOf('.'));
        if(format === ".xlsx" || format === ".xls") {
            document.getElementById("myform3").submit();
            alert("上传成功");
            window.location.reload();
        } else {
            alert("请选择.xlsx或者.xls的文件上传");
        }
    });

});