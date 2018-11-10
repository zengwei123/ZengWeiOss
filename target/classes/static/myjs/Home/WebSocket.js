var websocket = null;
function linkWeb(){
    if('WebSocket' in window){
        websocket = new WebSocket("ws://www.zengwei123.top:8089/websocket");
    } else{
        alert('浏览器版本过低，不支持服务！');
    }
    //连接发生错误的回调方法
    websocket.onerror = function(){
        alert('异常，请刷新页面恢复');
    };

    //连接成功建立的回调方法
    websocket.onopen = function(event){
    }
    //接收到消息的回调方法
    websocket.onmessage = function(event){
        $("#sun_a").css("width",event.data);
        $("#sun_b").text(event.data)
    }
    //连接关闭的回调方法
    websocket.onclose = function(){
        websocket.close();
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }
};

function cccc() {
    websocket.close();
}
