var doMainName = $.cookie('doMainName');
var $table_body = $('#table-body');
var $status = $('#status');
var index = [];

var team;
var canEditHtml = '<div>' +
    '                                <i class="icon-pass">&#xe62f;</i>' +
    '                                <a href="./signUpItem.html" onclick="setCookie($(this))">编辑</a>' +
    '                            </div>';
var cannotEditHtml = '<div>' +
    '                                 <i class="icon-notPass">&#xe62f;</i>' +
    '                                <a href="./signUpItem.html" onclick="setCookie2($(this))">查看</a>' +
    '                                 </div>';
$(function () {
    $('#logout').attr("href", doMainName + "/user/logout");
    $('#team_name').html('<span class="glyphicon glyphicon-user" aria-hidden="true"></span>\n' + $.cookie('team'));
    team = $.cookie('team');
    if(team !== 'CCA' && team !== 'admin') {
        document.getElementById('myform3').style.display = 'none';
        $('#change').attr('hidden', 'hidden');
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
            alert(jqXHR.data);
        }
    })
});

function constructTable(result) {
    for(var i = 0; i < result.length; i++){
        index[i] = result[i].id;
        result[i].id = i + 1;
        var data = result[i];
        constructTr(data,i);
    }
}
function constructTr(data,index) {
    var matchId = data.id;
    var matchName = data.name;
    var event = data.event;
    var host = data.host;
    var startTime = fmtDate(data.startTime);
    var endTime = fmtDate(data.endTime);
    var status = data.status;
    var tdCommon = ' <tr>\n' +
        '                                    <td>' + matchId + '</td>\n'+
        '                                    <td>' + matchName + '</td>\n' +
        '                                    <td>' + event + '</td>\n' +
        '                                    <td>' + host + '</td>\n' +
        '                                    <td>' + startTime + '</td>\n' +
        '                                    <td>' + endTime + '</td>\n' +
        '                                    <td class="registerStatus">\n' +
        '                                    </td>\n' +
        '                                </tr>';
    if(status === 1){
        $table_body.append(tdCommon);
        $table_body.find('tr').eq(index).children().eq(6).html(canEditHtml);
    } else {
        $table_body.append(tdCommon);
        $table_body.find('tr').eq(index).children().eq(6).html(cannotEditHtml);
    }
    if(team === 'CCA' || team === 'admin'){
        $table_body.find('tr').eq(index).children().eq(6).after('<td>\n' +
            '<select onchange="changeStatus($(this))">\n' +
            '    <option value="0">关闭</option>\n' +
            '    <option value="1">开启</option>\n' +
            '</select>\n' +
            '</td>');
        $table_body.find('tr').eq(index).children().eq(7).children().val(status);
        $table_body.find('tr').eq(index).children().eq(7).after('<td><a onclick="deleteMatch(' + data.id +')" href="#">删除</a></td>');

    }
}

function deleteMatch(matchId) {
    var conf = confirm('删除比赛会将该比赛下所有的信息都删除，确认删除？');
    if(conf === false) {
        return;
    }
    var id = index[matchId - 1];
    $.ajax({
        type : "post",
        dataType : 'json',
        url : doMainName + '/match/delete',
        data : {id : id},
        success : function (data) {
            if(data.ret === false) {
                alert(data.msg);
            } else {
                alert('删除成功');
                window.location.reload();
            }
        },
        error : function (jqXHR) {
            alert(jqXHR.data);
        }
    })
}


function fmtDate(obj){
    var date =  new Date(obj);
    var y = 1900+date.getYear();
    var m = "0"+(date.getMonth()+1);
    var d = "0"+date.getDate();
    return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
}

function setCookie(obj) {
    var oId = index[obj.parent().parent().parent().children().eq(0).html() -1];
    var oMatchName = obj.parent().parent().parent().children().eq(1).html();
    var Obj = {
        oId : oId,
        oMatchName : oMatchName
    };
    $.cookie('Obj',JSON.stringify(Obj));
    $.cookie('flag', true);
}

function setCookie2(obj) {
    var oId = index[obj.parent().parent().parent().children().eq(0).html() -1];
    var oMatchName = obj.parent().parent().parent().children().eq(1).html();
    var Obj = {
        oId : oId,
        oMatchName : oMatchName
    };
    $.cookie('Obj',JSON.stringify(Obj));
    $.cookie('flag', false);
}

function changeStatus(obj) {
    var status = parseInt(obj.val());
    var id = index[obj.parent().parent().children().eq(0).text() - 1];
    if(status === 1){
        obj.parent().prev().html(canEditHtml);
    } else {
        obj.parent().prev().html(cannotEditHtml);
    }
    $.ajax({
        type : 'post',
        url : doMainName + '/match/status',
        dataType : 'json',
        data : {id : id,status : status}
    })
}

function upload() {
    var url = doMainName + '/upload/file';
    $('#myform3').attr("action", url);
    document.getElementById("myform3").submit();
    alert("上传成功");
    window.location.reload();
}

$(function(){
    $('#file-upload3').on('change',function(){
        var fileName = this.files[0].name;
        var format = fileName.substring(fileName.indexOf('.'));
        if(format !== ".xlsx" && format !== ".xls") {
            alert("请选择.xlsx或者.xls的文件上传");
            return;
        }
        var f = this.files[0];
        var reader = new FileReader();
        reader.readAsBinaryString(f);
        reader.onload = function(e) {
            var data = e.target.result;
            zzexcel = XLSX.read(data, {
                type: 'binary'
            });
            var name = JSON.stringify(XLSX.utils.sheet_to_json(zzexcel.Sheets[zzexcel.SheetNames[0]])).split("\"")[5];
            $.ajax({
                type : "post",
                dataType : 'json',
                url : doMainName + '/match/verify',
                data : {name : name},
                success : function (data) {
                    if(data.ret) {
                        if(data.data === '666') {
                            var conf = confirm("该比赛已经存在，如果覆盖会把原有报名信息全部删除");
                            if(conf) {
                                upload();
                            }
                        } else if(data.data === '200') {
                            upload();
                        }
                    } else {
                        alert("系统出错, 请稍候再试!");
                    }
                },
                error : function (jqXHR) {
                    alert(jqXHR.data);
                }
            })
        }
    })
});
