"use strict";


function show_image(name) {
  let image = new Image();
  image.src = `images/${name}.jpg`;  // MUST BE SAME DOMAIN!!!
  image.onload = function () {
    render_image(image, name);
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

function render_image(image, image_name) {
  // Get A WebGL context
  /** @type {HTMLCanvasElement} */
  let canvas = document.querySelector(`#${image_name}`);
  let gl = canvas.getContext("webgl");
  if (!gl) {
    return;
  }

  // setup GLSL program
  let program = webglUtils.createProgramFromScripts(gl, ["vertex-shader-2d", "fragment-shader-2d"]);

  // look up where the vertex data needs to go.
  let positionLocation = gl.getAttribLocation(program, "a_position");
  let texcoordLocation = gl.getAttribLocation(program, "a_texCoord");

  // Create a buffer to put three 2d clip space points in
  let positionBuffer = gl.createBuffer();

  // Create a texture.
  let texture = gl.createTexture();
  gl.bindTexture(gl.TEXTURE_2D, texture);

  // Set the parameters so we can render any size image.
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);

  // Upload the image into the texture.
  gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, image);

  // lookup uniforms
  let resolutionLocation = gl.getUniformLocation(program, "u_resolution");

  webglUtils.resizeCanvasToDisplaySize(gl.canvas);

  // Tell WebGL how to convert from clip space to pixels
  gl.viewport(0, 0, gl.canvas.width, gl.canvas.height);

  function draw() {
    // Clear the canvas
    gl.clearColor(0, 0, 0, 0);
    gl.clear(gl.COLOR_BUFFER_BIT);

    // Tell it to use our program (pair of shaders)
    gl.useProgram(program);

    // Bind it to ARRAY_BUFFER (think of it as ARRAY_BUFFER = positionBuffer)
    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);
    // Set a rectangle the same size as the image.

    const data = createImageData()
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(data[0]), gl.STATIC_DRAW);

    // provide texture coordinates for the rectangle.
    let texcoordBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, texcoordBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(data[1]), gl.STATIC_DRAW);


    // Turn on the position attribute
    gl.enableVertexAttribArray(positionLocation);

    // Bind the position buffer.
    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);

    {
      // Tell the position attribute how to get data out of positionBuffer (ARRAY_BUFFER)
      let size = 2;          // 2 components per iteration
      let type = gl.FLOAT;   // the data is 32bit floats
      let normalize = false; // don't normalize the data
      let stride = 0;        // 0 = move forward size * sizeof(type) each iteration to get the next position
      let offset = 0;        // start at the beginning of the buffer
      gl.vertexAttribPointer(
        positionLocation, size, type, normalize, stride, offset);
    }

    // Turn on the texcoord attribute
    gl.enableVertexAttribArray(texcoordLocation);

    // bind the texcoord buffer.
    gl.bindBuffer(gl.ARRAY_BUFFER, texcoordBuffer);

    {
      // Tell the texcoord attribute how to get data out of texcoordBuffer (ARRAY_BUFFER)
      let size = 2;          // 2 components per iteration
      let type = gl.FLOAT;   // the data is 32bit floats
      let normalize = false; // don't normalize the data
      let stride = 0;        // 0 = move forward size * sizeof(type) each iteration to get the next position
      let offset = 0;        // start at the beginning of the buffer
      gl.vertexAttribPointer(
        texcoordLocation, size, type, normalize, stride, offset);
    }

    // set the resolution
    gl.uniform2f(resolutionLocation, gl.canvas.width, gl.canvas.height);

    {
      // Draw the rectangle.
      let primitiveType = gl.TRIANGLES;
      let offset = 0;
      let count = data[0].length;
      gl.drawArrays(primitiveType, offset, count);
    }
  }

  draw()
  setInterval(draw, 100)
}

show_image("image1");
show_image("image2");
show_image("image3");
