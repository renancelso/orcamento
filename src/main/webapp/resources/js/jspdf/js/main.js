document.addEventListener('tizenhwkey', function(e) {
  if (e.keyName === 'back') {
    try {
      tizen.application.getCurrentApplication().exit();
    } catch (error) {
      console.error('getCurrentApplication(): ' + error.message);
    }
  }
});

var errorHandler = function(e) {
  console.log('Error' + e.message);
};

var getExampleOutput = function(example, callback) {
  var output = function(doc) {
    callback(new Uint8Array(doc.output('arraybuffer')));
  };

  switch (example) {
    case 1:
      var pdfDoc = new jsPDF('landscape', 'mm', 'a5');

      pdfDoc.addPage('100', '100');
      pdfDoc.addPage('200', '200');
      pdfDoc.addPage('300', '300');

      output(pdfDoc);
      break;
    case 2:
      var pdfDoc = new jsPDF();

      pdfDoc.setFont("courier", "italic");
      pdfDoc.setFontSize(40);
      pdfDoc.setTextColor(255, 0, 0);
      pdfDoc.text(10, 10, 'Hello Tizen!');

      pdfDoc.setFont("courier", "bolditalic");
      pdfDoc.setFontSize(60);
      pdfDoc.setTextColor(0, 0, 255);
      pdfDoc.text(10, 70, 'Hello Tizen!');

      pdfDoc.setFont("times", "bold");
      pdfDoc.setFontSize(80);
      pdfDoc.setTextColor(0, 100, 100);
      pdfDoc.text(10, 150, 'Hello Tizen!');

      pdfDoc.setFont("helvetica");
      pdfDoc.setFontSize(40);
      pdfDoc.setTextColor(100, 0, 100);
      pdfDoc.text(10, 190, 'Hello Tizen!');

      output(pdfDoc);
      break;
    case 3:
      var img = new Image();
      img.addEventListener('load', function() {
        var pdfDoc = new jsPDF();

        pdfDoc.addImage(img, 'png', 10, 50, 107.6, 35.6);

        output(pdfDoc);
      });
      img.src = 'images/tizen.png';
      break;
    case 4:
      var pdfDoc = new jsPDF();

      pdfDoc.setFillColor(100, 200, 240);
      pdfDoc.setDrawColor(100, 100, 0);
      pdfDoc.setLineWidth(1);

      // Empty ellipse
      pdfDoc.ellipse(50, 50, 10, 5);
      // Filled ellipse
      pdfDoc.ellipse(100, 50, 10, 5, 'F');
      // Filled circle with borders
      pdfDoc.circle(150, 50, 5, 'FD');


      // Empty square
      pdfDoc.rect(50, 100, 10, 10);
      // Filled square
      pdfDoc.rect(100, 100, 10, 10, 'F');
      // Filled square with borders
      pdfDoc.rect(150, 100, 10, 10, 'FD');

      // Filled sqaure with rounded corners
      pdfDoc.roundedRect(50, 150, 10, 10, 3, 3, 'FD');

      // Filled triangle with borders
      pdfDoc.triangle(50, 200, 60, 200, 55, 210, 'FD');

      // Line
      pdfDoc.line(50, 250, 100, 250);

      output(pdfDoc);
      break;
  }
};

var saveExample = function(example) {
  if (example < 1 || example > 4) return;

  var fileName = prompt('How to name the example PDF file?');
  if (!fileName || fileName.length === 0) return;

  if (fileName.substr(-4, 4).toLowerCase() !== '.pdf') {
    fileName += '.pdf';
  }

  tizen.filesystem.resolve('documents', function(dir) {
    var file = dir.createFile(fileName);
    file.openStream('w', function(stream) {
      getExampleOutput(example, function(output) {
        var chunk = 16384;
        for (var beg = 0; beg < output.length; beg += chunk) {
          stream.writeBytes(Array.apply(null, output.subarray(beg, beg + chunk)));
        }
        stream.close();
      });
    }, errorHandler);
  }, errorHandler);
};

var examples = document.querySelectorAll('button');
Array.prototype.forEach.call(examples, function(example) {
  example.addEventListener('click', function() {
    saveExample(parseInt(example.dataset.example, 10));
  });
});
