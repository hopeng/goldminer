var plotDiv = document.getElementById('tester');

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

var messageCount = 0;
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
    var msg = JSON.parse(evt.data);
    switch (msg.eventType) {
      case "TradeBias":
        plotTradeBias(msg.payload);
        break;

      case "PriceQuote":
        plotPriceChart(msg.payload);
        break;

      default:
        console.log("unknown event: ", evt);
    }
  };

  ws.onclose = function () {
    // websocket is closed.
    alert("Connection is closed...");
  };
}

function plotTradeBias(data) {
  messageCount++;
  console.log(messageCount, " Message is received: ", data);
  var twitMessage = JSON.parse(data);
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

  // be gentle with plotly to push data every 100ms, not too fast
  setTimeout(function () {
    if (percent === 100) { // skip false percentage, todo fix the 100% calc
      return;
    }
    var update = {x: [[twitMessage.createdAt]], y: [[percent.toFixed(0)]]};
    Plotly.extendTraces(plotDiv, update, [lineIndex], 2000);
  }, 200);
}
