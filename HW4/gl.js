//
// Initialize a texture and load an image.
// When the image finished loading copy it into the texture.
//
function loadTexture(gl, url, index) {
  const texture = gl.createTexture();
  gl.bindTexture(gl.TEXTURE_2D, texture);

  // Because images have to be downloaded over the internet
  // they might take a moment until they are ready.
  // Until then put a single pixel in the texture so we can
  // use it immediately. When the image has finished downloading
  // we'll update the texture with the contents of the image.
  const level = 0;
  const internalFormat = gl.RGBA;
  const width = 1;
  const height = 1;
  const border = 0;
  const srcFormat = gl.RGBA;
  const srcType = gl.UNSIGNED_BYTE;
  const pixel = new Uint8Array([0, 0, 255, 255]);  // opaque blue
  gl.texImage2D(gl.TEXTURE_2D, level, internalFormat,
    width, height, border, srcFormat, srcType,
    pixel);

  const image = new Image();
  image.onload = function () {
    gl.activeTexture(index)
    gl.bindTexture(gl.TEXTURE_2D, texture);
    gl.texImage2D(gl.TEXTURE_2D, level, internalFormat,
      srcFormat, srcType, image);

    // WebGL1 has different requirements for power of 2 images
    // vs non power of 2 images so check if the image is a
    // power of 2 in both dimensions.
    // No, it's not a power of 2. Turn off mips and set
    // wrapping to clamp to edge
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
  };
  image.src = url;

  return texture;
}


function createProgram(gl, image, isAnimation) {
  // setup GLSL program
  let program = webglUtils.createProgramFromScripts(gl, ["vertex-shader-2d", "fragment-shader-2d"]);
  gl.useProgram(program);
  if (isAnimation) {
    let u_image1Location = gl.getUniformLocation(program, "u_image1");
    let u_image2Location = gl.getUniformLocation(program, "u_image2");
    let u_image3Location = gl.getUniformLocation(program, "u_image3");
    // set which texture units to render with.
    gl.uniform1i(u_image1Location, 1);  // texture unit 1
    gl.uniform1i(u_image2Location, 2);  // texture unit 2
    gl.uniform1i(u_image3Location, 3);  // texture unit 3

    loadTexture(gl, "images/image1.jpg", gl.TEXTURE1)
    loadTexture(gl, "images/image2.jpg", gl.TEXTURE2)
    loadTexture(gl, "images/image3.jpg", gl.TEXTURE3)
  }


  // look up where the vertex data needs to go.
  let positionLocation = gl.getAttribLocation(program, "a_position");
  let texcoordLocation = gl.getAttribLocation(program, "a_texCoord");

  let position1Location = gl.getAttribLocation(program, "a_position1");
  let texcoord1Location = gl.getAttribLocation(program, "a_texCoord1");
  let position2Location = gl.getAttribLocation(program, "a_position2");
  let texcoord2Location = gl.getAttribLocation(program, "a_texCoord2");
  let position3Location = gl.getAttribLocation(program, "a_position3");
  let texcoord3Location = gl.getAttribLocation(program, "a_texCoord3");
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
  // set the resolution
  gl.uniform2f(resolutionLocation, gl.canvas.width, gl.canvas.height);

  let tInLocation = gl.getUniformLocation(program, "t_in");
  webglUtils.resizeCanvasToDisplaySize(gl.canvas);

  // Tell WebGL how to convert from clip space to pixels
  gl.viewport(0, 0, gl.canvas.width, gl.canvas.height);

  // Create a buffer to put three 2d clip space points in
  let positionBuffer = gl.createBuffer();

  // provide texture coordinates for the rectangle.
  let texcoordBuffer = gl.createBuffer();

  let positionBuffer1 = gl.createBuffer();
  let texcoordBuffer1 = gl.createBuffer();
  let positionBuffer2 = gl.createBuffer();
  let texcoordBuffer2 = gl.createBuffer();
  let positionBuffer3 = gl.createBuffer();
  let texcoordBuffer3 = gl.createBuffer();

  return {
    program,
    positionLocation,
    texcoordLocation,
    positionBuffer,
    texcoordBuffer,
    tInLocation,
    positionBuffer1,
    texcoordBuffer1,
    positionBuffer2,
    texcoordBuffer2,
    positionBuffer3,
    texcoordBuffer3,
    position1Location,
    texcoord1Location,
    position2Location,
    texcoord2Location,
    position3Location,
    texcoord3Location,
  }
}


