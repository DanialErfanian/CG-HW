"use strict";

let frame = 0;
const landmarks = [
  0.0, 0.0,
  0.0, 1.0,
  1.0, 1.0,
  1.0, 0.0,
]

const texturePoints = [
  0.0, 0.0,
  0.0, 1.0,
  1.0, 1.0,
  1.0, 0.0,
]

const points = getPoints("image1")
for (let i = 0; i < points.length; i++) {
  landmarks.push(...points[i])
  texturePoints.push(...points[i])
}

function createAnimationData() {
  const resultP = []
  const resultT = [];

  const triangles = new Delaunator(landmarks).triangles;
  for (let i = 0; i < triangles.length; i++) {
    let ind = triangles[i]
    resultP.push(landmarks[2 * ind], landmarks[2 * ind + 1])
    resultT.push(texturePoints[2 * ind], texturePoints[2 * ind + 1])
  }
  for (let i = 4; 2 * i < landmarks.length; i++) {
    let sgn = +1;
    if (frame % 80 >= 40)
      sgn = -1;
    landmarks[2 * i] += sgn * 0.002
    landmarks[2 * i + 1] += sgn * 0.002
  }
  frame += 1;
  return [resultP, resultT]
}


function main() {
  let image = new Image();
  image.src = "images/image1.jpg";  // MUST BE SAME DOMAIN!!!
  image.onload = function () {
    render(image, "canvas", createAnimationData);
  };
}

main();
