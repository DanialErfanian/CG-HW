"use strict";


function show_image(name) {
  let image = new Image();
  image.src = `images/${name}.jpg`;  // MUST BE SAME DOMAIN!!!
  image.onload = function () {
    render(image, name, createImageData);
  };
}

function createImageData() {
  const points = [
    0.0, 0.0,
    0.0, 1.0,
    1.0, 1.0,
    0.0, 0.0,
    1.0, 1.0,
    1.0, 0.0,
  ];
  return [points, points]
}


show_image("image1");
show_image("image2");
show_image("image3");