function dastan(gl, location, buffer) {
  // Turn on the position attribute
  gl.enableVertexAttribArray(location);

  // Bind the position buffer.
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer);

  {
    // Tell the position attribute how to get data out of positionBuffer (ARRAY_BUFFER)
    let size = 2;          // 2 components per iteration
    let type = gl.FLOAT;   // the data is 32bit floats
    let normalize = false; // don't normalize the data
    let stride = 0;        // 0 = move forward size * sizeof(type) each iteration to get the next position
    let offset = 0;        // start at the beginning of the buffer
    gl.vertexAttribPointer(
      location, size, type, normalize, stride, offset);
  }

}

function fillBuffer(gl, positionBuffer, datum, count) {
  if (datum.length < count) {
    datum = []
    for (let i = 0; i < count; i++) {
      datum.push(0, 0)
    }
  }
  gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(datum), gl.STATIC_DRAW);
}

function draw(gl, prog, data, t, count) {
  // Tell it to use our program (pair of shaders)
  gl.useProgram(prog.program);

  gl.uniform1f(prog.tInLocation, t);


  fillBuffer(gl, prog.positionBuffer, data[0], count)
  fillBuffer(gl, prog.texcoordBuffer, data[1], count)

  fillBuffer(gl, prog.positionBuffer1, data[2], count)
  fillBuffer(gl, prog.texcoordBuffer1, data[3], count)

  fillBuffer(gl, prog.positionBuffer2, data[4], count)
  fillBuffer(gl, prog.texcoordBuffer2, data[5], count)

  fillBuffer(gl, prog.positionBuffer3, data[6], count)
  fillBuffer(gl, prog.texcoordBuffer3, data[7], count)


  dastan(gl, prog.positionLocation, prog.positionBuffer)
  dastan(gl, prog.texcoordLocation, prog.texcoordBuffer)

  dastan(gl, prog.position1Location, prog.positionBuffer1)
  dastan(gl, prog.texcoord1Location, prog.texcoordBuffer1)

  dastan(gl, prog.position2Location, prog.positionBuffer2)
  dastan(gl, prog.texcoord2Location, prog.texcoordBuffer2)

  dastan(gl, prog.position3Location, prog.positionBuffer3)
  dastan(gl, prog.texcoord3Location, prog.texcoordBuffer3)
  {
    // Draw the rectangle.
    let primitiveType = gl.TRIANGLES;
    let offset = 0;
    gl.drawArrays(primitiveType, offset, count);
  }
}

function render(image, canvasID, dataFunction, isAnimation) {
  // Get A WebGL context
  /** @type {HTMLCanvasElement} */
  let canvas = document.querySelector(`#${canvasID}`);
  let gl = canvas.getContext("webgl");
  if (!gl) {
    return;
  }
  const prog1 = createProgram(gl, image, isAnimation);
  const prog2 = isAnimation ? null : createProgram(gl, image, isAnimation);
  let startTime = new Date().getTime()

  setInterval(function () {
    // Clear the canvas
    gl.clearColor(0, 0, 0, 0);
    gl.clear(gl.COLOR_BUFFER_BIT);

    let t = (new Date().getTime() - startTime) / 3000
    if (t > 4) {
      t = 0
      startTime = new Date().getTime()
    }
    if (t > 2.0) {
      t = 4 - t;
    }

    if (!isAnimation)
      t = -1;
    const data = dataFunction(t)
    if (data.length === 3) {
      draw(gl, prog1, [
        [], [],
        ...data[0],
        ...data[1],
        ...data[2],
      ], t, data[0][0].length)
    } else {
      draw(gl, prog1, [
        data[0], data[1],
        [], [],
        [], [],
        [], [],
      ], -1, data[0].length)
      if (data.length > 2)
        draw(gl, prog2, [
          data[2], data[3],
          [], [],
          [], [],
          [], [],
        ], -1, data[2].length)
    }
  }, 10)
}
