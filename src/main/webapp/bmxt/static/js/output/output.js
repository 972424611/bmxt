
var myCookie = JSON.parse($.cookie('Obj'));
var matchId = myCookie.oId;
// var matchId = 1;
var doMainName = $.cookie('doMainName');
var $output_table_body = $('#output-table-body');
var $itemTotal = $('#itemTotal');
var $boatTotal = $('#boatTotal');
var $athleteTotal = $('#athleteTotal');
var $matchName = $('#matchName');
var $time = $('#time');

$.ajax({
    type: 'post',
    url: doMainName + '/match/table',
    dataType: 'json',
    data: {'id': matchId},
    success: function (data) {
        var result = data.data;
        var tableData = result.itemInfoList;
        constructTable(tableData);
        constructNavInfo(result);
        constructBottomInfo(result);

        window.print();

    },
    error: function (jqXHR) {

    }
});

function constructTr(data) {
    var itemName = data.itemName;
    if(itemName === null){
        itemName = '';
    }
    $output_table_body.append('<tr>\n' +
        '                        <td>' + itemName + '</td>\n' +
        '                        <td>' + data.team + '</td>\n' +
        '                        <td>' + data.athleteName + '</td>\n' +
        '                        <td>' + data.boatId + '</td>\n' +
        '                    </tr>');
}

function constructTable(tableData) {
    for(var i = 0; i < tableData.length; i++){
        constructTr(tableData[i]);
    }
}

function constructBottomInfo(result) {
    $itemTotal.html('小项数量：' + result.itemTotal);
    $boatTotal.html('船只数量：' + result.boatTotal);
    $athleteTotal.html('运动员总数' + result.athleteTotal);
}

function constructNavInfo(result) {
    $matchName.html('' + result.matchName);
    $time.html('' + result.time);
}