var $change_athlete_name = $('#change-athlete-name');
var $change_sex = $('#change-sex');
var $change_birthday = $('#change-birthday');
var $change_item = $('.change-checkbox');
var $change_teamame = $('#change-teamname');
//var $change_athlete_number  =$('#change-athlete-number');
var $search = $('#search');
var $search_clean = $('#search-clean');
var $myModalChange = $('#myModal-change');
var $pic_container = $('.pic-container');
var $save_change = $('.save-change');

var $add_item = $('.add-checkbox');
var $modal_athlete_name = $('#modal-athlete-name');
var $modal_teamname = $('#modal-teamname');
var $modal_sex = $('#modal-sex');
var $modal_birthday = $('#modal-birthday');
var $modal_number = $('#modal-number');

var fileRandomName = '';
var doMainName = $.cookie('doMainName');
var totalNum;

var index = [];

var team = ["安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "黑龙江",
            "河南", "香港", "湖北", "湖南", "内蒙古自治区", "江苏", "江西", "吉林", "辽宁", "澳门", "宁夏", "青海",
            "陕西", "山东", "上海", "山西", "四川", "台湾", "天津", "西藏", "新疆", "云南", "浙江"];

$(function () {
    $('#logout').attr("href", doMainName + "/user/logout");
    $('#team_name').html('<span class="glyphicon glyphicon-user" aria-hidden="true"></span>\n' + $.cookie('team'));
    if($.cookie('team') === 'CCA' || $.cookie('team') === 'admin') {
        document.getElementById("modal-teamname").removeAttribute("readonly");
        document.getElementById("change-teamname").removeAttribute("readonly");
    } else {
        $('#teamname').val($.cookie('team'));
        $('#teamname').attr('readonly', 'readonly');
    }
});

/**运动员查询部分开始*/
$(function () {
    var $athlete_name = $('#athlete-name');
    var $sex = $('#sex');
    var $birthday = $('#birthday');
    var $classname = $('#classname');
    var $teamname = $('#teamname');
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem : '#birthday',
            lang : 'cn',
        });
    });
    loadTable('/athlete/list',1,10);
    $search.on('click',function () {
        loadTable('/athlete/listCondition');
    });
    $search_clean.on('click',function () {//监听重置按钮
        $athlete_name.val('');
        $teamname.val('');
        $sex.val('');
        $classname.val('');
        $birthday.val('');
    });
    function loadTable(suffix,pageNo,pageSize) {
        $.ajax({
            type : 'post',
            url : doMainName + suffix,
            dataType : 'json',
            async : false,
            data : {
                "name": $athlete_name.val(),
                "team": $teamname.val(),
                "gender": $sex.val(),
                "event": $classname.val(),
                "birthday": $birthday.val(),
                "pageNo" : pageNo,
                "pageSize" : pageSize
            },
            success : function (result) {
                var dataArray = result.data;
                totalNum = result.data.total;
                constructTable(dataArray);
                tableButtonListener(dataArray);
            },
            error : function (jqXHR) {
                alert(jqXHR.data);
            }
        });
        layui.use('laypage', function(){
            var laypage = layui.laypage;
            laypage.render({
                elem: 'pageNation'
                ,count: totalNum
                ,limit : 10
                ,curr : pageNo
                ,jump : function (obj,first) {
                    if(!first){
                        loadTable('/athlete/listCondition',obj.curr,obj.limit);
                    }
                }
            });
        });
    }


});

function constructTable(result) {
    $('#table-body').html('');
    for(var i = 0; i < result.data.length; i++){
        index[i] = result.data[i].id;
        result.data[i].id = i + 1;
        var tmp = result.data[i];
        constructTr(tmp);
    }
}

function constructTr(oTr) {
    $('#table-body').append('<tr>\n' +
        '                                    <td>' + oTr.id + '</td>\n' +
        '                                    <td>' + oTr.name + '</td>\n' +
        '                                    <td>' + oTr.gender + '</td>\n' +
        '                                    <td>' + oTr.birthday + '</td>\n' +
        '                                    <td>' + oTr.event + '</td>\n' +
        '                                    <td>' + oTr.team + '</td>\n' +
        '                                    <td>\n' +
        '                                        <button type="button" class="btn btn-info change" data-toggle="modal" data-target="#myModal-change">修改</button>\n' +
        '                                        <button type="button" class="btn btn-info delete">删除</button>\n' +
        '                                    </td>\n' +
        '                                </tr>');
}

