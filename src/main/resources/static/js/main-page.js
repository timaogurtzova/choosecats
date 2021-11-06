let catFirst = $('#catFirst').data('catfirstid');
let catLast = $('#catLast').data('catlastid');

$('#catFirstRef, #catFirstImg').on('click', function () {
    let winner = catFirst;
    let notwinner = catLast;
    sendRequest(winner, notwinner);
});

$('#catLastRef, #catLastImg').on('click', function () {
    let winner = catLast;
    let notwinner = catFirst;
    sendRequest(winner, notwinner);
});


function sendRequest(winner, notwinner) {
    console.log(winner + notwinner);
    let xhr = new XMLHttpRequest();
    let url = '/voteCat/' + winner + '/' + notwinner;
    xhr.open('POST', url, false);
    xhr.send();
    location.reload();
}