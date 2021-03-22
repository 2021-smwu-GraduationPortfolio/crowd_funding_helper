var result = "";
//var resultArray = [];
function getCheckboxValue()  {
    const query = 'input[name="chk_info"]:checked';
    const selectedEls =
        document.querySelectorAll(query);
    selectedEls.forEach((el) => {
        result += el.id + ' ';
    });
    result = result + 'c' + 'p';
    // 출력
    //document.getElementById('result').innerText = result;

    //resultArray[resultArray.length] = result;
    // resultArray = Arrays.asList(result.split(" "));
    // console.log(result)
    // document.getElementById('resultArray').innerText = resultArray;

    var element = document.getElementById("zzimList");
    if(element.style.display == 'none'){
        element.style.display='block';
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
    sendData.open('POST', '/cart/buy', true);
    sendData.send(result);
}