function tableButtonListener(dataArr) {//监听两个按钮的触发事件
    var trIndex;
    var id;
    $('.change').on('click',function () {
        var infoArr = [];
        $(this).parent().parent().children().each(function (index) {
            infoArr.push($(this).html());
        });
        trIndex = $(this).parent().parent().index();
        id = index[infoArr[0] - 1];
        var name = infoArr[1];
        var sex = infoArr[2];
        var birthday = infoArr[3];
        var item = infoArr[4].split('-');
        var teamName = infoArr[5];
        var photoUrl = dataArr.data[trIndex].photoUrl;
        if(photoUrl !== null && photoUrl !== '') {
            $pic_container.html('<img src="' + photoUrl + '">');
        } else {
            $pic_container.html('<img src="../static/img/comment/noPic.png">');
        }
        $change_item.each(function () {
            $(this).prop('checked',false);
            for(var i = 0; i < item.length; i++){
                if($(this).next().html() === item[i]) {
                    $(this).prop('checked','checked');
                }
            }
        });
        $change_athlete_name.val(name);
        $change_sex.val(sex);
        $change_birthday.val(birthday);
        $change_teamame.val(teamName);
        //$change_athlete_number.val(n)
    });
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem : '#change-birthday',
            lang : 'cn'
        });
    });
    $save_change.on('click',function () {
        $myModalChange.modal('hide');
        changeAthlete(trIndex, id);
    });
    /*运动员删除开始*/
    $('.delete').on('click',function () {
        var conf = confirm('确认删除该信息？');
        var oId = index[$(this).parent().parent().children().eq(0).html() - 1];
        if(conf) {
            deleteOtr(oId);
        }
    })
    /*运动员删除结束*/
}
function deleteOtr(id) {//向后台发送ajax请求，删除一行信息并且在前端进行删除
    $(this).parent().parent().html('');
    //ajax部分
    $.ajax({
        type : 'post',
        dataType : 'json',
        url : doMainName + '/athlete/delete',
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
function changeAthlete(trIndex, id) {//点击保存时修改运动员信息并刷新该运动员的信息
    var $tmp = $('#table-body').children('tr').eq(trIndex).children('td');
    /*$tmp.eq(1).html($change_athlete_name.val());
    $tmp.eq(2).html($change_sex.val());
    $tmp.eq(3).html($change_birthday.val());
    $tmp.eq(5).html($change_teamame.val());*/
    var tmpArr = [];
    var itemNames = '';
    $change_item.each(function () {
        if(this.checked) {
            tmpArr.push($(this).next().html());
        }
    });
    if(tmpArr.length === 1){
        itemNames = tmpArr[0];
    } else {
        for(var i = 0; i < tmpArr.length; i++) {
            if(i === tmpArr.length - 1){
                itemNames += tmpArr[i];
            } else {
                itemNames += tmpArr[i] + '-';
            }
        }
    }
    //$tmp.eq(4).html(itemNames);
    //TODO 更新功能暂且不做更新身份证，要做的时候要把这个给替换掉
    var number = "000000";
    $.ajax({
        type : 'post',
        dataType : 'json',
        url : doMainName + '/athlete/update',
        data : {id : id, name : $change_athlete_name.val(), team : $change_teamame.val(), gender : $change_sex.val(), birthday : $change_birthday.val(),  number : number, event : itemNames, photoName: fileRandomName},
        success : function (data) {
            if(data.ret === false) {
                alert(data.msg);
            } else {
                alert('修改运动员成功');
            }
            window.location.reload();
        },
        error : function (jqXHR) {
            alert(jqXHR.data);
        }
    });
}

$(function(){
    /**添加运动员信息部分, 图片上传部分开始*/
    $('#file-upload').on('change',function(){
        var fileName = this.files[0].name;
        var format = fileName.substring(fileName.indexOf('.'));
        fileRandomName = randomString(8);
        var url = doMainName + "/upload/picture?id=" + fileRandomName;
        $('#myform').attr("action", url);
        var a = fileUpload(this);
        while(1) {
            if(a === -1) {
                break;
            }
            if(a === true) {
                document.getElementById("myform").submit();
                fileRandomName = fileRandomName + format;
                break;
            }
        }
    });

    /**修改运动员信息部分, 图片上传部分开始*/
    $('#file-upload2').on('change',function(){
        var fileName = this.files[0].name;
        var format = fileName.substring(fileName.indexOf('.'));
        fileRandomName = randomString(8);
        var url = doMainName + "/upload/picture?id=" + fileRandomName;
        $('#myform2').attr("action", url);
        var a = fileUpload(this);
        while(1) {
            if(a === -1) {
                break;
            }
            if(a === true) {
                document.getElementById("myform2").submit();
                fileRandomName = fileRandomName + format;
                break;
            }
        }
    })
});

function randomString(len) {//生成指定长度的随机字符串
    var data=["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"];
    var result = "";
    for(var i = 0; i < len; i++){
        var r = Math.floor(Math.random()*62);
        result += data[r];
    }
    return result;
}

function fileUpload(obj) {
    var files = obj.files[0];
    var fileSize = files.size;
    var fileName = files.name;
    var index = fileName.indexOf('.');
    if($.cookie('team') === 'CCA' || $.cookie('team') === 'admin') {
        if(fileName.indexOf('-') === -1) {
            alert("图片名格式不正确, 请使用例如: 小明-北京");
            return -1;
        }
        var name = fileName.substring(0, index);
        if(name.split('-').length > 3) {
            alert("图片格式不正确, 请使用例如: 小明-北京");
            return -1;
        }
        var t = name.split("-")[1];
        for(var s in team) {
            if(team[s] === t) {
                break;
            }
        }
        if(team[s] !== t) {
            alert("图片名中代表队名称不正常.");
            return -1;
        }
    }
    var fileNameSuffix = fileName.substring(index);//存放文件后缀
    if(fileNameSuffix === '.jpg' || fileNameSuffix === '.png'){
        var size = fileSize / 1024 / 1024;
        if (size > 1) {
            alert("上传的图片不能大于1M！");
            this.value="";
            return -1;
        } else {
            var fileReader = new FileReader();
            fileReader.readAsDataURL(files);
            fileReader.onloadend = function (ev) {
                var src = ev.target.result;
                $pic_container.html('<img src="' + src + '">');
            };
            return true;
        }
    } else {
        alert('请上传jpg或png格式的文件');
        return -1;
    }
}

/**图片上传部分结束*/


/**添加运动员部分开始*/
$('.add-athlete').on('click',function () {
    confirm("请仔细检查所有信息后确认，确认之后将无法修改！");
    var addAthleteName = $modal_athlete_name.val();
    var addTeamname = $modal_teamname.val();
    var addSex = $modal_sex.val();
    var addBirthday = $modal_birthday.val();
    var number = $modal_number.val();
    var tmpArr = [];
    var itemNames = '';
    $add_item.each(function () {
        if(this.checked){
            tmpArr.push($(this).next().html());
        }
    });
    if(tmpArr.length === 1){
        itemNames = tmpArr[0];
    } else {
        for(var i = 0; i < tmpArr.length; i++){
            if(i === tmpArr.length - 1){
                itemNames += tmpArr[i];
            } else {
                itemNames += tmpArr[i] + '-';
            }
        }
    }
    $.ajax({
        type : 'post',
        dataType : 'json',
        url : doMainName + '/athlete/save',
        data : {name : addAthleteName, team : addTeamname, gender : addSex, birthday : addBirthday, number : number, event : itemNames, photoName: fileRandomName},
        success : function (data) {
            if(data.ret === false) {
                alert(data.msg);
            } else {
                alert('添加运动员成功');
                window.location.reload();
            }
        },
        error : function (jqXHR) {
            alert(jqXHR.data);
        }
    })
});
$('#add-one').on('click',function () {
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem : '#modal-birthday',
            lang : 'cn',
            value : '2000-01-01',
            isInitValue : true
        });
    });
    if($.cookie('team') === 'CCA' || $.cookie('team') === 'admin') {
        $modal_teamname.val('');
    } else {
        $modal_teamname.val($.cookie('team'));
    }
    $add_item.val('');
    $modal_athlete_name.val('');
    $modal_sex.val('');
    $modal_birthday.val('2000-01-01');
    $('.file-upload').val('');
    $('.pic-container').html('<img src="../static/img/comment/noPic.png" alt="">');
    $('.add-checkbox').val('');
});