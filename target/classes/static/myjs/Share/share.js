$(document).ready(function(){
    console.log(sessionStorage.getItem("key"))
    axios({
        method: 'post',
        url: 'Share1',
        data:{
            shareToken:Request[1]
        }
    }).then(function (response) {
        var json=response.data;
        var b=isState(json);
        if(b){
            $("#xiazai").attr("href",json.date);
            $("#imgs").attr("src","https://tool.kd128.com/qrcode?text="+encodeURIComponent(json.date));
          $("#fileName").text(decodeURIComponent(Request[3]))
        }else{
            alert("error")
        }
    });
});