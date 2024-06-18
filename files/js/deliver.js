var send
var text
var textout
var fileout
var textin
var filein
var filesendinput
var droparea
var sendsection
var getsection
var namebtn
var namearea
var nameopen = false
var save = false
var timedisplay
var getsubmitbtn


window.onload = function () {
    document.getElementById("sendbtn").classList.add("checked")
    document.getElementById("textbtn").classList.add("checked")
    send = true
    text = true
    textout = document.getElementById("textout")
    fileout = document.getElementById("filearea")
    textout.style.display = "none";
    fileout.style.display = "none";

    sendsection = document.getElementById("sendsection")
    getsection = document.getElementById("getsection")
    getsection.style.display = "none";
    sendsection.style.display = "flex";


    textin = document.getElementById("textin")
    filein = document.getElementById("fileinput")
    filein.style.display = "none";

    namebtn = document.querySelector("label#namearea>a#label")
    namearea = document.querySelector("label#namearea>input")

    namebtn.onclick = function () {
        if (nameopen){
            namebtn.innerHTML = "+ Название для отображения в списке"
            namearea.style.display = "none"
            nameopen = false
        } else {
            namebtn.innerHTML = "– Название для отображения в списке"
            namearea.style.display = "block"
            nameopen = true
        }
    }

    timedisplay = document.getElementById("timedisplay")

    getsubmitbtn = document.getElementById("getsubmitbtn")

    filesendinput = document.getElementById("filesendinput")
    droparea = document.getElementById("droparea")

    droparea.ondragover = droparea.ondragenter = function(evt) {
        evt.preventDefault();
    };

    droparea.ondrop = function(evt) {
        // pretty simple -- but not for IE :(
        filesendinput.files = evt.dataTransfer.files;

        // If you want to use some of the dropped files
        const dT = new DataTransfer();
        dT.items.add(evt.dataTransfer.files[0]);
        filesendinput.files = dT.files;

        evt.preventDefault();
    };
};
function saveContent(){
    if (save){
        document.getElementById("savebtn").classList.remove("checked")
        save = false
    } else {
        document.getElementById("savebtn").classList.add("checked")
        save = true
    }
}

function changetime(o) {
    timedisplay.innerHTML = o.value+"<br>сек";
}

function controlSwitch(cmd){
    switch (cmd){
        case "text":
            if (!text) {
                filein.style.display = "none"
                textin.style.display = "block"


                document.getElementById("textbtn").classList.add("checked")
                document.getElementById("filebtn").classList.remove("checked")

                text = true
            }
            break
        case "file":
            if (text) {
                filein.style.display = "block"
                textin.style.display = "none"
                document.getElementById("textbtn").classList.remove("checked")
                document.getElementById("filebtn").classList.add("checked")
                text = false
            }
            break
        case "send":
            if (!send){
                send = true

                document.getElementById("sendbtn").classList.add("checked")
                document.getElementById("getbtn").classList.remove("checked")

                sendsection.style.display = "flex"
                getsection.style.display = "none"

            }
            break
        case "get":
            if (send){

                document.getElementById("sendbtn").classList.remove("checked")
                document.getElementById("getbtn").classList.add("checked")

                getsection.style.display = "flex"
                sendsection.style.display = "none"
                send = false
            }
            break

    }
}

function getcodeinput(o){
    if (o.value.length == 6){
        getsubmitbtn.disabled = false
    } else {
        getsubmitbtn.disabled = true
    }
}
function getContent(){
    textout.style.display = "block";

    textout.lastElementChild.innerHTML = "hfjlkgsbg434bglgbhlrgebhvl43brhl34blrghbgerlhj"
}

function sendContent(){

    if (text && textin.lastElementChild.innerHTML.length>0){
        document.getElementById("codesend").value = 174455
    }
    if (!text && document.getElementById("filesendinput").files.length != 0){
        document.getElementById("codesend").value = 174455
    }

}


