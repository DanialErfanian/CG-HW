#version 430 core

in vec3 color;
in vec3 Normal;
in vec4 FragPos;

uniform vec3 uColor;

out vec4 fragColor;

void main() {
    //ambinet
    vec3 lightColor = vec3(1.0, 1.0, 1.0);
    float ambientStrength = 0.1;
    vec3 ambientLight = ambientStrength * lightColor;

    //diffuse
    vec3 norm = normalize(Normal);
    vec3 lightPos = vec3(-5.0, -5.0, -2.0);
    vec3 lightDir = normalize(lightPos - vec3(FragPos));

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuseLight = diff * lightColor;

    vec3 result = (ambientLight + diffuseLight) * uColor;
    fragColor = vec4(result, 1.0);
}
