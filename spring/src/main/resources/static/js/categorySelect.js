state = 0;


var categoryArray = [];


function ClickImg() {
    var minielement = event.target;
    var clickimgvalue = minielement.getAttribute('value');
    console.log(clickimgvalue);

    //이미지들 각각 인식 성공. clickimgname의 이미지가
    //opacity 0.5면 1로 바꾸고 1이면 0으로 바꾸기.

    if(minielement.style.opacity == 0.5){
        minielement.style.opacity = 1;
        var size = categoryArray.length;
        for(var i = 0; i < size; i++){
            if(categoryArray[i] == clickimgvalue) {
                categoryArray.splice(i, 1);
            }
        }
        console.log(categoryArray);
    } else {
        minielement.style.opacity = 0.5;
        categoryArray[categoryArray.length] = clickimgvalue;
        console.log(categoryArray)
    }
}


