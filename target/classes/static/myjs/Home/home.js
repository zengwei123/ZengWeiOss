$(document).ready(function(){
    getfile_list();
    $("#quanxuan").change(function () {
        var flage = $("#quanxuan").is(':checked');
        $("#file_list input[disabled!='disabled']").each(function () {
            $(this).prop("checked", flage);
        });
    });
    /**返回上一级文件夹**/
    $("#file_back").click(function () {
       $("#url_file").children("a").length;
        if($("#url_file").children("a").length==1){
        }else{
            $("#url_file span:last-child").remove();
            $("#url_file a:last-child").remove();
            var last_name=$("#url_file a:nth-last-child(2)").attr("data");
            if(last_name==undefined||last_name=="undefined"){
                getfile_list();
            }else{
                getfile_list1(last_name);
            }
        }
    });
    /**新建文件夹**/
    $("#new_Folder").quickModal();
    $("#yes-button").click(function () {
        var n=$("#url_file a").last().attr("data");
        var new_Folder_input;
        if(n==undefined||n=="undefined"){
            new_Folder_input=$("#new_Folder_input_1").val();
        }else{
            new_Folder_input=$("#url_file").text().substring(4,$("#url_file").text().length-1)+"/"+$("#new_Folder_input_1").val();
        }
       // console.log(new_Folder_input+"-"+n)
        axios({
            method: 'post',
            url: 'PutFolder',
            data:{
                FolderName:new_Folder_input
            },
            headers: {"zwToken": sessionStorage.getItem("key")},
        }).then(function (response) {
            var json=response.data;
            var b=isState(json);
            if(b){
                if(n==undefined||n=="undefined"){
                    getfile_list();
                }else{
                    getfile_list1($("#url_file").text().substring(4,$("#url_file").text().length-1));
                }
                $("#my-modal").quickModal("close");
            }else{
                alert("新建文件夹失败！")
            }
        });
    });

    /**上传文件**/
    $("#put_Flie").quickModal();
    $("#put-button").click(function ()  {
        var file= $("#put_File_input_1")[0].files[0];
        var file_path=$("#put_File_input_1").val().split("\\");
        var path=$("#url_file").text().substring(4,$("#url_file").text().length-1);
        if(path=="/"){
            path=file_path[file_path.length-1];
        }else {
            path=path+"/"+file_path[file_path.length-1];
        }
        var formData = new FormData();
        formData.append("file",file);
        formData.append("path",path);
        $("#modal1_div").hide();
        $("#modal1_div1").show();
        $(".container").show();
        $("#put-button").hide();
        linkWeb();
        $.ajax({
            url:"/PutFile",
            type:'POST',
            cache:false,
            data:formData,
            processData:false,
            contentType:false,
            xhrFields: {
                withCredentials: true
            },
            headers: {"zwToken": sessionStorage.getItem("key")},
            crossDomain: true,
            success: function (data) {
                var c=$("#url_file").text().substring(4,$("#url_file").text().length-1);
                var b=isState(data);
                if(b){
                    if(c=="/"){
                        getfile_list()
                    }else {
                        getfile_list1(c)
                    }
                    }else{
                        alert("上传失败")
                    }
                    $("#modal1_div").show();
                    $("#modal1_div1").hide();
                    $(".container").hide();
                    $("#put-button").show();
                    $("#modal1_div1").quickModal("close");
                    cccc();
                    $("#sun_a").css("width","0%");
                }
            });
    });

    /**批量删除文件**/
    $("#delete_File").quickModal();
    $("#delete-button").click(function () {
        var inputs=$("#file_list").children("input");
        var arrs=[];
        var s=$("#url_file").text().substring(4,$("#url_file").text().length-1);
        if(s=="/"){
            for(var i=0;i<inputs.size();i++){
                if(inputs.eq(i).is(':checked')){
                    arrs[i]=inputs.eq(i).attr("value");
                }
            }
        }else{
            for(var i=0;i<inputs.size();i++){
                if(inputs.eq(i).is(':checked')){
                    arrs[i]=s+"/"+inputs.eq(i).attr("value");
                }
            }
        }
        if(arrs.length>0){
            axios({
                method: 'post',
                url: 'DeleteFile_2',
                data:{
                    list:arrs
                },
                headers: {"zwToken": sessionStorage.getItem("key")},
            }).then(function (response) {
                var json=response.data;
                var b=isState(json);
                if(b){
                    $("#modal1_div2").quickModal("close");
                    var n=$("#url_file").text().substring(4,$("#url_file").text().length-1);
                    if(n=="/"){
                        getfile_list();
                    }else {
                        getfile_list1(n);
                    }
                }else{
                    alert("删除失败！")
                }
            });
        }else{
            $("#modal1_div2").quickModal("close");
            alert("删除文件不能为空！")
        }
    })

    /**生成分享文件下载链接**/
    $("#share_s").click(function () {
        if($("#share_s").text()=="复制链接"){
            $("#my-modal3").quickModal('close');
            $("#share_url").select();
            document.execCommand("copy", false);
            $("#share_url").val("")
            $("#share_s").text("生成下载链接")
        }else{
            var sz= $("#share_span").text();
            axios({
                method: 'post',
                url: 'Share',
                data:{
                    FileName:sz
                },
                headers: {"zwToken": sessionStorage.getItem("key")},
            }).then(function (response) {
                var json=response.data;
                var b=isState(json);
                if(b){
                    $("#share_url").val("http://www.zengwei123.top:8089/Share?shareToken="+json.date+"=&fileName="+$("#share_span").text());
                    $("#share_s").text("复制链接")
                }else{
                    alert("分享失败！")
                }
            });
        }
    })

});
/**默认根文件夹**/
function getfile_list() {
    $("#file_list").empty()
    $("#url_file").html("<a href='#' onclick='getfile_list()'>...</a>/")
    axios({
        method: 'post',
        url: 'FatherQueryFile',
        headers: {"zwToken": sessionStorage.getItem("key")},
    }).then(function (response) {
        var json=response.data;
        var b=isState(json);
        if(b){
            var dateArr=json.date;
            for(var i=0;i<dateArr.length;i++){
                IsFile(dateArr[i]);
            }
        }else{
           alert("服务器异常！")
        }
    });
}
/**输入文件夹**/
function getfile_list1(z) {
    $("#file_list").empty()
    axios({
        method: 'post',
        url: 'SonQueryFile',
        data:{
            name:z
        },
        headers: {"zwToken": sessionStorage.getItem("key")},
    }).then(function (response) {
        var json=response.data;
        var b=isState(json);
        if(b){
            var dateArr=json.date;
            for(var i=0;i<dateArr.length;i++){
                IsFile(dateArr[i]);
            }
        }else{
            alert("服务器异常！")
        }
    });
}
/**查询解析文件夹中的文件，并且显示**/
function IsFile(fileString) {
   // console.log(fileString);
    var s=fileString.split("/");
    if(fileString.charAt(fileString.length-1)=="/"){
        $("#file_list").append("" +
            "<input style='float: left;margin-top: 10px' type='checkbox' disabled='disabled'/>" +
            "<Li class='ul_li_style_2' onclick='file_click(this)'>" +
            "<img style='float: left' src='../static/Image/文件夹.png'/>" +
            "<span >"+s[s.length-2]+"</span>" +
            " <a onclick='deletes(this)' datas='"+fileString+"' style='float: right;margin-right: 20px'>删除</a>"+
            "</Li>")
    }else{
        var z=fileString.split(".")
        var geshi="../static/Image/文件.png";
        if(z[1]=="exe"){
            geshi="../static/Image/exe.png";
        }else if(z[1]=="mp3"||z[1]=="MIDI"||z[1]=="midi"){
            geshi="../static/Image/歌曲.png";
        }else if(z[1]=="rar"||z[1]=="zip"||z[1]=="jar"){
            geshi="../static/Image/压缩文件.png";
        }else if(z[1]=="png"||z[1]=="PNG"||z[1]=="JPG"||z[1]=="jpg"||z[1]=="JPEG"||z[1]=="jgeg"||z[1]=="GIF"||z[1]=="gif"){
            geshi="../static/Image/图片.png";
        }else if(z[1]=="avi"||z[1]=="mp4"||z[1]=="mkv"||z[1]=="asf"||z[1]=="vob"||z[1]=="rm"||z[1]=="wmv"||z[1]=="gif"){
            geshi="../static/Image/视频.png";
        }else{
            geshi="../static/Image/文件.png";
        }
        var sz=$("#url_file").text().substring(4,$("#url_file").text().length-1);
        if(sz=="/"){
            sz=s[s.length-1];
        }else{
            sz=sz+"/"+s[s.length-1];
        }
        $("#file_list").append("" +
            "<input style='float: left;margin-top: 10px' type='checkbox' value='"+s[s.length-1]+"'/>" +
            "<Li onclick='Share(\""+sz+"\")' class='ul_li_style_2' data-modal-id='my-modal3'>" +
            "<img style='float: left' src='"+geshi+"'/>" +
            "<span> "+s[s.length-1]+"</span>" +
            "<a onclick='a_download()' download='"+s[s.length-1]+"' href='/DownloadFile?file="+sz+"' style='float: right;margin-right: 20px'>下载</a>"+
            "</Li>")
    }
}
/**点击文件夹**/
function file_click(id) {
    var s=$("#url_file").html()+"<a href='#' onclick='file_a_click(this)' data='"+$(id).children("span").text()+"'>"+$(id).children("span").text()+"</a><span>/</span>";
    var z=$("#url_file").text().substring(4,s.length)+$(id).children("span").text();
    $("#url_file").html(s);
    getfile_list1(z);
    $("#quanxuan").attr('checked',false);
}
/**文件夹路径，返回想要返回的上级文件夹**/
function file_a_click(id) {
    $("#file_list").empty();
    $(id).nextAll().remove();
    $("#url_file").html($("#url_file").html()+"<span>/</span>");
    var as=$("#url_file").text().substring(4,$("#url_file").text().length-1);
    if(as==undefined||as=="undefined"){
        getfile_list();
    }else{
        getfile_list1(as);
    }
}
/**删除文件夹**/
function deletes(id) {
    var filename=$(id).attr("datas");
    //console.log(filename)
    axios({
        method: 'post',
        url: 'DeleteFile_1',
        data:{
            FileName:filename
        },
        headers: {"zwToken": sessionStorage.getItem("key")},
    }).then(function (response) {
        var json=response.data;
        var b=isState(json);
        if(b){
            var lastname=$("#url_file").text().split("/");
            if(lastname[lastname.length-2]=="..."){
                getfile_list();
            }else{
                var s=$("#url_file").text().substring(4,$("#url_file").text().length-1);
                getfile_list1(s);
            }
        }else{
            alert("服务器异常！")
        }
    });
    event.stopPropagation();
}
function Share(sz) {
    $("#share_span").text(sz)
    $("#my-modal3").quickModal('open');
    $("#share_url").val("")
    $("#share_s").text("生成下载链接")
}
/**文件下载防止事件穿透**/
function a_download() {
    event.stopPropagation();
    return false;
}