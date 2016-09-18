var plotDiv = document.getElementById('tester');
var line1 = {
  x: ['2013-10-04 22:23:00', '2013-11-04 22:23:00', '2013-12-04 22:23:00', '2013-12-11 22:23:00', '2014-01-01 22:22:00'],
  y: [1, 2, 4, 8, 16]
};
var line2 = {
  x: ['2013-10-04 22:23:00', '2013-11-04 22:23:00', '2013-12-04 22:23:00', '2013-12-11 22:23:00', '2014-01-01 22:22:00'],
  y: [1, 2, 3, 5, 1]
};

var longMatrixIndex = 0;
var shortMatrixIndex = 1;
var neutralMatrixIndex = 2;

var longCount = 0;
var shortCount = 0;
var neutralCount = 0;
var total = 0;

Plotly.plot(plotDiv, [
    {x: [], y: []}, // long matrix
    {x: [], y: []}, // short matrix
    {x: [], y: []}  // neutral matrix
  ],
  {
    margin: {t: 0}
  });


function WebSocketDataReceiver() {
  if (!("WebSocket" in window)) {
    alert("WebSocket NOT supported by your Browser!");
    return;
  }

  // Let us open a web socket
  var ws = new WebSocket("ws://" + window.location.hostname + ":9090/events/");

  ws.onopen = function () {
    // Web Socket is connected, send data using send()
    ws.send("reload");
    console.log("reload is sent...");
  };

  ws.onmessage = function (evt) {
    console.log("Message is received: ", evt.data);
    var twitMessage = JSON.parse(evt.data);
    var lineIndex;

    switch (twitMessage.bias) {
      case "Long":
        var percent = (++longCount / ++total) * 100;
        lineIndex = 0;
        break;

      case "Short":
        percent = (++shortCount / ++total) * 100;
        lineIndex = 1;
        break;

      case "Neutral":
        percent = (++neutralCount / ++total) * 100;
        lineIndex = 2;
        break;

      default:
        console.log('unknown type: ', twitMessage.bias);
    }

    var update = {x: [[twitMessage.createdAt]],y: [[percent.toFixed(0)]]};

    Plotly.extendTraces(plotDiv, update, [lineIndex], 2000);
  };

  ws.onclose = function () {
    // websocket is closed.
    alert("Connection is closed...");
  };
}