#version 430 core

in vec2 TexCoord;
in vec3 Normal;
in vec4 FragPos;
in float textureFlag;

out vec4 fragColor;

layout(binding=0) uniform sampler2D boxTexture;
layout(binding=1) uniform sampler2D boxTexture2;

void main() {
    if (textureFlag > 0.5){
        fragColor = texture(boxTexture2, TexCoord);
        fragColor *= 0.7f;
    } else {
        fragColor = texture(boxTexture, TexCoord);
    }
    vec4 lightColor = vec4(0.5, 1.0, 1.0, 1.0);
    float ambientStrength = 0.8;
    vec4 ambient = ambientStrength * lightColor;
    //diffuse
    vec3 norm = normalize(Normal);
    vec3 lightPos = vec3(-5.0, -5.0, -2.0);
    vec3 lightDir = normalize(lightPos - vec3(FragPos));
    float diff = max(dot(norm, lightDir), 0.0);
    vec4 diffuseLight = diff * lightColor;
    fragColor = (ambient + diffuseLight) * fragColor;
}
