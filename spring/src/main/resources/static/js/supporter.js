state = 0;


var categoryArray = [];


function ClickImg() {
    var minielement = event.target;
    var clickimgvalue = minielement.getAttribute('value');
    console.log(clickimgvalue);

    //이미지들 각각 인식 성공. clickimgname의 이미지가
    //opacity 0.5면 1로 바꾸고 1이면 0으로 바꾸기.

    if(minielement.style.backgroundColor == "paleturquoise"){
        minielement.style.backgroundColor = "palegreen";
        var size = categoryArray.length;
        for(var i = 0; i < size; i++){
            if(categoryArray[i] == clickimgvalue) {
                categoryArray.splice(i, 1);
            }
        }
        console.log(categoryArray);
    } else {
        minielement.style.backgroundColor = "paleturquoise";
        categoryArray[categoryArray.length] = clickimgvalue;
        console.log(categoryArray)
    }

}

function clickNext() {
    var sendData = new XMLHttpRequest();
    sendData.onreadystatechange = function () {
        if (sendData.readyState == XMLHttpRequest.DONE) {
            if (sendData.status == 200) {
                document.body.innerText = sendData.responseText;
            } else {
                document.body.innerText = 'Error: ' + sendData.status;
            }
        }
    };

    //var data = 'This is my data';

    sendData.open('POST', '/supporterpage', true);
    sendData.send(categoryArray);
}

