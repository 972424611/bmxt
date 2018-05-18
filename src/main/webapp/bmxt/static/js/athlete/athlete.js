var $change_athlete_name = $('#change-athlete-name');
var $change_sex = $('#change-sex');
var $change_birthday = $('#change-birthday');
var $change_item = $('.change-checkbox');
var $change_teamame = $('#change-teamname');
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

var fileRandomName = '';
var doMainName = 'http://39.108.5.210/bmxt';
var totalNum;

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
            lang : 'cn'
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
                alert('未找到符号条件的运动员');
            }
        })
    }

    layui.use('laypage', function(){
        var laypage = layui.laypage;
        laypage.render({
            elem: 'pageNation'
            ,count: totalNum
            ,limit : 10
            ,curr : 1
            ,jump : function (obj,first) {
                if(!first){
                    loadTable('/athlete/list',obj.curr,obj.limit);
                }
            }
        });
    });
});



function constructTable(result) {
    $('#table-body').html('');
    for(var i = 0; i < result.data.length; i++){
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
        id = infoArr[0];
        var name = infoArr[1];
        var sex = infoArr[2];
        var birthday = infoArr[3];
        var item = infoArr[4].split('-');
        var teamName = infoArr[5];
        var photoUrl = dataArr.data[trIndex].photoUrl;
        $pic_container.html('<img src="' + photoUrl + '">');
        $change_item.each(function () {
            $(this).attr('checked',false);
            for(var i = 0; i < item.length; i++){
                if($(this).next().html() === item[i]) {
                    $(this).attr('checked','checked');
                }
            }
        });
        $change_athlete_name.val(name);
        $change_sex.val(sex);
        $change_birthday.val(birthday);
        $change_teamame.val(teamName);
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
        var oId = $(this).parent().parent().children().eq(0).html();
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
        url : '/athlete/delete',
        data : {id : id},
        success : function (data) {
            alert('删除成功');
            window.location.reload();
        },
        error : function (jqXHR) {
            console.log('error : ',jqXHR);
        }
    })
}
function changeAthlete(trIndex, id) {//点击保存时修改运动员信息并刷新该运动员的信息
    var $tmp = $('#table-body').children('tr').eq(trIndex).children('td');
    $tmp.eq(1).html($change_athlete_name.val());
    $tmp.eq(2).html($change_sex.val());
    $tmp.eq(3).html($change_birthday.val());
    $tmp.eq(5).html($change_teamame.val());
    var tmpArr = [];
    var itemNames = '';
    $change_item.each(function () {
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
    $tmp.eq(4).html(itemNames);
    $.ajax({
        type : 'post',
        dataType : 'json',
        url : doMainName + '/athlete/update',
        data : {id : id, name : $change_athlete_name.val(), team : $change_teamame.val(), gender : $change_sex.val(), birthday : $change_birthday.val(), event : itemNames, photoName: fileRandomName},
        success : function (data) {
            alert('修改运动员成功');
            window.location.reload();
        },
        error : function (jqXHR) {
            console.log(jqXHR);
            alert('修改运动员失败，请重试！')
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
                $pic_container.val('');
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
    var addAthleteName = $modal_athlete_name.val();
    var addTeamname = $modal_teamname.val();
    var addSex = $modal_sex.val();
    var addBirthday = $modal_birthday.val();
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
        data : {name : addAthleteName, team : addTeamname, gender : addSex, birthday : addBirthday, event : itemNames, photoName: fileRandomName},
        success : function (data) {
            alert('添加运动员成功');
            window.location.reload();
        },
        error : function (jqXHR) {
            console.log(jqXHR);
            alert('添加运动员失败，请重试！')
        }
    })
});
$('#add-one').on('click',function () {
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem : '#modal-birthday',
            lang : 'cn'
        });
    });
    $modal_teamname.val($.cookie('team'));
    $add_item.val('');
    $modal_athlete_name.val('');
    $modal_sex.val('');
    $modal_birthday.val('');
    $('.file-upload').val('');
    $('.pic-container').html('');
    $('.add-checkbox').val('');
});


