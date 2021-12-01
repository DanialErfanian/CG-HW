#version 430 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoord;
layout (location = 3) in float movingPart;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 movingPartMat;

out vec3 color;
out vec2 TexCoord;
out vec4 FragPos;
out vec3 Normal;
out float textureFlag;

void main() {
    FragPos = model * vec4(aPos, 1.0);
    if (movingPart > 0.5){
        gl_Position = projection * view * model * movingPartMat * vec4(aPos, 1.0);
        textureFlag = 1;
    } else {
        gl_Position = projection * view * model * vec4(aPos, 1.0);
        textureFlag = 0;
    }
    TexCoord = aTexCoord;
    Normal = mat3(transpose(inverse(model))) * aNormal;
}
