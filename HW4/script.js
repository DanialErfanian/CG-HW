"use strict";

let frame = 0;
const landmarks = [
  0.0, 0.0,
  0.0, 1.0,
  1.0, 1.0,
  1.0, 0.0,
  0.25, 0.25,
  0.75, 0.25,
  0.25, 0.75,
  0.75, 0.75,
]

const texturePoints = [
  0.0, 0.0,
  0.0, 1.0,
  1.0, 1.0,
  1.0, 0.0,
  0.25, 0.25,
  0.75, 0.25,
  0.25, 0.75,
  0.75, 0.75,
]


function createAnimationData() {
  const resultP = []
  const resultT = [];

  const triangles = new Delaunator(landmarks).triangles;
  for (let i = 0; i < triangles.length; i++) {
    let ind = triangles[i]
    resultP.push(landmarks[2 * ind], landmarks[2 * ind + 1])
    resultT.push(texturePoints[2 * ind], texturePoints[2 * ind + 1])
  }
  for (let i = 4; frame < 100 && 2 * i < landmarks.length; i++) {
    landmarks[2 * i] += 0.001
    landmarks[2 * i + 1] += 0.001
  }
  console.log(frame)
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
