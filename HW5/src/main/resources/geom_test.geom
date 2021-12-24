#version 430 core
layout (lines) in;
layout (line_strip, max_vertices = 6) out;

#define PI 3.14159265359

//  Predefined interfaces:
//in gl_PerVertex
//{
//    vec4 gl_Position;
//    float gl_PointSize;
//    float gl_ClipDistance[];
//} gl_in[];

//out gl_PerVertex
//{
//    vec4 gl_Position;
//    float gl_PointSize;
//    float gl_ClipDistance[];
//};

in VertexData {
    vec3 vColor;
} v_in[];

out vec3 fColor;

uniform float eyeSize;

vec2 rotate(vec2 a, float angle) {
    mat2 rotMat = mat2(cos(angle), -sin(angle),
                       sin(angle), cos(angle));
    return rotMat * a;
}

void emitVert(vec2 pos) {
    gl_Position = vec4(pos, 0.0, 1.0);
    EmitVertex();
}

void main() {
    vec2 pos0 = gl_in[0].gl_Position.xy;
    vec2 pos1 = gl_in[1].gl_Position.xy;
    vec2 direction = eyeSize * normalize(pos0 - pos1);
    vec2 rotatedDir = rotate(direction, PI / 2.0);

    // First eye
    fColor = v_in[0].vColor;
    emitVert(-rotatedDir + direction + pos0);
    emitVert(pos0);
    emitVert(rotatedDir + direction + pos0);
    EndPrimitive();

    // Second eye
    fColor = v_in[1].vColor;
    emitVert(rotatedDir - direction + pos1);
    emitVert(pos1);
    emitVert(-rotatedDir - direction + pos1);
    EndPrimitive();
}
