"use strict";

function unboxPoints(points) {
  const result = []
  for (let i = 0; i < points.length; i++) {
    result.push(...points[i])
  }
  return result;
}

const triangles = new Delaunator([
  0.0, 0.0,
  0.0, 1.0,
  1.0, 1.0,
  1.0, 0.0,
  ...unboxPoints(getPoints("image1"))
]).triangles;
console.log([
  0.0, 0.0,
  0.0, 1.0,
  1.0, 1.0,
  1.0, 0.0,
  ...unboxPoints(getPoints("image1"))
])
console.log(triangles)

function createImageData(name) {
  const landmarks = [
    0.0, 0.0,
    0.0, 1.0,
    1.0, 1.0,
    1.0, 0.0,
  ]
  const points = getPoints(name);
  for (let i = 0; i < points.length; i++) {
    landmarks.push(...points[i])
  }

  const resultP = []

  for (let i = 0; i < triangles.length; i++) {
    let ind = triangles[i]
    resultP.push(landmarks[2 * ind], landmarks[2 * ind + 1])
  }
  return [resultP, resultP]
}


function createAnimationData(t) {
  // if (0.0 <= t && t <= 1.0)
  //   return [
  //     createImageData(getPoints("image1"), getPoints("image2"), t), [],
  //
  //   ]
  // else if (1.0 <= t && t <= 2) {
  //   return createImageData(
  //     getPoints("image2"),
  //     getPoints("image3"),
  //     t - 1.0
  //   )
  // }
  return [
    createImageData("image1"),
    createImageData("image2"),
    createImageData("image3"),
  ]
}

function main() {
  let image = new Image();
  image.src = "images/image1.jpg";  // MUST BE SAME DOMAIN!!!
  image.onload = function () {
    render(image, "canvas", createAnimationData, true);
  };
}

main();
