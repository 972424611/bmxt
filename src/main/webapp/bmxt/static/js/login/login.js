/**监听输入框鼠标的点击和移除点击
 * **/

var $rewrite = $('.rewrite');
var $submit = $('.submit');
var $username = $('#username');
var $password = $('#password');
var $warning_bar = $('.warning-bar');

var doMainName = 'http://localhost:8080/bmxt';

window.onload = function () {
    $rewrite.on('click',function () {
        $username.val('');
        $password.val('');
    });
    $submit.on('click',function () {
        if(judgeForm()) {
            $.ajax({
                type : 'post',
                url : doMainName + '/user/login',
                dataType : 'json',
                data : {username : $username.val(), password : $password.val()},
                success : function (result) {
                    if(result.ret === true){
                        $.cookie('team',result.data);
                        if(result.data === 'CCA') {
                            window.location = './signUp.html';
                        } else {
                            window.location = './athlete.html';
                        }
                    } else {
                        alert('账号或密码不正确！');
                    }
                },
                error : function () {
                    alert('请检查网络！');
                }
            })
        } else {
            return false;
        }
    })
};

function judgeForm() {//用来判断用户名和密码是否为空
    if ($username.val() === '' && $password.val() === '') {
        $warning_bar.css('display', 'block');
        $warning_bar.html('请填写用户名和密码');
        return false;
    } else if ($username.val() === '' && $password.val() !== '') {
        $warning_bar.css('display', 'block');
        $warning_bar.html('请填写用户名');
        return false;
    } else if ($username.val() !== '' && $password.val() === '') {
        $warning_bar.css('display', 'block');
        $warning_bar.html('请填写密码');
        return false;
    } else {
        $warning_bar.css('display', 'none');
        return true;
    }
}