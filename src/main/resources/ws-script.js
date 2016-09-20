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
        var twitDto = JSON.parse(msg.payload);
        // todo better throttling not to freeze UI
        setTimeout(function () {
          plotTradeBias(twitDto);
          populateMessageDashboard(twitDto);
        }, 200);
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

function plotTradeBias(twitDto) {
  console.log(total, " Message is received: ", twitDto);
  var lineIndex;

  switch (twitDto.bias) {
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
      console.log('unknown type: ', twitDto.bias);
  }

  // be gentle with plotly to push data every 100ms, not too fast
  if (total > 20) {
    var update = {x: [[twitDto.createdAt]], y: [[percent.toFixed(0)]]};
    Plotly.extendTraces(plotDiv, update, [lineIndex], 2000);
  }
}

var longListElement = document.getElementById('LongList');
var shortListElement = document.getElementById('ShortList');
var neutralListElement = document.getElementById('NeutralList');

function populateMessageDashboard(twitDto) {
  var listElement;
  var count = 0;

  switch (twitDto.bias) {
    case "Long":
      listElement = longListElement;
      count = longCount;
      break;

    case "Short":
      listElement = shortListElement;
      count = shortCount;
      break;

    case "Neutral":
      listElement = neutralListElement;
      count = neutralCount;
      break;

    default:
  }

  var node = document.createElement("a");
  node.innerHTML = '#' + count + '. ' + twitDto.createdAt + ': ' + twitDto.message;
  node.href = "#";
  node.className = "list-group-item list-group-item-action";

  listElement.insertBefore(node, listElement.firstChild);
}
