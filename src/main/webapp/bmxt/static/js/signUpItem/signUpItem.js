var itemArr = [];
var athleteArr = [];
var $boat = $('.boat');
var $modal_body = $('.modal-body');
var doMainName = 'http://localhost:8080/bmxt';//在这里填上url的主体部分

$(function () {
    var team = $.cookie('team');
    if(team === 'CCA') {
        document.getElementById('ggg').style.display = 'none';
    }
});

$(function () {
    var obj = JSON.parse($.cookie('Obj'));
    var oId = obj.oId;
    var oMatchName = obj.oMatchName;
    $('.item-title').html(oMatchName);
    $.ajax({
        type : 'post',
        dataType : 'json',
        data :{id : oId},
        url : doMainName + '/item/list',
        success : function (data) {
           var result = data.data;
            constructTable(result);
            editItemListener(result);
        },
        error : function (jqXHR) {
            console.log(jqXHR);
        }
    })
});

function constructTable(result) {
    for(var i = 0; i < result.length; i++){
        var data = result[i];
        var limitNumber = data.number;
        var startTime = data.itemCondition.startTime;
        var endTime = data.itemCondition.endTime;
        var limitGender = data.itemCondition.gender;
        var tmpArr = [];
        tmpArr.push(limitNumber,startTime,endTime,limitGender);
        itemArr.push(tmpArr);
        constructTr(data);
    }
}

function constructTr(data) {
    var itemId = data.id;
    var itemName = data.name;
    var registeredNumber = data.registeredNumber;
    $('#table-body').append('<tr>\constructTr(data);n' +
        '                            <td>' + itemId + '</td>'+
        '                            <td>' + itemName + '</td>\n' +
        '                            <td>' + registeredNumber + '</td>\n' +
        '                            <td>\n' +
        '                                <a href="####" data-toggle="modal" data-target="#myModal" class="item-edit">\n' +
        '                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>\n' +
        '                                    编辑信息\n' +
        '                                </a>\n' +
        '                            </td>\n' +
        '                        </tr>');
}

function editItemListener(data) {//监听编辑信息点击事件
    var number = 0;
    var id = 0;
    var name = '';
    var registeredNumber = 0;
    var itemDto = {};
    var listItem = [];//存放下拉的运动员数组
    $('.item-edit').on('click',function () {
        var rowIndex = $(this).parent().parent().index();
        var modalTitle = $(this).parent().parent().children().eq(1).html();
        var rowData = data[rowIndex];
        number = rowData.number;
        name = rowData.name;
        id = rowData.id;
        registeredNumber = rowData.registeredNumber;
        itemCondition = rowData.itemCondition;
        itemDto = {
            id : id,
            name : name,
            registeredNumber : registeredNumber,
            number : number,
            itemCondition : itemCondition
        };
        var itemCondition = rowData.itemCondition;
        var startTime;
        var endTime;
        var warningTitle;
        if(itemCondition.startTime != null) {
            startTime = toChineseDate(itemCondition.startTime);
            warningTitle = '仅限' + startTime + '后出生的运动员参加';
        }
        if(itemCondition.endTime != null) {
            endTime = toChineseDate(itemCondition.endTime);
            warningTitle = '仅限' + endTime + '前出生的运动员参加';
        }
        if(itemCondition.startTime != null && itemCondition.endTime != null) {
            warningTitle = '仅限' + startTime + '至' + endTime + '期间出生的运动员参加';
        }
        $('.modal-title').html(modalTitle);
        $modal_body.html('<div class="alert alert-danger warning" role="alert"></div>');
        $('.warning').html(warningTitle);
        listItem = loadListItem(id);
        loadSignUped(id,number,listItem);
    });
    $('.add-boat').on('click',function () {
        addBoat(number,listItem);
    });
    $('.save-boat').on('click',function () {
        $('#myModal').modal('hide');
        var athleteMessageArr = [];
        var boatArr = [];
        var id;
        $('.athlete-input').each(function (index) {
            if($(this).val() !== '请选择') {
                athleteMessageArr.push($(this).val());
                if($(this).prev().html().indexOf("quitEdit") != -1) {
                    boatArr.push($(this).prev().html().substring(1, $(this).prev().html().indexOf("<")));
                    id = $(this).prev().html().substring(1, $(this).prev().html().indexOf("<"));
                } else {
                    boatArr.push(id);
                }
            }
        });
        var athleteList = [];
        for(var i = 0; i < athleteMessageArr.length; i++) {
            athleteList.push({
                athleteMessage : athleteMessageArr[i],
                boat : boatArr[i]
            })
        }
        var itemData = {
            athleteList : athleteList,
            itemDto : itemDto
        };
        $.ajax({
            type : 'post',
            url : doMainName + '/item/saveAthlete',
            dataType : 'json',
            data : {athleteList : JSON.stringify(athleteList), itemVo : JSON.stringify(itemDto)},
            success : function () {
                alert('保存成功！');
                window.location.reload();
            },
            error : function (jqXHR) {
                console.log(jqXHR);
                alert('保存失败，请重试！');
            }
        })
    })
}

