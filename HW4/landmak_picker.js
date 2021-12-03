"use strict";


function extractXY(e) {
  let rect = e.target.getBoundingClientRect();
  let x = (e.clientX - rect.left) / rect.width; // x position within the element.
  let y = (e.clientY - rect.top) / rect.height;  // y position within the element.
  return [x, y]
}

function show_image(name) {
  let image = new Image();
  image.src = `images/${name}.jpg`;  // MUST BE SAME DOMAIN!!!
  image.onload = function () {
    render(image, name, createImageData, false);
  };

  const points = getPoints(name)
  console.log("fsdf", points)
  const mouseXY = [-1, -1]

  function createPoints() {
    const stroke = 0.02
    const result = [[], []]
    for (let i = 0; i < points.length; i++) {
      const point = points[i]
      doTheShit(point)
    }
    doTheShit(mouseXY)

    function doTheShit(point) {
      const x = point[0];
      const y = point[1];
      result[0].push(
        x - stroke, y - stroke,
        x + stroke, y - stroke,
        x + stroke, y + stroke,
        x - stroke, y - stroke,
        x - stroke, y + stroke,
        x + stroke, y + stroke,
      )
      result[1].push(
        -1.0, -1.0,
        -1.0, -1.0,
        -1.0, -1.0,
        -1.0, -1.0,
        -1.0, -1.0,
        -1.0, -1.0,
      )
    }

    return result;
  }

  function createImageData() {
    const corners = [
      0.0, 0.0,
      0.0, 1.0,
      1.0, 1.0,
      0.0, 0.0,
      1.0, 1.0,
      1.0, 0.0,
    ];
    return [corners, corners, ...createPoints()]
  }


  let canvas = document.querySelector(`#${name}`);
  canvas.addEventListener("mouseout", function (e) {
    // clear mouse indicator for better UI =)
    mouseXY[0] = -1;
    mouseXY[1] = -1;
  })
  canvas.addEventListener("mousemove", function (e) {
    const point = extractXY(e)
    mouseXY[0] = point[0]
    mouseXY[1] = point[1]
  })
  canvas.addEventListener("click", function (e) {
    const point = extractXY(e)
    points.push(point)
    addPoint(name, point)
  })
}

show_image("image1");
show_image("image2");
show_image("image3");
