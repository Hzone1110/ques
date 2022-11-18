var BASE_URL = `http://${location.host}/dev-api`

// 实时租户
$.ajax({
    type: 'GET',			//以post方法提交数据给服务器
    url: BASE_URL + "/stat/allCompany",				//提交数据到User
    dataType: "json",		//数据类型
    success: function (res) {			//回调函数
        var data = res.data
        var str = ''
        for (let index = 0; index < data.length; index++) {
            const element = data[index];
            str += `<li class='clearfix'> <span class='pulll_left'>${element.companyName}</span> <span class='pulll_right'>到期时间：${element.activeTime}</span> </li>`
        }
        document.getElementById("company").innerHTML = str
    }
});

// 实时问卷
$.ajax({
    type: 'GET',			//以post方法提交数据给服务器
    url: BASE_URL + "/stat/allQues",				//提交数据到User
    dataType: "json",		//数据类型
    success: function (res) {			//回调函数
        var data = res.data
        var str = ''
        for (let index = 0; index < data.length; index++) {
            const element = data[index];
            // str += `<li class='clearfix'> <span class='pulll_left'>${element.companyName}</span> <span class='pulll_right'>到期时间：${element.activeTime}</span> </li>`

            str += ` <li>
        <p>${element.nickName}-${element.title}-${element.createTime}</p>
    </li>`
        }
        document.getElementById("ques").innerHTML = str
    }
});


// 实时问卷
$.ajax({
    type: 'GET',			//以post方法提交数据给服务器
    url: BASE_URL + "/stat/answerCount",				//提交数据到User
    dataType: "json",		//数据类型
    success: function (res) {			//回调函数
        var data = res.data
        /**
         * jrdt: 0
         * jrtk: 0
         * zrdt: 3
         * zrtk: 0
         */
        var d1 = data.jrtk
        var d2 = data.zrtk
        var d4 = data.jrdt
        var d5 = data.zrdt
        console.log(data);
        document.getElementById("d1").innerText = d1
        document.getElementById("d2").innerText = d2
        document.getElementById("d4").innerText = d4
        document.getElementById("d5").innerText = d5


    }
});