function toChineseDate(date) {//把“yyyy-mm-dd”格式的日期转为“yyyy年mm月dd日”的中文格式
    var dateArr = date.split('-');
    var ChineseDate = dateArr[0] + '年' + dateArr[1] + '月' + dateArr[2] + '日';
    return ChineseDate;
}

function loadItemAthlete(list) {
    var option = '<option>请选择</option>';
    for(var i = 0; i < list.length; i++){
        option = option + '<option>' + list[i] + '</option>';
    }
    return option;
}

function constructSelect(num,list) { //根据项目限制人数添加select选项框
    var select = '';
    for(var i = 0; i < num; i++){
        select = select + '<select class="athlete-input">'+
                                loadItemAthlete(list) +
                            '</select> ';
    }
    return select;
}

function addBoat(num,list) { //增加舟艇,num为每一个艇上的人数
    var $boat_last = $('.boat:last');
    var boatId;
    if($boat_last.length !== 0){
        boatId = $('.boat:last .boat-id').html();
        boatId = boatId.substring(1, boatId.indexOf("<"));
        boatId ++;
        $boat_last.after('<div class="boat">\n' +
            '                    <div class="boat-id">艇' + boatId + '<i class="quit" onclick="quitEdit($(this))">&#xe623;</i>' +'</div>\n' + constructSelect(num,list) +
            '                </div>');
    } else {
        var $warning = $('.warning');
        boatId = 1;
        $warning.after('<div class="boat">' +
            '                  <div class="boat-id">艇' + boatId + '<i class="quit" onclick="quitEdit($(this))">&#xe623;</i>' +'</div>' +  constructSelect(num,list) +
            '           </div>');
    }
}

function loadSignUped(id,num,list) {//小项目中获取已经报名的运动员
    $.ajax({
        type : 'post',
        url : doMainName + '/item/getAthletes',
        dataType : 'json',
        data : {id : id},
        success : function (data) {
            var result = data.data;
            if(result.length > 0) {
                var boatNum = result[result.length - 1].boatId;
                for(var i = 0;i < boatNum; i++){
                    addBoat(num,list);
                }
                for(var j = 0;j < result.length; j++){
                    $('.athlete-input').eq(j).val(result[j].athleteMessage);
                }
            }
        },
        error : function (jqXHR) {
            console.log(jqXHR);
        }
    })
}

function quitEdit(obj) {
    obj.parent().parent().remove();
    var boatNum = $('.boat').length;
    for(var i = 1; i <= boatNum; i++){
        $('.boat .boat-id').eq(i-1).html('艇' + i + '<i class="quit" onclick="quitEdit($(this))">&#xe623;</i>');
    }
}

function loadListItem(id) {
    var result = [];
    $.ajax({
        type : 'post',
        dataType : 'json',
        url : doMainName + '/athlete/listItem',
        data : {id : id},
        async : false,
        success : function (data) {
            result = data.data;
        },
        error : function (jqXHR) {
            console.log('error : ',jqXHR);
        }
    });
    return result;
}
