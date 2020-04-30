$(function() { // 页面加载完执行
    // 1.全选按钮
    function init() {
        $('#selectAll').change(function(e) {
            // 问题1：当选中其中一项时，勾选全选，再取消全选，原来勾选的复选框没有改变。
            // 2.遍历获取子复选框
            const $checkBox = $('tbody input[type="checkbox"]');
            $('#allInput')[0].checked = e.currentTarget.checked;
            for (let i = 0, len = $checkBox.length; i < len; i++) {
                // 如果子复选框的选中状态和全选复选框的选中状态不一样的话，就让它们一样。
                if ($checkBox[i].checked !== e.currentTarget.checked) {
                    $checkBox[i].checked = e.currentTarget.checked;
                }
            }
            getTotal();
        });
    }
    init();
    // 问题2：在另一个全选复选框，怎么实现与之前全选复选框的状态同步？（copy上面代码）
    $('#allInput').change(function(e) {
        const $checkBox = $('tbody input[type="checkbox"]');
        $('#selectAll')[0].checked = e.currentTarget.checked;
        for (let i = 0, len = $checkBox.length; i < len; i++) {
            if ($checkBox[i].checked !== e.currentTarget.checked) {
                $checkBox[i].checked = e.currentTarget.checked;
            }
        }
        getTotal();
    });

    // 问题3：当子复选框其中一个取消勾选时，两个全选复选框应该都取消勾选。
    // 怎么监听3个子复选框？
    $('tbody input[type="checkbox"]').change(function(e) {
        if (this.checked == false) {
            $('#selectAll')[0].checked = false;
            $('#allInput')[0].checked = false;
        }
        const $checkBox = $('tbody input[type="checkbox"]');
        // 声明一个计数器，用于标记选择的数量
        let count = 0;
        for (let i = 0, len = $checkBox.length; i < len; i++) {
            if ($checkBox[i].checked == true) {
                count++;
            }
            $checkBox[i].onclick = function() {
                getTotal();
            }
        }
        // 问题4：当子复选框都选上的时候，两个全选复选框应该都勾选上。
        if (count == $checkBox.length) {
            $('#selectAll')[0].checked = true;
            $('#allInput')[0].checked = true;
        }

    });

    // 获取表格行数据。表格的rows、cells属性不熟悉！
    let tr = $('#cartTable')[0].children[1].rows;
    // 商品的件数
    let selectedGoods = $('.selected')[0];

    // 商品结算价格
    let totalGoods = $('.total')[0];

    // 封装计算函数
    function getTotal() {
        var selected = 0;
        var price = 0;
        // 拼接商品预览html字符串
        var HTMLstr = '';
        for (var i = 0, len = tr.length; i < len; i++) {
            if (tr[i].getElementsByTagName('input')[0].checked) {
                tr[i].className = 'on';
                selected += parseInt(tr[i].getElementsByTagName('input')[1].value);
                price += parseFloat(tr[i].cells[4].innerHTML);
                HTMLstr += '<div><img height="80" src="' + tr[i].getElementsByTagName('img')[0].src + '"><span class="del" index="' + i + '">取消选择</span></div>';
            } else {
                tr[i].className = '';
            }
        }
        selectedGoods.innerHTML = selected;
        totalGoods.innerHTML = price.toFixed(2);
        $('.float-div')[0].innerHTML = HTMLstr;
    }

    // 封装小计函数:参数为tr dom对象
    function getSubTotal(tr) {
        var tds = tr.cells;
        var price = parseFloat(tds[2].innerHTML);
        var count = parseInt(tr.getElementsByTagName('input')[1].value);
        tds[4].innerHTML = parseFloat(price * count).toFixed(2);
    }
    // 问题：不能使用jQuery方法获取表格内的input子元素？
    // const $checkBox = $('tbody input[type="checkbox"]');
    // for (var i = 0, len = $checkBox.length; i < len; i++) {
    //     $checkBox[i].onclick = function() {
    //         getTotal();
    //     }
    // }
    // 商品预览浮层功能
    $('.selGoods').click(function() {
        // console.log(parseInt($('.selected')[0].innerHTML))
        if ($('.foot').hasClass('show')) {
            $('.foot').removeClass('show');
        } else {
            if (parseInt($('.selected')[0].innerHTML) !== 0) {
                $('.foot').addClass('show');
            }
        }

    });

    // 取消选择功能
    $('.float-div').click(function(e) {
        // console.log(e.target.className);
        // console.log(e.target.getAttribute("index"));
        if (e.target.className == "del") {
            let index = e.target.getAttribute("index");
            var input = tr[index].getElementsByTagName('input')[0];
            input.checked = false;
            // 手动触发复选框的点击事件。
            input.onclick();
            if (parseInt($('.selected')[0].innerHTML) == 0) {
                $('.foot').removeClass('show');
            }
        }
    });

    // 增减商品数量
    for (var i = 0, len = tr.length; i < len; i++) {
        tr[i].onclick = function(e) {
            // debugger
            if (e.target.className == 'add') {
                this.getElementsByTagName('input')[1].value = parseInt(this.getElementsByTagName('input')[1].value) + 1;
                getSubTotal(this);
            } else if (e.target.className == 'reduce') {
                if (parseInt(this.getElementsByTagName('input')[1].value) <= 1) {
                    this.getElementsByTagName('input')[1].value = 1;
                } else {
                    this.getElementsByTagName('input')[1].value = parseInt(this.getElementsByTagName('input')[1].value) - 1;
                }
                getSubTotal(this);
            } else if (e.target.className == 'delete') {
                var conf = confirm("确定要删除吗？");
                if (conf) {
                    this.remove();
                }
            }
            getTotal();
        }

        // 监听数量文本框的键盘抬起事件
        tr[i].getElementsByTagName('input')[1].onkeyup = function() {
            var val = this.value;
            // 禁止输入负数，或者字母汉字符号等非数字。
            if (val <= 1 || isNaN(val)) {
                this.value = 1;
            }
            getSubTotal(this.parentNode.parentNode);
            getTotal();
        }
    }

    // 监听底部栏全部删除功能
    $('.deleteAll').click(function() {
        if ($('.selected')[0].innerHTML != '0') { // 未选中商品，不弹出删除确认框。
            var conf = confirm("确定删除所选中的商品？");
            if (conf) {
                for (let index = 0; index < tr.length; index++) {
                    if (tr[index].getElementsByTagName("input")[0].checked) {
                        tr[index].remove();
                        // ['a','b','c'] 索引：i==0 删除第一个 'a', 剩余 ['b','c'] 索引：i==1,所以删除'c'
                        index--;
                    }
                }
                // 置空件数和总额
                $('.selected')[0].innerHTML = '0';
                $('.total')[0].innerHTML = '0.00';
                $('#selectAll')[0].checked = false;
                $('#allInput')[0].checked = false;
            }
        }
    })


    $('#selectAll')[0].checked = true;
    $('#allInput')[0].checked = true;
    initZero();

    function initZero() { //  初始全部选中
        const $checkBox = $('tbody input[type="checkbox"]');
        for (let i = 0, len = $checkBox.length; i < len; i++) {
            $checkBox[i].checked = true;
        }
        getTotal();
    }
});